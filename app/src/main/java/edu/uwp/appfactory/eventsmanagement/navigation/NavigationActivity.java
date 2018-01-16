package edu.uwp.appfactory.eventsmanagement.navigation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationRequest;

import edu.uwp.appfactory.eventsmanagement.EventListScreen.EventFilterListener;
import edu.uwp.appfactory.eventsmanagement.EventListScreen.EventListFragment;
import edu.uwp.appfactory.eventsmanagement.R;
import edu.uwp.appfactory.eventsmanagement.ReminderScreen.ReminderFragment;
import edu.uwp.appfactory.eventsmanagement.filter.FilterActivity;
import edu.uwp.appfactory.eventsmanagement.home.HomeFragment;
import edu.uwp.appfactory.eventsmanagement.infoscreen.LinkFragment;
import edu.uwp.appfactory.eventsmanagement.util.Config;
import edu.uwp.appfactory.eventsmanagement.RealmControllers.RealmUpdater;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Subscription;
import rx.functions.Action1;


public class NavigationActivity extends AppCompatActivity implements RealmUpdater.RealmUpdateCallback, EventFilterListener {

    private static final String TAG = "Navigation Activity";

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 111;

    private static final int GOOGLE_PLAY_SERVICES_REQUEST = 222;

    public static final int REQUEST_FILTER = 90;

    private Subscription updatableLocationSubscription;

    private Subscription lastKnownLocationSubscription;

    private Fragment fragment;

    private Location location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        if (savedInstanceState == null) {
            fragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
        setupBottomNavigation();
        setUpLocationTracker();
    }

    private void setupBottomNavigation(){

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.action_home:
                        fragment = new HomeFragment();
                        break;

                    case R.id.action_event:
                        fragment = new EventListFragment();
                        break;

                    case R.id.action_reminder:
                        fragment = new ReminderFragment();
                        break;
                    case R.id.action_link:
                        fragment = new LinkFragment();
                        break;
                    default:
                        fragment = new HomeFragment();
                }
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.fragment_container, fragment)
                        .commit();
                updateDistance(location);
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new RealmUpdater(Config.BASE_URLS, this, this);
    }

    @Override
    public void updateComplete() {
        if (fragment instanceof HomeFragment) {
            ((HomeFragment)fragment).updateEvents();

        } else if (fragment instanceof EventListFragment) {
            ((EventListFragment)fragment).updateEvents();
        }
    }

    public void updateDistance(Location location) {
        this.location = location;
        if (fragment instanceof HomeFragment) {
            ((HomeFragment)fragment).updateDistance(this.location);

        } else if (fragment instanceof  EventListFragment) {
            ((EventListFragment)fragment).updateDistance(this.location);
        }
    }

    public void setUpLocationTracker() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
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

        switch (requestCode) {
            case GOOGLE_PLAY_SERVICES_REQUEST:
                Log.d(TAG, "resultCode: " + resultCode);
                setUpLocationTracker();
                break;

            case REQUEST_FILTER:
                fragment.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lastKnownLocationSubscription != null && !lastKnownLocationSubscription.isUnsubscribed()) {
            lastKnownLocationSubscription.unsubscribe();
        }

        if (updatableLocationSubscription != null && !updatableLocationSubscription.isUnsubscribed()) {
            updatableLocationSubscription.unsubscribe();
        }
    }

    @Override
    public void filterClicked() {
        Intent intent = new Intent(this, FilterActivity.class);
        startActivityForResult(intent, REQUEST_FILTER);
    }
}