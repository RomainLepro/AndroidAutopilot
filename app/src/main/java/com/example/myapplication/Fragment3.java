package com.example.myapplication;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.location.Priority;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment3 extends Fragment {

    View view;
    Switch sw_locationsupdates,sw_gps;
    TextView tv_lat,tv_lon,tv_accuracy,tv_speed,tv_altitude,tv_sensor,tv_updates,tv_waypointCount,tv_bearing,tv_updateCount;
    Button btn_showWaypoints,btn_addWaypoint,btn_showMap,btn_showSensor,btn_showArduino;

    public Fragment3() {
        // Required empty public constructor
    }

    public static Fragment3 newInstance(String param1, String param2) {
        Fragment3 fragment = new Fragment3();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Fragment3","onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity myApp = (MainActivity) getActivity();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_3, container, false);

        tv_lat = view.findViewById(R.id.tv_lat);
        tv_lon = view.findViewById(R.id.tv_lon);
        tv_accuracy = view.findViewById(R.id.tv_accuracy);
        tv_speed = view.findViewById(R.id.tv_speed);
        tv_altitude = view.findViewById(R.id.tv_altitude);
        tv_sensor = view.findViewById(R.id.tv_sensor);
        tv_updates = view.findViewById(R.id.tv_updates);
        tv_waypointCount = view.findViewById(R.id.tv_waypointCount);
        tv_bearing = view.findViewById(R.id.tv_bearing);
        tv_updateCount = view.findViewById(R.id.tv_updateCount);

        btn_addWaypoint     = view.findViewById(R.id.btn_addWaypoint);
        btn_addWaypoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the gps and add it to a global list
                myApp.saveCurrentLocation();
                updateView(myApp.getSavedLocations(),myApp.getCurrentLocation(),0);
            }
        });

        sw_locationsupdates = view.findViewById(R.id.sw_locationsupdates);
        sw_locationsupdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sw_locationsupdates.isChecked())
                {
                    //updates location
                    tv_updates.setText("ON");
                    myApp.startLocationUpdate();
                }
                else
                {
                    tv_updates.setText("OFF");
                    myApp.stopLocationUpdate();
                }
            }
        });

        sw_gps = view.findViewById(R.id.sw_gps);
        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sw_gps.isChecked())
                {
                    myApp.locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
                    tv_sensor.setText("Using high accuracy");
                }
                else
                {
                    myApp.locationRequest.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY);
                    tv_sensor.setText("Using balance power");
                }
            }
        });

        return view;
    }


    public void updateView(List<Location> savedLocations, Location currentLocation, int updateCount)
    {
        Location location = currentLocation;
        //Log.i("UI","updates UI");
        // UPDATES THE UI LOCATION
        if(location == null)
        {
            //Log.i("UI","no location");
            return;
        }

        tv_lat.setText(String.valueOf(location.getLatitude()));
        tv_lon.setText(String.valueOf(location.getLongitude()));
        tv_accuracy.setText(String.valueOf(location.getAccuracy()));
        tv_updateCount.setText(String.valueOf(updateCount));

        if(location.hasAltitude())
        {
            tv_altitude.setText(String.valueOf(location.getAltitude()));
        }
        else
        {
            tv_altitude.setText("altitude not working");
        }
        if(location.hasSpeed())
        {
            tv_speed.setText(String.valueOf(location.getSpeed()));
        }
        else
        {
            tv_speed.setText("speed not working");
        }
        if(location.hasBearing())
        {
            tv_bearing.setText(String.valueOf(location.getBearing()));
        }
        else
        {
            tv_bearing.setText("bearing not working");
        }


        tv_waypointCount.setText(String.valueOf(savedLocations.size()));
    }
}