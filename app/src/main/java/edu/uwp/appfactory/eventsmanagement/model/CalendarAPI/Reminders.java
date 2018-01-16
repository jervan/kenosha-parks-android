package edu.uwp.appfactory.eventsmanagement.model.CalendarAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jeremiah on 6/11/17.
 */

public class Reminders {

    @SerializedName("useDefault")
    @Expose
    private Boolean useDefault;

    public Boolean getUseDefault() {
        return useDefault;
    }

    public void setUseDefault(Boolean useDefault) {
        this.useDefault = useDefault;
    }

    @Override
    public String toString() {
        return "Reminders{" +
                "useDefault=" + useDefault +
                '}';
    }
}
