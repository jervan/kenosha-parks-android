package edu.uwp.appfactory.eventsmanagement.home.eventCard;

import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uwp.appfactory.eventsmanagement.R;
import edu.uwp.appfactory.eventsmanagement.databinding.ItemEventHomeBinding;
import edu.uwp.appfactory.eventsmanagement.model.Realm.Event;
import edu.uwp.appfactory.eventsmanagement.RealmControllers.RealmReader;
import edu.uwp.appfactory.eventsmanagement.viewmodel.EventItemViewModel;

/**
 * Created by dakota on 4/27/17.
 */

public class FragmentEventCard extends Fragment {

    RealmReader mRealmReader;

    EventItemViewModel mEventItemViewModel;

    ItemEventHomeBinding binding;

    Location mLocation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRealmReader = new RealmReader();
        Event event = mRealmReader.getEvent(getArguments().getString("ID"));
        binding = DataBindingUtil.inflate(inflater, R.layout.item_event_home, container, false);
        mEventItemViewModel = new EventItemViewModel(getContext(), event);
        mEventItemViewModel.setLocation(mLocation);
        binding.setEvent(mEventItemViewModel);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRealmReader != null) {
            mRealmReader.close();
        }
    }


    public void setLocation(Location location) {
        mLocation = location;
        if (mEventItemViewModel != null) {
            mEventItemViewModel.setLocation(location);
        }
    }


}
