package edu.uwp.appfactory.eventsmanagement.home;


import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

import edu.uwp.appfactory.eventsmanagement.R;
import edu.uwp.appfactory.eventsmanagement.databinding.FragmentHomeBinding;
import edu.uwp.appfactory.eventsmanagement.model.Realm.Event;
import edu.uwp.appfactory.eventsmanagement.RealmControllers.RealmReader;
import edu.uwp.appfactory.eventsmanagement.viewmodel.HomeViewModel;

/**
 * Created by dakota on 4/27/17.
 */

public class HomeFragment extends Fragment {

    private static final String TAG = "Home Fragment";

    private EventCardPagerAdapter mAdapter;

    private ViewPager pager;

    private HomeViewModel mHomeViewModel;

    private RealmReader mRealmReader;

    private Location mLocation;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRealmReader = new RealmReader();
        FragmentHomeBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        mHomeViewModel = new HomeViewModel(mRealmReader.getNumberOfEventsToday(), mRealmReader.getNumberOfEventsMonth(), mRealmReader.getNumberOfEventsTotal());
        binding.setHome(mHomeViewModel);
        View v = binding.getRoot();
        pager = (ViewPager) v.findViewById(R.id.home_event_card_view_pager);
        List<Event> events = mRealmReader.getEvents();
        if (events.size() > 5) {
            events = events.subList(0, 5);
        }
        mAdapter = new EventCardPagerAdapter(getChildFragmentManager(), events);
        mAdapter.setLocation(mLocation);
        pager.setAdapter(mAdapter);
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(pager);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void updateEvents() {
        List<Event> events = mRealmReader.getEvents();
        if (events.size() > 5) {
            events = events.subList(0, 5);
        }
        if (isVisible()) {
            mAdapter.setEventList(events);
            mHomeViewModel.setCounts(mRealmReader.getNumberOfEventsToday(), mRealmReader.getNumberOfEventsMonth(), mRealmReader.getNumberOfEventsTotal());
        }
    }

    public void updateDistance(Location location) {
        mLocation = location;
        if (mAdapter != null && isVisible()) {
            mAdapter.setLocation(mLocation);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        pager.setAdapter(null);
        if (mRealmReader != null) {
            mRealmReader.close();
        }
    }
}
