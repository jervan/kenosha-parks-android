package edu.uwp.appfactory.eventsmanagement.RealmControllers;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.uwp.appfactory.eventsmanagement.model.Realm.Event;
import io.realm.Realm;

/**
 * RealmReader holds an instance of realm and allows for all realm queries to be stored in one location.
 *
 * every instance of RealmReader must be closed to prevent memory leaks!!!
 *
 * follows same principle as here with realm instance
 * https://realm.io/docs/java/latest/#controlling-the-lifecycle-of-realm-instances
 *
 * Created by Jeremiah on 6/11/17.
 */

public class RealmReader {

    private static final Date todayStart = new Date();
    static {
        todayStart.setHours(0);
        todayStart.setMinutes(0);
        todayStart.setSeconds(0);
    }

    private static final Date todayEnd = new Date();
    static {
        todayEnd.setHours(23);
        todayEnd.setMinutes(59);
        todayEnd.setSeconds(59);
    }

    private static final Date monthEnd = new Date();
    static {
        monthEnd.setDate(1);
        monthEnd.setMonth(monthEnd.getMonth() + 1);
        monthEnd.setHours(0);
        monthEnd.setMinutes(0);
        monthEnd.setSeconds(-1);
    }

    private Realm realm;

    public RealmReader() {
        realm = Realm.getDefaultInstance();
    }

    public List<Event> getEvents() {
        if (realm == null) {
            return new ArrayList<>();
        }

        return realm.where(Event.class)
                .greaterThanOrEqualTo("endDate", todayStart.getTime())
                .findAll().sort("startDate");
    }

    public Event getEvent(String id) {
        if (realm == null) {
            return new Event();
        }
        return realm.where(Event.class).equalTo("id", id).findFirst();
    }

    public int getNumberOfEventsToday() {
        if (realm == null) {
            return 0;
        }
        return realm.where(Event.class)
                .greaterThanOrEqualTo("endDate", todayStart.getTime())
                .lessThanOrEqualTo("startDate", todayEnd.getTime())
                .findAll().size();
    }

    public int getNumberOfEventsMonth() {
        if (realm == null) {
            return 0;
        }
        return realm.where(Event.class)
                .greaterThanOrEqualTo("endDate", todayStart.getTime())
                .lessThanOrEqualTo("startDate", monthEnd.getTime())
                .findAll().size();
    }

    public int getNumberOfEventsTotal() {
        if (realm == null) {
            return 0;
        }
        return realm.where(Event.class)
                .greaterThanOrEqualTo("endDate", todayStart.getTime())
                .findAll().size();
    }

    public List<Event> getEventsContaining(String text) {
        if (realm == null) {
            return new ArrayList<>();
        }
        return realm.where(Event.class).contains("organization", text).findAll().sort("startDate");
    }

    public void close() {
        if (realm != null) {
            realm.close();
        }
        realm = null;
    }
}
