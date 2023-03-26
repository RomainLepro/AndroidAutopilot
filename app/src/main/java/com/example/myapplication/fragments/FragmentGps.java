package com.example.myapplication.fragments;

import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.myapplication.Interfaces.InterfaceGps;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.location.Priority;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentGps#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentGps extends Fragment {
    View view;
    Switch sw_locationsupdates,sw_gps;
    TextView tv_lat,tv_lon,tv_accuracy,tv_speed,tv_altitude,tv_sensor,tv_updates,tv_waypointCount,tv_bearing,tv_bearingWp,tv_updateCount;
    Button btn_showWaypoints,btn_addWaypoint,btn_showMap,btn_showSensor,btn_showArduino;

    MainActivity myApp;

    InterfaceGps m_gpsInterface;

    FragmentWaypoints fragmentWaypoints;

    Float lastBearing = 0.f;

    public FragmentGps() {
        m_gpsInterface = new InterfaceGps();
    }

    public FragmentGps(InterfaceGps gpsInterfaces) {
        m_gpsInterface = gpsInterfaces;
    }

    public static FragmentGps newInstance() {
        FragmentGps fragment = new FragmentGps();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("FragmentGps","onCreate");
        fragmentWaypoints = new FragmentWaypoints();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        myApp = (MainActivity) getActivity();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gps, container, false);

        tv_lat = view.findViewById(R.id.tv_lat);
        tv_lon = view.findViewById(R.id.tv_lon);
        tv_accuracy = view.findViewById(R.id.tv_accuracy);
        tv_speed = view.findViewById(R.id.tv_speed);
        tv_altitude = view.findViewById(R.id.tv_altitude);
        tv_sensor = view.findViewById(R.id.tv_sensor);
        tv_updates = view.findViewById(R.id.tv_updates);
        tv_waypointCount = view.findViewById(R.id.tv_waypointCount);
        tv_bearing = view.findViewById(R.id.tv_bearing);
        tv_bearingWp = view.findViewById(R.id.tv_bearingWp);
        tv_updateCount = view.findViewById(R.id.tv_updateCount);

        btn_addWaypoint     = view.findViewById(R.id.btn_addWaypoint);
        btn_addWaypoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the gps and add it to a global list
                myApp.saveCurrentLocation();
                updateView(m_gpsInterface.savedLocations,m_gpsInterface.currentLocation,0);
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

        btn_showWaypoints   = view.findViewById(R.id.btn_showWaypoints);
        btn_showWaypoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayout, fragmentWaypoints);
                ft.commit();
            }
        });


        //INITIALIZE
        tv_updates.setText("ON");
        myApp.startLocationUpdate();
        myApp.locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        tv_sensor.setText("Using high accuracy");

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
        float wpO = 0.f;
        if(m_gpsInterface.savedLocations.size()>0)
        {
            wpO = location.bearingTo(m_gpsInterface.savedLocations.get(m_gpsInterface.savedLocations.size()-1));
        }


        tv_lat.setText(String.valueOf(location.getLatitude()));
        tv_lon.setText(String.valueOf(location.getLongitude()));
        tv_accuracy.setText(String.valueOf(location.getAccuracy()));
        tv_bearingWp.setText(String.valueOf(wpO)+" delta : "+String.valueOf(wpO-lastBearing));
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
            lastBearing = location.getBearing();
            tv_bearing.setText(String.valueOf(location.getBearing()));
        }
        else
        {
            tv_bearing.setText("bearing not working");
        }


        tv_waypointCount.setText(String.valueOf(savedLocations.size()));
    }
}