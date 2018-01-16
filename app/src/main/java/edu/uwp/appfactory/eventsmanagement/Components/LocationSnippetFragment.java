package edu.uwp.appfactory.eventsmanagement.Components;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by hanh on 3/28/17.
 */

public class LocationSnippetFragment extends SupportMapFragment implements OnMapReadyCallback {

    private LatLng coordinate;
    private final static float ZOOM = 15;

    public void setCoordinate(LatLng coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.addMarker(new MarkerOptions().position(coordinate));
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(coordinate,ZOOM);
        googleMap.moveCamera(center);
        //googleMap.getUiSettings().setZoomGesturesEnabled(false);
        googleMap.getUiSettings().setAllGesturesEnabled(false);



    }
}
