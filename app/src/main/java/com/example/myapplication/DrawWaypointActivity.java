package com.example.myapplication;

import android.content.Intent;
import android.icu.text.Transliterator;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Interfaces.DataGps;
import com.example.myapplication.Models.ModelFactory;
import com.example.myapplication.Models.ModelGps;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class DrawWaypointActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener {
    private GoogleMap mMap;
    private MapView mMapView;
    private ModelFactory modelFactory;
    private DataGps dataGps;
    private Button btn_updateWps,btn_clearWps;

    // Create a custom marker icon (e.g., red)
    BitmapDescriptor markerIcon ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_waypoint);

        mMapView = findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        modelFactory = ModelFactory.getInstance(null);
        dataGps = (DataGps)modelFactory.dataGps;

        btn_updateWps  = findViewById(R.id.btn_updateWps);
        btn_updateWps.setOnClickListener(v -> {
            updateAllMarkers();
        });

        btn_clearWps  = findViewById(R.id.btn_clearWps);
        btn_clearWps.setOnClickListener(v -> {
            clearAllMarkers();
        });

        markerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateAllMarkers();
        // Customize the map settings here, if needed.
        mMap.setOnMapClickListener(this);
    }

    public void clearAllMarkers(){
        mMap.clear(); // Clears all markers from the map.
    }

    public void updateAllMarkers(){
        List<Location> waypoints = dataGps.allLocations;
        // Add markers for each waypoint
        for (Location waypoint : waypoints) {
            LatLng latLng = new LatLng(waypoint.getLatitude(), waypoint.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Waypoint"));
        }


        Location waypoint = dataGps.currentLocation;
        LatLng latLng = new LatLng(waypoint.getLatitude(), waypoint.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(markerIcon)  // Use the custom icon here
                .title("Waypoint")
                .snippet("Custom colored marker");
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }



    @Override
    public void onMapClick(LatLng latLng) {
        // Add a marker at the clicked location and display a title.
        mMap.addMarker(new MarkerOptions().position(latLng).title("Waypoint"));
        // TODO put marker in list of dataGps
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



}