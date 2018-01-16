package edu.uwp.appfactory.eventsmanagement.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import edu.uwp.appfactory.eventsmanagement.BR;
import edu.uwp.appfactory.eventsmanagement.EventDetailActivity;
import edu.uwp.appfactory.eventsmanagement.R;
import edu.uwp.appfactory.eventsmanagement.model.Realm.Event;

/**
 * Created by dakota on 3/26/17.
 *
 * https://nullpointer.wtf/android/mvvm-architecture-data-binding-library/
 */

public class EventItemViewModel extends BaseObservable {

    private Context mContext;

    private Event mEvent;

    private Location mLocation;

    public EventItemViewModel(Context context, Event event) {
        mContext = context;
        mEvent = event;
    }

    public void setLocation(Location location) {
        mLocation = location;
        notifyPropertyChanged(BR.distance);
        notifyPropertyChanged(BR.showDistance);
    }

    public String getName() {
        return mEvent.getTitle();
    }

    public String getLocationName() { return mEvent.getLocationName(); }

    public String getPrice() {
        return mEvent.getPrice();
    }

    public String getDescription() {

        return mEvent.getDescription();
    }

    public String getStartDate() {
        SimpleDateFormat df = new SimpleDateFormat("MMM d", Locale.getDefault());
        return df.format(mEvent.getStartDate());
    }

    public String getAgeGroup() {
        return mEvent.getAgeGroup();
    }

    public String getIsIndoors() {
        return mEvent.getIsIndoor() ? "Indoors" : "Outdoors";
    }

    public String getSetting() {
        return mEvent.getSetting();
    }

    public String getStartTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String startTime = timeFormat.format(mEvent.getStartDate());
        if (startTime.equals("12:00 AM")) {
            return "ALL DAY";
        }
        return startTime;
    }

    public String getEndTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String endTime = timeFormat.format(mEvent.getEndDate());
        if (endTime.equals("11:59 PM")) {
            return "ALL DAY";
        }
        return endTime;
    }

    @Bindable
    public int getShowDistance() { return mLocation != null ? View.VISIBLE : View.GONE; }

    @Bindable
    public String getDistance() {
        if (mLocation != null) {
            Location eventLocation = new Location("event");
            eventLocation.setLatitude(mEvent.getLat());
            eventLocation.setLongitude(mEvent.getLon());
            Float distance = mLocation.distanceTo(eventLocation)/(float) 1609.344;
            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.CEILING);
            return df.format(distance) + " mi";
        } else {
            return "";
        }
    }

    public void clickEvent(EventItemViewModel eventItemViewModel) {
        Event event = eventItemViewModel.mEvent;
        Intent intent = new Intent(mContext, EventDetailActivity.class);
        intent.putExtra("ID", event.getId());
        mContext.startActivity(intent);
    }

    public String getImage() {
        return mEvent.getImage();
    }

//    public HashMap<String, String> getSchedule() {
//        HashMap<String, String> schedule = new HashMap<>();
//        schedule.put("12:00pm", "Lunch");
//        schedule.put("1:00pm", "Go Shopping");
//        schedule.put("2:00pm", "Musical event");
//        schedule.put("3:00pm", "Group meeting");
//        return schedule;
//    }

    @BindingAdapter("image_icon")
    public static void loadImage(final ImageView imageView, String imageUrl) {
        final Context context = imageView.getContext();
        Glide.with(context)
                .load(imageUrl)
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });


    }

}
