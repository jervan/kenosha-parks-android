package edu.uwp.appfactory.eventsmanagement.model.CalendarAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jeremiah on 6/11/17.
 */

public class End {

    @SerializedName("dateTime")
    @Expose
    private String dateTime;
    @SerializedName("timeZone")
    @Expose
    private String timeZone;
    @SerializedName("date")
    @Expose
    private String date;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "End{" +
                "dateTime='" + dateTime + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
