package edu.uwp.appfactory.eventsmanagement.EventListScreen;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.uwp.appfactory.eventsmanagement.R;
import edu.uwp.appfactory.eventsmanagement.model.Realm.Event;
import edu.uwp.appfactory.eventsmanagement.viewmodel.EventItemViewModel;
import edu.uwp.appfactory.eventsmanagement.databinding.ItemEventBinding;

/**
 * Created by dakota on 3/26/17.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {

    private List<Event> mEventList;

    private Location mLocation;

    private Context mContext;

    public EventListAdapter(List<Event> eventList, Context context) {
        mEventList = eventList;
        mContext = context;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemEventBinding eventBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_event,
                parent,
                false);
        return new EventViewHolder(eventBinding);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        ItemEventBinding postBinding = holder.mEventBinding;
        EventItemViewModel eventItemViewModel = new EventItemViewModel(mContext, mEventList.get(position));
        eventItemViewModel.setLocation(mLocation);
        holder.bind(eventItemViewModel);

    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    public void setEventList(List<Event> events) {
        setEventList(events, true);
    }

    public void setEventList(List<Event> events, boolean notify) {
        this.mEventList = events;
        if (notify) {
            notifyDataSetChanged();
        }
    }

    public void updateDistance(Location location) {
        this.mLocation = location;
        notifyDataSetChanged();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        private ItemEventBinding mEventBinding;

        public EventViewHolder(ItemEventBinding eventBinding) {
            super(eventBinding.getRoot());
            this.mEventBinding = eventBinding;
        }

        public void bind(EventItemViewModel viewModel){
            mEventBinding.setEvent(viewModel);
            mEventBinding.executePendingBindings();
        }
    }
}
