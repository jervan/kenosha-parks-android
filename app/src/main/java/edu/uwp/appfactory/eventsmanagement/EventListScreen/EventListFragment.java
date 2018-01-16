package edu.uwp.appfactory.eventsmanagement.EventListScreen;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.lang.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.uwp.appfactory.eventsmanagement.R;
import edu.uwp.appfactory.eventsmanagement.model.EventFilter;
import edu.uwp.appfactory.eventsmanagement.model.Realm.Event;
import edu.uwp.appfactory.eventsmanagement.navigation.NavigationActivity;
import edu.uwp.appfactory.eventsmanagement.RealmControllers.RealmReader;
import edu.uwp.appfactory.eventsmanagement.util.ReminderUtil;
import edu.uwp.appfactory.eventsmanagement.util.SearchUtil;
import rx.Observable;
import rx.Subscription;

/**
 * Created by dakota on 3/26/17.
 */
//TODO - Indicate search/filter are active
public class EventListFragment extends Fragment {

    private RecyclerView mEventRecyclerView;

    private EventListAdapter mAdapter;

    private List<Event> mEventList;

    private View rootView;

    private RealmReader mRealmReader;

    private static final int REQUEST_FILTER = NavigationActivity.REQUEST_FILTER;

    private static final String TAG = "EventListFragment";

    private EventFilter currentFilter = null;

    private String currentSearch = null;

    public static final int RESULT_FILTER = 78;

    public static final int RESULT_RESET = 79;

    private boolean pendingFilter = false;

    private SearchView mSearchView;

    private Location mLocation;

    //private DatabaseReference mDatabaseReference;

    private Subscription eventsByName;

    private Subscription eventFilterSubscription;

    private EventFilterListener mEventFilterListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ReminderUtil.promptForCalendarPermissions(getActivity());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.event_list_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //Write to SharedPrefs when user submits
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (searchItem != null) {
                    searchItem.collapseActionView();
                    if (isVisible()) {
                        filterEventsByName(query);
                    }
                }
                SearchUtil.setSearchQuery(getContext(), query);
                Log.d(TAG, "Submit: " + query);
                return false;
            }

            //Filter on each text change
            @Override
            public boolean onQueryTextChange(String newText) {
                if (isVisible()) {
                    filterEventsByName(newText);
                }
                Log.d(TAG, "Change: " + newText);
                return true;
            }
        });

        //Write query to preferences when focus changes
        mSearchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                SearchUtil.setSearchQuery(getContext(), mSearchView.getQuery().toString());
                Log.d(TAG, "TextChanged: " + mSearchView.getQuery());
                if (isVisible()) {
                    filterEventsByName(mSearchView.getQuery().toString());
                }
            }
        });

        //When search is clicked, reload query text from preferences
        mSearchView.setOnSearchClickListener(v -> {
            mSearchView.setQuery(SearchUtil.getSearchQuery(getContext()), false);
            Log.d(TAG, "Search was clicked " + SearchUtil.getSearchQuery(getContext()));
        });

        mSearchView.setIconifiedByDefault(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //TODO: manage this
            case R.id.action_filter:
                mEventFilterListener.filterClicked();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRealmReader = new RealmReader();
        rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
        setupToolbar();

        mEventList = mRealmReader.getEvents();
        mAdapter = new EventListAdapter(mEventList, getActivity());
        executePendingFilter();
        executePendingSearch();
        mAdapter.updateDistance(mLocation);
        mEventRecyclerView = (RecyclerView) rootView.findViewById(R.id.event_list_recycler_view);
        mEventRecyclerView.setAdapter(mAdapter);

//        mDatabaseReference.child("events").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                mEventList = new ArrayList<>();
//                for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                    Log.d("Event", ds.getValue(Event.class).toString());
//                    mEventList.add(ds.getValue(Event.class));
//                    mEventRecyclerView.setAdapter(new EventListAdapter(mEventList, getActivity()));
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        return rootView;
    }

    public void updateEvents() {
        if (!pendingFilter) {
            mEventList = mRealmReader.getEvents();
            if (isVisible()) {
                mAdapter.setEventList(mEventList);
            }
        }
        if (isVisible()) {
            executePendingFilter();
            executePendingSearch();
            mAdapter.notifyDataSetChanged();
        }
    }

    public void updateDistance(Location location) {
        mLocation = location;
        if (mAdapter != null && isVisible()) {
            mAdapter.updateDistance(location);
        }
    }

    public RecyclerView getEventRecyclerView() {
        return mEventRecyclerView;
    }

    public void setData(List<Event> eventList) {
        this.mEventList = eventList;
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRealmReader != null) {
            mRealmReader.close();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_FILTER) {
            switch (resultCode) {
                case RESULT_FILTER:
                    Log.d(TAG, "Filter initiated");
                    long startDate = -1;
                    long endDate = -1;
                    if (data.getLongExtra("START_DATE", 0) != 0) {
                        startDate = data.getLongExtra("START_DATE", 0);
                    }
                    if (data.getLongExtra("END_DATE", 0) != 0) {
                        endDate = data.getLongExtra("END_DATE", 0);
                    }
                    String location = data.getStringExtra("LOCATION");
                    int distance = data.getIntExtra("DISTANCE", -1);
                    boolean isReoccurring = data.getBooleanExtra("REOCCURRING", false);
                    SearchUtil.setFilterCriteria(getContext(), new EventFilter(location, startDate, endDate, distance, isReoccurring));
                    break;
                case RESULT_RESET:
                    SearchUtil.setFilterCriteria(getContext(), new EventFilter(null, -1, -1, -1, false));
                    Log.d(TAG, "Filter reset");
                    break;
            }
        }
    }

    public void filterEvents(EventFilter filterOptions) {
        eventFilterSubscription = Observable.from(mRealmReader.getEvents()).filter(event -> {
            boolean afterStartDate = true;
            boolean beforeEndDate = true;
            boolean isLocation = true;
            boolean withinDistance = true;
            boolean isReoccurring = true;
            if (filterOptions.getStartDate() != -1) {
                long tempEventStartDate = DateUtils.truncate(event.getStartDate(), Calendar.DAY_OF_MONTH).getTime();
                long tempFilterStartDate = DateUtils.truncate(new Date(filterOptions.getStartDate()), Calendar.DAY_OF_MONTH).getTime();
                afterStartDate = tempEventStartDate >= tempFilterStartDate;
            }
            if (filterOptions.getEndDate() != -1) {
                long tempEventEndDate = DateUtils.truncate(event.getEndDate(), Calendar.DAY_OF_MONTH).getTime();
                long tempFilterEndDate = DateUtils.truncate(new Date(filterOptions.getEndDate()), Calendar.DAY_OF_MONTH).getTime();
                beforeEndDate = tempEventEndDate <= tempFilterEndDate;
            }
            if (event.getLocationName() != null && filterOptions.getLocation() != null) {
                isLocation = event.getLocationName().equalsIgnoreCase(filterOptions.getLocation()) || filterOptions.getLocation().equalsIgnoreCase("All");
            }
            if (mLocation != null && filterOptions.getDistance() > -1) {
                int eventDistance = getDistance(event);
                withinDistance = eventDistance <= filterOptions.getDistance();
            }
            if (filterOptions.isReoccurring()) {
                isReoccurring = event.isReoccurring() == filterOptions.isReoccurring();
            }
            return isLocation && afterStartDate && beforeEndDate && withinDistance && isReoccurring;

        }).toList().subscribe(filteredEvents -> {
            mEventList = filteredEvents;
            for (Event e : filteredEvents) {
                Log.d(TAG, e.toString());
            }
            mAdapter.setEventList(mEventList, false);
        });
        Log.d(TAG, "Filtered events");
    }

    public int getDistance(Event event) {
        Location eventLocation = new Location("event");
        eventLocation.setLatitude(event.getLat());
        eventLocation.setLongitude(event.getLon());
        return (int) (mLocation.distanceTo(eventLocation) / (float) 1609.344);
    }

    public void filterEventsByName(String name) {
        if (name != null) {
            eventsByName = rx.Observable.from(mEventList)
                    .filter(event -> event.getTitle().toLowerCase()
                            .contains(name.toLowerCase()))
                    .toList()
                    .subscribe(filteredEvents -> {
                        mAdapter.setEventList(filteredEvents);
                    });
        }
    }

    private void executePendingFilter() {
        EventFilter filter = SearchUtil.getFilterCriteria(getContext());
        Log.d(TAG, "Pending filter " + filter.toString());
        filterEvents(filter);
    }

    private void executePendingSearch() {
        String query = SearchUtil.getSearchQuery(getContext());
        Log.d(TAG, "PendingSearch: " + query);
        filterEventsByName(query);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (eventFilterSubscription != null && !eventFilterSubscription.isUnsubscribed()) {
            eventFilterSubscription.unsubscribe();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EventFilterListener) {
            mEventFilterListener = (EventFilterListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EventFilterListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mEventFilterListener = null;
    }
}
