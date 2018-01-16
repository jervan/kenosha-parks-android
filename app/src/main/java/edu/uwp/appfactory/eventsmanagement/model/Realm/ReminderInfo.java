package edu.uwp.appfactory.eventsmanagement.model.Realm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmObject;

/**
 * Created by ChenMingxi on 3/31/17.
 */

public class ReminderInfo extends RealmObject {

    public String eventName;
//    public String eventDay;
//    public String eventMonth;
//    public Date startTime;
//    public Date endTime;
//    public String reminderTime;
    private long calendarEventId;

    public ReminderInfo(){
//        eventName = "SPROUTS Play-groups";
//
//        eventMonth = "Jun";
//        eventDay = "23";
//        time = "2pm-5pm";
//        reminderTime = "1:50pm";
    }

    public ReminderInfo(String name, /*String month, String day,Date startTime, Date endTime, String reminderTime, */long calendarEventId){
        this.eventName = name;
//        this.eventMonth = month;
//        this.eventDay = day;
//        this.startTime = startTime;
//        this.endTime = endTime;
//        this.reminderTime = reminderTime;
        this.calendarEventId = calendarEventId;
    }

    public long getCalendarEventId() {
        return calendarEventId;
    }

}
