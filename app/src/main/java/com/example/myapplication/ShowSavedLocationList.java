package com.example.myapplication;

import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ShowSavedLocationList extends AppCompatActivity {
    ListView lv_waypointList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_location_list);

        MainActivity myApp = (MainActivity) getApplicationContext();
        List<Location> savedLocations = myApp.getSavedLocations();

        lv_waypointList = findViewById(R.id.lv_waypointList);
        lv_waypointList.setAdapter(new ArrayAdapter<Location>(this, android.R.layout.simple_list_item_1,savedLocations));
    }
}