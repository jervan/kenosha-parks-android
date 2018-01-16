package edu.uwp.appfactory.eventsmanagement.network;

import edu.uwp.appfactory.eventsmanagement.model.CalendarAPI.Calendar;
import edu.uwp.appfactory.eventsmanagement.model.CalendarAPI.Item;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by dakota on 6/8/17.
 */

public interface EventService {

    @GET("events")
    Call<Calendar> getEvents(@Query("timeMin") String dateTime, @Query("singleEvents") Boolean isSingle);

    @GET("events/{eventId}")
    Call<Item> getEvent(@Path("eventId") String eventId);
}
