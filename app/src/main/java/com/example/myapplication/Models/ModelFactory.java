package com.example.myapplication.Models;


import androidx.fragment.app.Fragment;

import com.example.myapplication.fragments.FragmentGps;
import com.example.myapplication.fragments.FragmentLinker;
import com.example.myapplication.fragments.FragmentLogger;
import com.example.myapplication.fragments.FragmentMacroData;
import com.example.myapplication.fragments.FragmentPID;
import com.example.myapplication.fragments.FragmentSensor;
import com.example.myapplication.fragments.FragmentWaypoints;

public class ModelFactory {

    private Fragment m_fragmentLogger;
    private Fragment m_fragmentSensor;
    private Fragment m_fragmentGps;
    private Fragment m_fragmentPID;
    private Fragment m_fragmentWaypoints;
    private Fragment m_fragmentLinker;

    public Fragment getFragmentLogger() {
        return m_fragmentLogger;
    }

    public Fragment getFragmentSensor() {
        return m_fragmentSensor;
    }

    public Fragment getFragmentGps() {
        return m_fragmentGps;
    }

    public Fragment getFragmentPID() {
        return m_fragmentPID;
    }

    public Fragment getFragmentWaypoints() {
        return m_fragmentWaypoints;
    }

    public Fragment getFragmentLinker() {
        return m_fragmentLinker;
    }

    public Fragment getFragmentMacroData() {
        return m_fragmentMacroData;
    }

    public ModelPlane getPlane() {
        return m_plane;
    }

    public ModelGps getGps() {
        return m_gps;
    }

    private Fragment m_fragmentMacroData;
    private ModelPlane m_plane;
    private ModelGps m_gps;
    public enum modelType{
        e_modelPlane,
        e_modelGps
    }

    public Model createModel(modelType type){
        switch (type){
            case e_modelPlane:
                return new ModelPlane();
            case e_modelGps:
                return new ModelGps();
        };
        return null;
    };

    public void createAllModels(){

        m_plane = new ModelPlane();
        m_gps = new ModelGps(m_plane.m_interfaceGps);

        m_fragmentLogger = new FragmentLogger();
        m_fragmentSensor = new FragmentSensor(m_plane.sensorsInterface, m_plane.radioInterface);
        m_fragmentGps = new FragmentGps(m_plane.m_interfaceGps);
        m_fragmentPID = new FragmentPID(m_plane.pidInterface);
        m_fragmentWaypoints = new FragmentWaypoints(m_plane.m_interfaceGps);
        m_fragmentLinker = new FragmentLinker(m_plane.m_InterfaceLinkerSelector);
        m_fragmentMacroData = new FragmentMacroData(m_gps.m_interfaceMacroData);
    }
}
