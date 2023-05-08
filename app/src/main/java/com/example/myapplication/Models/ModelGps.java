package com.example.myapplication.Models;

import android.location.Location;

import com.example.myapplication.Interfaces.DataGps;
import com.example.myapplication.Interfaces.DataMacroData;
import com.example.myapplication.MainActivity;

public class ModelGps implements Model {

    MainActivity myApp;
    public DataGps m_interfaceGps;

    public ModelGps(){

        m_interfaceGps = new DataGps();
    }

    public ModelGps(DataGps interfaceGps){

        m_interfaceGps = interfaceGps;
    }

    public void updateDt(int dt_ms){
        Location location = m_interfaceGps.currentLocation;
        if(location == null)
        {
            return;
        }

        m_interfaceGps.latitude_deg = (float)location.getLatitude();
        m_interfaceGps.longitude_deg = (float)location.getLongitude();
        m_interfaceGps.accuracy = (float)location.getAccuracy();

        //computation of the corse to the next wp
        float wpO = 0.f;
        if(m_interfaceGps.savedLocations.size()>0)
        {
            wpO = location.bearingTo(m_interfaceGps.savedLocations.get(m_interfaceGps.savedLocations.size()-1));
            m_interfaceGps.courseToNextWaypoint_deg = wpO;
        }

        if(location.hasAltitude())
        {
            m_interfaceGps.altitude_m = (float)location.getAltitude();
        }
        else
        {
            m_interfaceGps.altitude_m = -1f;
        }
        if(location.hasSpeed())
        {
            m_interfaceGps.speed_ms = (float)location.getSpeed();
        }
        else
        {
            m_interfaceGps.speed_ms = 0;
        }
        if(location.hasBearing())
        {
            m_interfaceGps.currentCourse_deg = location.getBearing();
        }
        else
        {
            m_interfaceGps.currentCourse_deg = 0;
        }

        m_interfaceGps.deltaCourseToNextWaypoint_deg = m_interfaceGps.courseToNextWaypoint_deg -m_interfaceGps.currentCourse_deg;
        if(m_interfaceGps.savedLocations.size()==0)
        {
            m_interfaceGps.waypointDistance_m = 0;
            return;
        }

        m_interfaceGps.waypointDistance_m = m_interfaceGps.currentLocation.distanceTo(m_interfaceGps.savedLocations.get(m_interfaceGps.nexLocationId));


        if(!m_interfaceGps.startLocationFollowing)
        {
            return;
        }
        if(m_interfaceGps.waypointDistance_m< m_interfaceGps.waypointValidationDistance_m)
        {
            m_interfaceGps.nexLocationId = m_interfaceGps.nexLocationId+1;
            if(m_interfaceGps.nexLocationId>=m_interfaceGps.savedLocations.size())
            {
                m_interfaceGps.nexLocationId=0;
            }
        }
    }
}
