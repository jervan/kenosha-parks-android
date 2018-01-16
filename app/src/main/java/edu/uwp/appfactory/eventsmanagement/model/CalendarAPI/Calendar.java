package edu.uwp.appfactory.eventsmanagement.model.CalendarAPI;

/**
 * Created by dakota on 6/8/17.
 */

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Calendar {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("etag")
    @Expose
    private String etag;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("timeZone")
    @Expose
    private String timeZone;
    @SerializedName("accessRole")
    @Expose
    private String accessRole;
    @SerializedName("defaultReminders")
    @Expose
    private List<Object> defaultReminders = null;
    @SerializedName("nextSyncToken")
    @Expose
    private String nextSyncToken;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getAccessRole() {
        return accessRole;
    }

    public void setAccessRole(String accessRole) {
        this.accessRole = accessRole;
    }

    public List<Object> getDefaultReminders() {
        return defaultReminders;
    }

    public void setDefaultReminders(List<Object> defaultReminders) {
        this.defaultReminders = defaultReminders;
    }

    public String getNextSyncToken() {
        return nextSyncToken;
    }

    public void setNextSyncToken(String nextSyncToken) {
        this.nextSyncToken = nextSyncToken;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Calendar{" +
                "kind='" + kind + '\'' +
                ", etag='" + etag + '\'' +
                ", summary='" + summary + '\'' +
                ", updated='" + updated + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", accessRole='" + accessRole + '\'' +
                ", defaultReminders=" + defaultReminders +
                ", nextSyncToken='" + nextSyncToken + '\'' +
                ", items=" + items +
                '}';
    }
}
