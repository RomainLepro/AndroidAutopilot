package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Interfaces.InterfaceGps;
import com.example.myapplication.Interfaces.InterfaceMacroData;
import com.example.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMacroData#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMacroData extends Fragment {
    //TODO conect macro data
    InterfaceMacroData m_interfaceMacroData;

    public FragmentMacroData() {
        m_interfaceMacroData = new InterfaceMacroData();
    }

    public FragmentMacroData(InterfaceMacroData interfaceMacroData) {
        m_interfaceMacroData = interfaceMacroData;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_macro_data, container, false);
    }
}