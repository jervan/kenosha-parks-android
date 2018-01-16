package edu.uwp.appfactory.eventsmanagement.RealmControllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.uwp.appfactory.eventsmanagement.model.CalendarAPI.Calendar;
import edu.uwp.appfactory.eventsmanagement.model.CalendarAPI.Item;
import edu.uwp.appfactory.eventsmanagement.model.Realm.Event;
import edu.uwp.appfactory.eventsmanagement.network.EventService;
import edu.uwp.appfactory.eventsmanagement.network.RetrofitClient;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jeremiah on 6/11/17.
 */

public class RealmUpdater {

    private static final String TAG = "RealmWriter";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());

    private Realm realm;
    private Context context;
    private int requestNumber;
    private Map<String, Event> events;
    private Map<String, String> baseURLs;
    private RealmUpdateCallback callback;

    public RealmUpdater(Map<String, String> baseURLs, Context context, RealmUpdateCallback callback) {
        this.realm = Realm.getDefaultInstance();
        this.context = context;
        this.requestNumber = 0;
        this.events = new HashMap<>();
        this.baseURLs = baseURLs;
        this.callback = callback;
        Log.d(TAG, "update started");
        updateEvents();
    }

    public void updateEvents() {

        List<String> locations = new LinkedList<>(baseURLs.keySet());
        final String location = locations.get(requestNumber++);
        String url = baseURLs.get(location);
        EventService eventService = RetrofitClient.getClient(url).create(EventService.class);
        Call<Calendar> call = eventService.getEvents(dateFormat.format(new Date()), true);
        call.enqueue(new Callback<Calendar>() {
            @Override
            public void onResponse(@NonNull Call<Calendar> call, @NonNull Response<Calendar> response) {
                Calendar calendar = response.body();
                if (calendar != null) {
                    createEvents(location, calendar);
                    if (requestNumber == baseURLs.size()) {
                        updateRealmEvents();
                        deleteMissingEventIds();
                        Log.d(TAG, "update complete");
                        close();
                        callback.updateComplete();
                    } else {
                        updateEvents();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Calendar> call, @NonNull Throwable t) {
                Toast.makeText(context, "Connect to the internet for the latest event information.", Toast.LENGTH_LONG).show();
                Log.e(TAG,"update failed");
                close();
            }
        });

    }

    private synchronized void createEvents(String location, Calendar calendar) {

        List<Item> items = calendar.getItems();
        for (Item item : items) {
            try {
                Event event = new Event(context, location, item);
                events.put(item.getId(), event);
            } catch (NullPointerException | ParseException e) {
                Log.e(TAG, item.toString());
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private synchronized void updateRealmEvents() {
        realm.executeTransaction(realm -> realm.copyToRealmOrUpdate(events.values()));
    }

    private synchronized void deleteMissingEventIds() {

        String[] objectKeys = events.keySet().toArray(new String[events.keySet().size()]);

        final RealmResults<Event> deleteObjects;
        if (objectKeys.length > 0) {
            deleteObjects = realm.where(Event.class).not().in("id", objectKeys).findAll();
        } else {
            deleteObjects = realm.where(Event.class).findAll();
        }

        Log.d(TAG, "Removing: " + deleteObjects.toString());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                deleteObjects.deleteAllFromRealm();
            }
        });
    }

    public void close() {
        if (realm != null) {
            realm.close();
        }
        realm = null;
    }

    public interface RealmUpdateCallback {

        void updateComplete();
    }


}
