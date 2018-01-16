//TODO - Force calendar sync after delete

package edu.uwp.appfactory.eventsmanagement.util;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import edu.uwp.appfactory.eventsmanagement.model.Realm.ReminderInfo;
import io.realm.Realm;

/**
 * Created by dakota on 5/16/17.
 */

public class ReminderUtil {

    public static final int REQUEST_READ_CALENDAR = 55;

    @Nullable
    public static String getEventNameById(Long id, Activity context) {
        try {
            Uri eventUri = Uri.parse("content://com.android.calendar/events");
            final String EVENTS_WHERE = CalendarContract.Events._ID + "=?";
            String[] args = new String[]{Long.toString(id)};
            String projection[] = {"title"};
            Cursor cursor = context.getContentResolver().query(eventUri, projection, EVENTS_WHERE, args,
                    null);

            String eventName = null;

            if (cursor.moveToFirst()) {
                int nameCol = cursor.getColumnIndex(projection[0]);
                do {
                    eventName = cursor.getString(nameCol);
                } while (cursor.moveToNext());
                cursor.close();
            }
            return eventName == null ? null : eventName;
        } catch (SecurityException ex) {
            promptForCalendarPermissions(context);
            return "";
        }
    }

    public static long[] getEventTimesOnCalendar(Long id, Activity context) {
        try {
            Uri eventUri = Uri.parse("content://com.android.calendar/events");
            final String EVENTS_WHERE = CalendarContract.Events._ID + "=?";
            String[] args = new String[]{Long.toString(id)};
            String projection[] = {"dtstart", "dtend"};
            Cursor cursor = context.getContentResolver().query(eventUri, projection, EVENTS_WHERE, args,
                    null);

            long[] times = new long[2];

            if (cursor.moveToFirst()) {
                int startCol = cursor.getColumnIndex(projection[0]);
                int endCol = cursor.getColumnIndex(projection[1]);
                do {
                    times[0] = cursor.getLong(startCol);
                    times[1] = cursor.getLong(endCol);

                } while (cursor.moveToNext());
                cursor.close();
            }
            return times;
        } catch (SecurityException ex) {
            promptForCalendarPermissions(context);
            return new long[]{-1, -1};
        }
    }

    public static long getNewEventId(Activity context) {
        ContentResolver cr = context.getContentResolver();
        try {
            Uri calUri = Uri.parse("content://com.android.calendar/events");
            Cursor cursor = cr.query(calUri, new String[]{"MAX(_id) as max_id"}, null, null, "_id");
            cursor.moveToFirst();
            Log.d("CRSR", DatabaseUtils.dumpCursorToString(cursor));
            return cursor.getLong(cursor.getColumnIndex("max_id"));
        } catch (SecurityException ex) {
            promptForCalendarPermissions(context);
            return -1;
        }
    }

    public static List<ReminderInfo> verifyReminderSet(List<ReminderInfo> reminders, Activity context) {
        Realm realm = Realm.getDefaultInstance();
        for (ReminderInfo reminder : reminders) {
            String eventName = getEventNameById(reminder.getCalendarEventId(), context);
            if (eventName == null) {
                realm.beginTransaction();
                realm.where(ReminderInfo.class).equalTo("calendarEventId", reminder.getCalendarEventId()).findFirst().deleteFromRealm();
                realm.commitTransaction();
            }
        }
        realm.close();
        return reminders;
    }

    public static void promptForCalendarPermissions(Activity activityContext) {
        if (ContextCompat.checkSelfPermission(activityContext,
                Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activityContext,
                    new String[]{Manifest.permission.READ_CALENDAR}, REQUEST_READ_CALENDAR);
        }
    }

    public static int getFirstReminderForEvent(long eventId, Activity context) {
        ContentResolver cr = context.getContentResolver();
        try {
            String[] args = new String[]{Long.toString(eventId)};
            String REMINDERS_WHERE = CalendarContract.Reminders.EVENT_ID + "=?";
            Cursor cursor = cr.query(
                    CalendarContract.Reminders.CONTENT_URI, null, REMINDERS_WHERE, args, null
            );

            if (cursor == null) {
                return -1;
            }
            int[] minutes = new int[cursor.getCount()];
            int index = 0;

            while (cursor.moveToNext()) {
                Log.d("CRSR",
                        "Id: " + cursor.getInt(cursor.getColumnIndex(CalendarContract.Reminders._ID)) +
                                ", Minutes: " + cursor.getInt(cursor.getColumnIndex(CalendarContract.Reminders.MINUTES)) +
                                ", Method: " + cursor.getInt(cursor.getColumnIndex(CalendarContract.Reminders.METHOD))
                );
                minutes[index] = cursor.getInt(cursor.getColumnIndex(CalendarContract.Reminders.MINUTES));
                index++;
            }
            Arrays.sort(minutes);
            return minutes.length > 0 ? minutes[minutes.length - 1] : -1;
        } catch (SecurityException ex) {
            promptForCalendarPermissions(context);
            return -1;
        }
    }

}
