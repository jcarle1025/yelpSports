package com.example.jcarle1025.finalproject;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * uses google maps to display arraylist of business
 * objects that was passed into intent and show their
 * position relative to the stadium that was searched
 */
public class TripMapActivity extends FragmentActivity implements View.OnClickListener {
    TextView title;
    GoogleMap map;
    ArrayList<Restaurant> foods;
    float latitude, longitude;
    String stadium;
    Venue venue;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_map);
        Intent parent = getIntent();
        venue = parent.getParcelableExtra("venue");
        latitude = venue.getLat();
        longitude = venue.getLon();
        stadium = venue.getStadium();
        foods = parent.getParcelableArrayListExtra("f");
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        title = (TextView) findViewById(R.id.titleBar);
        title.setText(venue.getStadium().toUpperCase());

        map = ((SupportMapFragment)  getSupportFragmentManager()
                .findFragmentById(R.id.map))
                .getMap();
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Add a marker in stadium and move the camera.
        LatLng stadiumSpot = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLng(stadiumSpot));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(stadiumSpot, 13), 3000, null);
        map.addMarker(new MarkerOptions().position(stadiumSpot)
                .title(stadium.toUpperCase())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        postFoods(foods);
    }

    public void postFoods(ArrayList<Restaurant> list){
        LatLng spot;
        for(Restaurant r : list){
            spot = new LatLng(r.getLatitude(), r.getLongitude());
            map.addMarker(new MarkerOptions().position(spot).title(r.getName()).snippet(r.getPhone())
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}