package com.example.myapplication.Models;

import android.location.Location;

import com.example.myapplication.Interfaces.DataGps;
import com.example.myapplication.MainActivity;

public class ModelGps implements Model {

    MainActivity myApp;
    public DataGps dataGps;

    public ModelGps(){

        dataGps = new DataGps();
    }

    public ModelGps(DataGps gps){

        dataGps = gps;
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
}
