package edu.uwp.appfactory.eventsmanagement.home;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import edu.uwp.appfactory.eventsmanagement.home.eventCard.FragmentEventCard;
import edu.uwp.appfactory.eventsmanagement.model.Realm.Event;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by dakota on 4/27/17.
 */

public class EventCardPagerAdapter extends FragmentStatePagerAdapter {

    private List<Event> mEventList;

    private Location mLocation;


    public EventCardPagerAdapter(FragmentManager fm, List<Event> eventList) {
        super(fm);
        this.mEventList = eventList;
    }

    @Override
    public Fragment getItem(int position) {
        FragmentEventCard fragmentEventCard = new FragmentEventCard();
        Bundle bundle = new Bundle();
        bundle.putString("ID", mEventList.get(position).getId());
        fragmentEventCard.setArguments(bundle);
        fragmentEventCard.setLocation(mLocation);
        return fragmentEventCard;
    }

    @Override
    public int getCount() {
        return mEventList.size();
    }

    public void setEventList(List<Event> eventList) {
        this.mEventList = eventList;
        notifyDataSetChanged();
    }

    public void setLocation(Location location) {
        mLocation = location;
        notifyDataSetChanged();
    }

    // forces view to update when event list changes
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
