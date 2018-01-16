package edu.uwp.appfactory.eventsmanagement.model.CalendarAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jeremiah on 6/11/17.
 */

public class OriginalStartTime {

    @SerializedName("dateTime")
    @Expose
    private String dateTime;
    @SerializedName("timeZone")
    @Expose
    private String timeZone;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public String toString() {
        return "OriginalStartTime{" +
                "dateTime='" + dateTime + '\'' +
                ", timeZone='" + timeZone + '\'' +
                '}';
    }
}
