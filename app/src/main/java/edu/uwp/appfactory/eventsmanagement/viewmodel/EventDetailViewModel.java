package edu.uwp.appfactory.eventsmanagement.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import edu.uwp.appfactory.eventsmanagement.model.CalendarAPI.Item;
import edu.uwp.appfactory.eventsmanagement.model.Realm.Event;
import edu.uwp.appfactory.eventsmanagement.network.EventService;
import edu.uwp.appfactory.eventsmanagement.network.RetrofitClient;
import edu.uwp.appfactory.eventsmanagement.util.Config;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChenMingxi on 4/21/17.
 *
 * https://nullpointer.wtf/android/mvvm-architecture-data-binding-library/
 */

public class EventDetailViewModel extends EventItemViewModel {
    private final Event event;
    private Activity context;
    private String prevEventName = "";
    private static final String TAG = "EventDetailViewModel";

    public EventDetailViewModel(Event event, Activity context) {
        super(context, event);
        this.event = event;
        this.context = context;
    }

    public String getTitle() {
        //return "Child Event";
        return event.getTitle();
    }

    @Bindable
    public int getShowDistance() { return super.getShowDistance(); }

    @Bindable
    public String getDistance() { return super.getDistance(); }

    public int getShowOrganization() { return getOrganization() != null ? View.VISIBLE : View.GONE; }

    public int getShowContact() { return getContact() != null ? View.VISIBLE : View.GONE; }

    public int getShowRegistration() { return getRegistrationMethod() != null ? View.VISIBLE : View.GONE; }

    public int getShowSchedule() { return getSchedule() != null ? View.VISIBLE : View.GONE; }

    public String getIsIndoors() {
        return event.getIsIndoor() ? "Indoors" : "Outdoors";
    }

    public String getDescription() {
        return event.getDescription();
    }

    public String getDateAndTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MMMM d", Locale.getDefault());
        String startDate = dateFormat.format(event.getStartDate());
        String endDate = dateFormat.format(event.getEndDate());
        if (startDate.equals(endDate)) {
            if (getStartTime().equals("ALL DAY")) {
                return startDate + "\n" + getStartTime();
            } else {
                return startDate + "\n" + getStartTime() + " to " + getEndTime();
            }
        } else {
            return startDate + " " + getStartTime() + "\nto\n" + endDate + " " + getEndTime();
        }
    }

    public String getRegistrationMethod() {
        return event.getRegistration();
    }

    public String getSchedule() { return event.getSchedule(); }

    public String getOrganization() {
        return event.getOrganization();
    }

    public String getLocationName() {
        return super.getLocationName();
    }

    public String getStartTime() {
        return super.getStartTime();
    }

    public String getEndTime() {
        return super.getEndTime();
    }

    public String getContact() {
        return event.getContact();
    }

    public LocationViewModel getLocationViewModel() {
        return new LocationViewModel(event.getLocationName(), event.getAddress(), event.getLat(), event.getLong());
    }

    public String getImage() {
        return event.getImage();
    }

    @BindingAdapter("image")
    public static void loadImage(final ImageView imageView, String imageUrl) {
        final Context context = imageView.getContext();
        Log.d("Image", imageUrl);

        Glide.with(context)
                .load(imageUrl)
                .fitCenter()
                .into(imageView);
    }

    public String getPrevEventName() {
        return prevEventName;
    }

    public void setPrevEventName(String prevEventName) {
        this.prevEventName = prevEventName;
    }

    public void addEventToCalendar(View v) {
        if (event.isReoccurring()) {
            new AlertDialog.Builder(context)
                    .setTitle("Add Event")
                    .setMessage("This event is a recurring event. Would you like to add this date or all dates?")
                    .setNeutralButton("This Date", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addSingleEvent();
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("All Dates", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addRecurringEvent();
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            addSingleEvent();
        }
    }

    private void addSingleEvent() {
        Log.d(TAG, "Call function add to calendar");
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, event.getTitle());
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.getStartDate().getTime());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.getEndDate().getTime());
        intent.putExtra(CalendarContract.Events.ALL_DAY, getStartTime().equals("ALL DAY"));
        intent.putExtra(CalendarContract.Events.DESCRIPTION, event.getDescription());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, event.getLocationName());
        context.startActivity(intent);
        prevEventName = event.getTitle();
    }

    private void addRecurringEvent() {
        final String calendarName = event.getLocationName();
        EventService eventService = RetrofitClient.getClient(Config.BASE_URLS.get(calendarName)).create(EventService.class);
        Call<Item> call = eventService.getEvent(event.getRecurringEventId());
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                try {
                    Item item = response.body();
                    if (item != null) {
                        Event event = new Event(context, calendarName, item);
                        Log.d(TAG, event.toString());
                        Log.d(TAG, "Call function add to calendar");
                        Intent intent = new Intent(Intent.ACTION_EDIT);
                        intent.setType("vnd.android.cursor.item/event");
                        intent.putExtra(CalendarContract.Events.TITLE, event.getTitle());
                        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.getStartDate().getTime());
                        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.getEndDate().getTime());
                        intent.putExtra(CalendarContract.Events.ALL_DAY, timeFormat.format(event.getStartDate()).equals("12:00 AM"));
                        intent.putExtra(CalendarContract.Events.DESCRIPTION, event.getDescription());
                        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, event.getLocationName());
                        intent.putExtra(CalendarContract.Events.RRULE, item.getRecurrence().toString().replace("[RRULE:", "").replace("]", ""));
                        context.startActivity(intent);
                        prevEventName = event.getTitle();
                    } else {
                        Toast.makeText(context, "Unable to add recurring event", Toast.LENGTH_LONG).show();
                    }
                } catch (ParseException | NullPointerException e) {
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(context, "Unable to add recurring event", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                Toast.makeText(context, "Connect to the Internet to add recurring event", Toast.LENGTH_LONG).show();
            }
        });
    }
}
