package edu.uwp.appfactory.eventsmanagement.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dakota on 4/27/17.
 *
 * android view model example https://nullpointer.wtf/android/mvvm-architecture-data-binding-library/
 */

public class HomeViewModel extends BaseObservable {

    private int today;

    private int month;

    private int total;

    public HomeViewModel(int today, int month, int total) {
        this.today = today;
        this.month = month;
        this.total = total;
    }

    public void setCounts(int today, int month, int total) {
        this.today = today;
        this.month = month;
        this.total = total;
        notifyChange();
    }

    @Bindable public String getEventsCountPerDay() {
        return Integer.toString(today);
    }

    @Bindable public String getEventsCountPerMonth() {
        return Integer.toString(month);
    }

    @Bindable public String getAllEventsCount() {
        return Integer.toString(total);
    }

    public String getCurrentMonth() {
        SimpleDateFormat df = new SimpleDateFormat("MMMM", Locale.getDefault());
        return df.format(new Date()).toUpperCase();
    }
}
