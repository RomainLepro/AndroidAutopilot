package com.example.myapplication.Interfaces;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class DataGps implements DataInterface {

    public boolean startLocationFollowing = false; //TODO create a switch in GPS to connect it in the gps fragment
    public float latitude_deg = 0;
    public float longitude_deg = 0;
    public float accuracy = 0;
    public float currentCourse_deg = 0;
    public float courseToNextWaypoint_deg = 0;
    public float deltaCourseToNextWaypoint_deg = 0;

    public float deltaCourseToNextWaypointGyro_deg = 0; // same has before but with gyro

    public float waypointDistance_m = 0;
    public float waypointValidationDistance_m = 0;

    public float altitude_m = 0;
    public float baseAltitude_m = 0;

    public float speed_ms = 0;
    public float speedToWaypoint = 0;

    public int nexLocationId = 0;
    public List<Location> savedLocations = new ArrayList<>();
    public Location currentLocation;
    public int locationUpdateCount = 0;

    @Override
    public void saveData() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void loadData(String dataName) {

    }
}
