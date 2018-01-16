package edu.uwp.appfactory.eventsmanagement;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

import edu.uwp.appfactory.eventsmanagement.Components.LocationSnippetFragment;
import edu.uwp.appfactory.eventsmanagement.databinding.ActivityEventDetailBinding;
import edu.uwp.appfactory.eventsmanagement.model.Realm.Event;
import edu.uwp.appfactory.eventsmanagement.model.Realm.ReminderInfo;
import edu.uwp.appfactory.eventsmanagement.RealmControllers.RealmReader;
import edu.uwp.appfactory.eventsmanagement.util.ReminderUtil;
import edu.uwp.appfactory.eventsmanagement.viewmodel.EventDetailViewModel;
import io.realm.Realm;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Subscription;
import rx.functions.Action1;

public class EventDetailActivity extends AppCompatActivity {

    private static final String TAG = "EventDetailActivity";

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 111;

    private static final int GOOGLE_PLAY_SERVICES_REQUEST = 222;

    private Subscription updatableLocationSubscription;

    private Subscription lastKnownLocationSubscription;

    private Event event;

    private RealmReader mRealmReader;

    private EventDetailViewModel mEventDetailViewModel;

    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealmReader = new RealmReader();
        ActivityEventDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_event_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent data = getIntent();
        event = mRealmReader.getEvent(data.getStringExtra("ID"));
        Log.d(TAG, event.toString());
        binding.setEvent(new EventDetailViewModel(event, this));
        mEventDetailViewModel = binding.getEvent();
        mEventDetailViewModel.setLocation(mLocation);
        setupMap();
        setUpLocationTracker();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            // TODO implement sharing event
//            case R.id.action_share:
//                shareEvent();
//                break;
        }
        return true;
    }

    public void shareEvent() {
        //TODO: implement sharing event


    }

    private void setupMap() {
        LocationSnippetFragment mapFragment = (LocationSnippetFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.setCoordinate(new LatLng(event.getLat(), event.getLong()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRealmReader != null) {
            mRealmReader.close();
        }
        if (lastKnownLocationSubscription != null && !lastKnownLocationSubscription.isUnsubscribed()) {
            lastKnownLocationSubscription.unsubscribe();
        }

        if (updatableLocationSubscription != null && !updatableLocationSubscription.isUnsubscribed()) {
            updatableLocationSubscription.unsubscribe();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mEventDetailViewModel != null) {
            Realm realm = Realm.getDefaultInstance();
            final long newId = ReminderUtil.getNewEventId(this);
            if (mEventDetailViewModel.getPrevEventName().equals(ReminderUtil.getEventNameById(newId, this)) && realm.where(ReminderInfo.class).equalTo("calendarEventId", newId).findFirst() == null) {
                Log.d(TAG, "OnResume same event");
                mEventDetailViewModel.setPrevEventName("");
                ReminderInfo info = new ReminderInfo(event.getTitle(), newId);
                realm.beginTransaction();
                realm.copyToRealm(info);
                realm.commitTransaction();
            } else {
                Log.d(TAG, "OnResume different event");
            }
            realm.close();
        }
    }

    public void setUpLocationTracker() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);

        } else {
            if (isGooglePlayServicesAvailable()) {
                LocationRequest request = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(60 * 1000)
                        .setFastestInterval(1000);

                ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(this);


                lastKnownLocationSubscription = locationProvider.getLastKnownLocation()
                        .subscribe(new Action1<Location>() {
                            @Override
                            public void call(Location location) {
                                updateDistance(location);

                            }
                        });

                updatableLocationSubscription = locationProvider.getUpdatedLocation(request)
                        .subscribe(new Action1<Location>() {
                            @Override
                            public void call(Location location) {
                                updateDistance(location);
                            }
                        });
            }
        }
    }

    private void updateDistance(Location location) {
        mLocation = location;
        if (mEventDetailViewModel != null) {
            mEventDetailViewModel.setLocation(mLocation);
        }
    }

    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, GOOGLE_PLAY_SERVICES_REQUEST).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setUpLocationTracker();


                } else {
                    Toast.makeText(this, "Cannot display distance without permission", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_PLAY_SERVICES_REQUEST) {
            Log.d(TAG, "resultCode: " + resultCode);
            setUpLocationTracker();
        }
    }
}
