package com.example.myapplication.fragments;

import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.myapplication.Interfaces.InterfaceGps;
import com.example.myapplication.R;

import java.util.List;


public class FragmentWaypoints extends Fragment {

    public ListView lv_waypointList;

    View view;

    InterfaceGps m_gpsInterface;

    public FragmentWaypoints() {
        m_gpsInterface = new InterfaceGps();
    }

    public FragmentWaypoints(InterfaceGps gpsInterface) {
        m_gpsInterface = gpsInterface;
    }

    public static FragmentWaypoints newInstance(String param1, String param2) {
        FragmentWaypoints fragment = new FragmentWaypoints();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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