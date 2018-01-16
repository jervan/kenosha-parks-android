package edu.uwp.appfactory.eventsmanagement.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by dakota on 4/17/17.
 */

public class DateUtils {

    public static String formatDateForFilter(Date d) {
        if (d == null) {
            return "No date set";
        } else if (isToday(d)) {
            return "Today";
        } else {
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            return df.format(d);
        }
    }

    private static boolean isToday(Date d) {
        Calendar cal1 = new GregorianCalendar();
        Calendar cal2 = new GregorianCalendar();
        cal1.setTime(d);
        cal2.setTime(new Date());
        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH));
    }

    public static String formatReminderTime(int minutesPrior, Date eventTime) {
        if (minutesPrior == -1) {
            return "No reminder set";
        }
        Calendar c = new GregorianCalendar();
        c.setTime(eventTime);
        int mins = c.get(Calendar.MINUTE) - minutesPrior;
        c.set(Calendar.MINUTE, mins);
        SimpleDateFormat df = new SimpleDateFormat("h:mma M/d/yyyy", Locale.getDefault());
        String f = df.format(c.getTime());
        return "Reminder set for " + df.format(c.getTime());
    }

    public static String formatDateForReminderCard(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String monthName = c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        SimpleDateFormat df = new SimpleDateFormat("d", Locale.getDefault());
        return monthName + "\n" + df.format(date);
    }

    public static String formatDateForReminderRange(Date startTime, Date endTime) {
        SimpleDateFormat df = new SimpleDateFormat("h:mma", Locale.getDefault());
        return df.format(startTime) + " - " + df.format(endTime);
    }


}
