package edu.uwp.appfactory.eventsmanagement.model;

/**
 * Created by dakota on 6/16/17.
 */

public class EventFilter {
    private String location;
    private long startDate;
    private long endDate;
    private int distance;
    private boolean isReoccurring;

    public EventFilter(String organization, long startDate, long endDate, int distance, boolean isReoccurring) {
        this.location = organization;
        this.startDate = startDate;
        this.endDate = endDate;
        this.distance = distance;
        this.isReoccurring = isReoccurring;
    }

    public String getLocation() {
        return location;
    }

    public Long getStartDate() {
        return startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public boolean isReoccurring() {
        return isReoccurring;
    }

    public int getDistance() {
        return distance;
    }

}
