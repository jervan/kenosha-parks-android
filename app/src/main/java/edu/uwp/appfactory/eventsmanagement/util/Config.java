package edu.uwp.appfactory.eventsmanagement.util;

import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by hanh on 5/2/17.
 */

public class Config {
    public enum VersionType {
        FM,KP,KECC
    }

    public static final VersionType versionType = VersionType.KP;

    public static String YOUTUBE_API_KEY = "AIzaSyBEZG_X9QTWZRUS33_t98EJl23jIInGIlQ";

    //Calendar API Base URL's
    public static final String BASE_URL = "https://kenoshaparks.staging.appfactoryuwp.com/api/";
    public static final String HOBBS_PARK_BASE_URL = BASE_URL + "calendars/2ok3o3k6kugcnoe0milta4kqog@group.calendar.google.com/";
    public static final String ROOSEVELT_PARK_BASE_URL = BASE_URL + "calendars/h5hacgta0g3qte2gr9d508ne18@group.calendar.google.com/";
    public static final String LINCOLN_PARK_BASE_URL = BASE_URL + "calendars/parksalliancecalendar@gmail.com/";
    public static final String IMAGE_BASE_URL = BASE_URL + "drive/files/";

    //Location name
    public static final String HOBBS_PARK = "Hobbs Park";
    public static final String ROOSEVELT_PARK = "Roosevelt Park";
    public static final String LINCOLN_PARK = "Lincoln Park";
    public static final String KENOSHA_PARKS_ALLIANCE = "Kenosha Parks Alliance";

    public static final Map<String, String> BASE_URLS;
    static {
        BASE_URLS = new HashMap<>();
        BASE_URLS.put(LINCOLN_PARK, LINCOLN_PARK_BASE_URL);
        BASE_URLS.put(ROOSEVELT_PARK, ROOSEVELT_PARK_BASE_URL);
        BASE_URLS.put(HOBBS_PARK, HOBBS_PARK_BASE_URL);
    }

    // TODO Add Image URL's
    public static String getDefaultImage(String calendarName) {
        switch (calendarName) {
            case LINCOLN_PARK:
                return "https://m.discgolfscene.com/logos/clubs/3643/lincoln-park-dgc-lpdgc-46ce02039953.jpg";
            case ROOSEVELT_PARK:
                return "http://www.london-baby.com/wp-content/uploads/2013/05/4213.jpg";
            case HOBBS_PARK:
                return "http://www.london-baby.com/wp-content/uploads/2013/05/4213.jpg";
            default:
                return "http://www.london-baby.com/wp-content/uploads/2013/05/4213.jpg";
        }
    }

    public static LatLng getDefaultLatLng(String calendarName) {
        switch (calendarName) {
            case LINCOLN_PARK:
                return new LatLng(42.570816, -87.832325);
            case ROOSEVELT_PARK:
                return new LatLng(42.572135, -87.849168);
            case HOBBS_PARK:
                return new LatLng(42.595038, -87.846603);
            default:
                return new LatLng(0,0);
        }
    }

    public static String getDefaultAddress(String calendarName) {
        switch (calendarName) {
            case LINCOLN_PARK:
                return "6900 18th Ave\nKenosha, WI 53143";
            case ROOSEVELT_PARK:
                return "6801 34th Ave\nKenosha, WI 53142";
            case HOBBS_PARK:
                return "4500 30th Ave\nKenosha, WI 53144";
            default:
                return "Park address unavailable";
        }
    }

}
