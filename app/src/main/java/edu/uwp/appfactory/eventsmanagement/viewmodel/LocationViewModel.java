package edu.uwp.appfactory.eventsmanagement.viewmodel;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.view.View;

/**
 * Created by hanh on 3/27/17.
 *
 * https://nullpointer.wtf/android/mvvm-architecture-data-binding-library/
 */

public class LocationViewModel {

     private String address;
     private String name;
     private double lat;
     private double lon;

     public LocationViewModel(String name, String address, double lat, double lon){
          this.name = name;
          this.address = address;
          this.lat = lat;
          this.lon = lon;
     }

     public String getName() { return name; }

     public String getAddress() {
          return address;
     }

     public double getLat() {
          return lat;
     }

     public double getLon() {
          return lon;
     }

     
     /* This function open map and show the direction to the destination */

     public void showDirection(View v) {
          Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat+","+ lon);
          Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
          mapIntent.setPackage("com.google.android.apps.maps");
          v.getContext().startActivity(mapIntent);
     }

}
