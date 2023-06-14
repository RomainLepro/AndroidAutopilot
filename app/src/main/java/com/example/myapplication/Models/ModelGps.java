package com.example.myapplication.Models;

import static com.example.myapplication.MainActivity.MILLIS;
import static com.example.myapplication.MainActivity.PERMISSION_FINE_LOCATION;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.myapplication.ContextProvider;
import com.example.myapplication.Interfaces.DataGps;
import com.example.myapplication.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

public class ModelGps extends ModelDefault implements  SensorEventListener {

    MainActivity myApp;
    public DataGps dataGps;
    public FusedLocationProviderClient fusedLocationProviderClient;
    public LocationRequest locationRequest;
    private ContextProvider m_contextProvider = null;

    LocationCallback locationCallback;


    public ModelGps(ContextProvider contextProvider,DataGps gps){
        dataGps = gps;
        dataGps.saveData();
        m_contextProvider = contextProvider;
        createLocationRequest();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                updateGps();
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
            }
        };
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(m_contextProvider.getActivity());

        //LOCATION
        createLocationRequest();
        //this event is triggered every time the condition are met (here the timer)
        m_contextProvider.getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_FINE_LOCATION);
        startLocationUpdate();
    }

    public void updateDt(float dt_ms){
        Location location = dataGps.currentLocation;
        if(location == null)
        {
            return;
        }

        dataGps.latitude_deg = (float)location.getLatitude();
        dataGps.longitude_deg = (float)location.getLongitude();
        dataGps.accuracy = (float)location.getAccuracy();

        //computation of the corse to the next wp
        float wpO = 0.f;
        if(dataGps.savedLocations.size()>0)
        {
            wpO = location.bearingTo(dataGps.savedLocations.get(dataGps.savedLocations.size()-1));
            dataGps.courseToNextWaypoint_deg = wpO;
        }

        if(location.hasAltitude())
        {
            dataGps.altitude_m = (float)location.getAltitude();
        }
        else
        {
            dataGps.altitude_m = -1f;
        }
        if(location.hasSpeed())
        {
            dataGps.speed_ms = (float)location.getSpeed();
        }
        else
        {
            dataGps.speed_ms = 0;
        }
        if(location.hasBearing())
        {
            dataGps.currentCourse_deg = location.getBearing();
        }
        else
        {
            dataGps.currentCourse_deg = 0;
        }

        dataGps.deltaCourseToNextWaypoint_deg = dataGps.courseToNextWaypoint_deg - dataGps.currentCourse_deg;
        if(dataGps.savedLocations.size()==0)
        {
            dataGps.waypointDistance_m = 0;
            return;
        }

        dataGps.waypointDistance_m = dataGps.currentLocation.distanceTo(dataGps.savedLocations.get(dataGps.nexLocationId));


        if(!dataGps.startLocationFollowing)
        {
            return;
        }
        if(dataGps.waypointDistance_m< dataGps.waypointValidationDistance_m)
        {
            dataGps.nexLocationId = dataGps.nexLocationId+1;
            if(dataGps.nexLocationId>= dataGps.savedLocations.size())
            {
                dataGps.nexLocationId=0;
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void saveCurrentLocation(){
        dataGps.savedLocations.add(dataGps.currentLocation);
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10*MILLIS);
        locationRequest.setFastestInterval(3*MILLIS);
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
    }

    public void startLocationUpdate() {
        if(ActivityCompat.checkSelfPermission(m_contextProvider.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback,  Looper.getMainLooper());
            updateGps();
            Log.i("update","start location update");
        }
        else{
            Log.i("update","start location update FAILED");
        }
    }

    public void stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        Log.i("update","stop location update");
    }

    private void updateGps()
    {
        //Log.i("UpdateGps","Update GPS");

        if(ActivityCompat.checkSelfPermission(m_contextProvider.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(m_contextProvider.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
        {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(m_contextProvider.getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null) // check that the location isn't null
                    {
                        //Log.i("UpdateGps","not null");
                        dataGps.locationUpdateCount++;
                        dataGps.currentLocation = location;
                    }
                    else{
                        //Log.i("UpdateGps","null");
                    }
                }
            });
        }
        else
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                Log.e("UpdateGps","No permission");
                m_contextProvider.getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_FINE_LOCATION);
            }
        }
    }
}
