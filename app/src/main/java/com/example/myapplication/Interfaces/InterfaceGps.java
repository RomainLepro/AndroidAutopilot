package com.example.myapplication.Interfaces;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class InterfaceGps {

    public float corseToNextWaypoint_deg = 0;
    public float currentCorse_deg = 0;

    public float waypointDistance_m = 0;
    public float waypointValidationDistance_m = 0;

    public float altitude_m = 0;
    public float baseAltitude_m = 0;

    public List<Location> savedLocations = new ArrayList<>();;
    public Location currentLocation;
    public int locationUpdateCount = 0;
}
