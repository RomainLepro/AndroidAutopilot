package com.example.myapplication.fragments;

import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.myapplication.Interfaces.DataGps;
import com.example.myapplication.R;


public class FragmentWaypoints extends Fragment {

    public ListView lv_waypointList;

    View view;

    DataGps m_gpsInterface;

    public FragmentWaypoints(DataGps gpsInterface) {
        m_gpsInterface = gpsInterface;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_waypoints, container, false);

        lv_waypointList = view.findViewById(R.id.lv_waypointList);
        lv_waypointList.setAdapter(new ArrayAdapter<Location>(getActivity(),
                android.R.layout.simple_list_item_1,m_gpsInterface.savedLocations));

        return view;
    }






}