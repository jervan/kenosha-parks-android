package edu.uwp.appfactory.eventsmanagement.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import edu.uwp.appfactory.eventsmanagement.model.EventFilter;

/**
 * Created by dakota on 6/16/17.
 */

public class SearchUtil {

    private static final String PREF_SEARCH_QUERY = "searchQuery";
    private static final String PREF_FILTER_START_DATE = "filterStartDate";
    private static final String PREF_FILTER_END_DATE = "filterEndDate";
    private static final String PREF_FILTER_LOCATION = "filterLocation";
    private static final String PREF_FILTER_DISTANCE = "filterDistance";
    private static final String PREF_FILTER_REOCCURRING = "filterReoccurring";

    public static void setFilterCriteria(Context context, EventFilter filter) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_FILTER_LOCATION, filter.getLocation())
                .putLong(PREF_FILTER_START_DATE, filter.getStartDate())
                .putLong(PREF_FILTER_END_DATE, filter.getEndDate())
                .putInt(PREF_FILTER_DISTANCE, filter.getDistance())
                .putBoolean(PREF_FILTER_REOCCURRING, filter.isReoccurring())
                .apply();
    }

    public static EventFilter getFilterCriteria(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return new EventFilter(
                sp.getString(PREF_FILTER_LOCATION, null),
                sp.getLong(PREF_FILTER_START_DATE, -1),
                sp.getLong(PREF_FILTER_END_DATE, -1),
                sp.getInt(PREF_FILTER_DISTANCE, -1),
                sp.getBoolean(PREF_FILTER_REOCCURRING, false));
    }

    public static void setSearchQuery(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SEARCH_QUERY, query)
                .apply();
    }

    @Nullable
    public static String getSearchQuery(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_QUERY, null);
    }
}
