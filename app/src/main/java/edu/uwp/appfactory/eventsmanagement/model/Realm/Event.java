package edu.uwp.appfactory.eventsmanagement.model.Realm;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.uwp.appfactory.eventsmanagement.model.CalendarAPI.Item;
import edu.uwp.appfactory.eventsmanagement.util.Config;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jeremiah on 6/12/17.
 */

public class Event extends RealmObject {

    private static final String TAG = "Event";

    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String AGE_GROUP = "AGE GROUP";
    private static final String ORGANIZATION = "ORGANIZATION";
    private static final String CONTACT = "CONTACT";
    private static final String REGISTRATION = "REGISTRATION";
    private static final String SCHEDULE = "SCHEDULE";

    private static final List<String> KEYWORDS = new ArrayList<>();
    static {
        KEYWORDS.add(DESCRIPTION);
        KEYWORDS.add(AGE_GROUP);
        KEYWORDS.add(ORGANIZATION);
        KEYWORDS.add(CONTACT);
        KEYWORDS.add(REGISTRATION);
        KEYWORDS.add(SCHEDULE);
    }

    @PrimaryKey
    private String id;

    private String title;

    private String image;

    private String description;

    private boolean isIndoor;

    private String locationName;

    private double lat;

    private double lon;

    private String address;

    private String organization;

    private String contact;

    private String registration;

    private String schedule;

    private String ageGroup;

    private boolean isReoccurring;

    private String recurringEventId;

    private String recurringRRule;

    private Long startDate;

    private Long endDate;

    private RealmList<RealmString> weekDay;

    private String startTime;

    private String endTime;

    private String price;

    private String setting;

    public Event(){
    }

    public Event(Context context, String calendarName, Item item) throws NullPointerException, ParseException {
        Log.d(TAG, item.toString());
        this.id = item.getId();
        this.title = item.getSummary();
        this.ageGroup = "Family";
        this.isIndoor = false;
        setLocation(context, calendarName, item);
        parseDescription(item);
        setDateTimes(item);
        setImage(calendarName, item);
        setRecurring(item);
    }

    private void setLocation(Context context, String calendarName, Item item) {
        if (item.getLocation() != null) {
            if (!item.getLocation().startsWith(calendarName)) {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocationName(item.getLocation(), 1);
                    if (addresses != null && addresses.size() > 0) {
                        this.locationName = addresses.get(0).getAddressLine(0);
                        this.address = addresses.get(0).getAddressLine(1) + "\n" + addresses.get(0).getAddressLine(2);
                        this.lat = addresses.get(0).getLatitude();
                        this.lon = addresses.get(0).getLongitude();
                        return;
                    }
                } catch (IOException | IndexOutOfBoundsException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        this.locationName = calendarName;
        this.address = Config.getDefaultAddress(calendarName);
        this.lat = Config.getDefaultLatLng(calendarName).latitude;
        this.lon = Config.getDefaultLatLng(calendarName).longitude;
    }



    private void setRecurring(Item item) {
        if (item.getRecurringEventId() != null) {
            this.recurringEventId = item.getRecurringEventId();
            this.isReoccurring = true;
        } else {
            this.recurringEventId = null;
            this.isReoccurring = false;
        }
    }

    private void setImage(String calendarName, Item item) {
        if (item.getAttachments() != null && item.getAttachments().get(0) != null) {
            this.image = Config.IMAGE_BASE_URL + item.getAttachments().get(0).getFileId() + "?alt=media";
        } else {
            this.image = Config.getDefaultImage(calendarName);
        }
    }

    private void setDateTimes(Item item) throws ParseException, NullPointerException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
        SimpleDateFormat timezone = new SimpleDateFormat("Z", Locale.getDefault());
        if (item.getStart().getDateTime() != null) {
            this.startDate = dateFormat.parse(item.getStart().getDateTime()).getTime();
        } else {
            this.startDate = dateFormat.parse(item.getStart().getDate() + "T00:00:00" + timezone.format(new Date())).getTime();
        }
        if (item.getEnd().getDateTime() != null) {
            this.endDate = dateFormat.parse(item.getEnd().getDateTime()).getTime();
        } else {
            this.endDate = dateFormat.parse(item.getEnd().getDate() + "T23:59:59" + timezone.format(new Date())).getTime();
        }
    }

    private void parseDescription(Item item) throws ParseException {
        if (item.getDescription() == null) {
            return;
        }
        String itemDescription = item.getDescription().replaceAll(";","").trim();
        if (itemDescription.startsWith(DESCRIPTION)) {
            String[] keywordStrings = itemDescription.split("\n");
            for (int i = keywordStrings.length -1; i >= 0; i--) {
                String[] pairs = keywordStrings[i].split(":");
                String key = pairs[0];
                String value = "";
                if (KEYWORDS.contains(key.replaceAll("\n",""))) {
                    key = key.replaceAll("\n", "");
                    if (pairs.length >= 2) {
                        for (int j = 1; j < pairs.length; j++) {
                            if (j == 1) {
                                value += pairs[j];
                            } else {
                                value += ":" + pairs[j];
                            }
                        }
                        value = value.trim();
                    } else {
                        value = null;
                    }
                    setParsedValues(key, value);
                } else {
                    if (i > 0) {
                        keywordStrings[i - 1] += "\n" + keywordStrings[i];
                    } else {
                        throw new ParseException("Could not parse description", i);
                    }
                }
            }
        } else {
            this.description = itemDescription;
        }
    }

    private void setParsedValues(String key, String value) {
        switch (key) {
            case DESCRIPTION:
                this.description = value;
                break;
            case AGE_GROUP:
                // if they have listed multiple age groups only take the first
                if (value != null) {
                    this.ageGroup = value.split(",")[0];
                }
                break;
            case ORGANIZATION:
                this.organization = value;
                break;
            case CONTACT:
                this.contact = value;
                break;
            case REGISTRATION:
                this.registration = value;
                break;
            case SCHEDULE:
                this.schedule = value;
                break;
        }
    }

    public double getLat() {
        return lat;
    }

    public double getLong() {
        return lon;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsIndoor() {
        return isIndoor;
    }

    public void setIndoor(boolean indoor) {
        isIndoor = indoor;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public boolean isReoccurring() {
        return isReoccurring;
    }

    public void setReoccurring(boolean reoccurring) {
        isReoccurring = reoccurring;
    }

    public String getRecurringEventId() {
        return recurringEventId;
    }

    public void setRecurringEventId(String recurringEventId) {
        this.recurringEventId = recurringEventId;
    }

    public String getRecurringRRule() {
        return recurringRRule;
    }

    public void setRecurringRRule(String recurringRRule) {
        this.recurringRRule = recurringRRule;
    }

    public Date getStartDate() {
        return new Date(startDate == null ? 0: startDate);
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return new Date(endDate == null ? 0: endDate);
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String[] getWeekDay() {
        String[] returnArray = new String[weekDay.size()];
        for(int i = 0; i < returnArray.length; i++) {
            returnArray[i] = weekDay.get(i).getString();
        }
        return returnArray;
    }

    public void setWeekDay(String[] weekDay) {
        RealmList<RealmString> temp = new RealmList<>();
        for(int i = 0; i < weekDay.length; i++) {
            temp.add(new RealmString(weekDay[i]));
        }
        this.weekDay = temp;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", isIndoor=" + isIndoor +
                ", lat=" + lat +
                ", lon=" + lon +
                ", address='" + address + '\'' +
                ", organization='" + organization + '\'' +
                ", contact='" + contact + '\'' +
                ", registration='" + registration + '\'' +
                ", schedule='" + schedule + '\'' +
                ", locationName='" + locationName + '\'' +
                ", ageGroup='" + ageGroup + '\'' +
                ", isReoccurring=" + isReoccurring +
                ", recurringEventId=" + recurringEventId +
                ", startDate=" + new Date(startDate).toString() +
                ", endDate=" + new Date(endDate).toString() +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", price='" + price + '\'' +
                ", setting='" + setting + '\'' +
                '}';
    }
}
