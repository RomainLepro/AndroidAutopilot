package com.example.myapplication.Models;


import androidx.fragment.app.Fragment;

import com.example.myapplication.fragments.FragmentGps;
import com.example.myapplication.fragments.FragmentInterface;
import com.example.myapplication.fragments.FragmentLinker;
import com.example.myapplication.fragments.FragmentLogger;
import com.example.myapplication.fragments.FragmentMacroData;
import com.example.myapplication.fragments.FragmentPID;
import com.example.myapplication.fragments.FragmentSensor;
import com.example.myapplication.fragments.FragmentWaypoints;

public class ModelFactory implements Model{
    private Fragment m_fragmentLogger;
    private Fragment m_fragmentSensor;
    private Fragment m_fragmentGps;
    private Fragment m_fragmentPID;
    private Fragment m_fragmentWaypoints;
    private Fragment m_fragmentLinker;
    private Fragment m_fragmentMacroData;
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

    private ModelMacroData m_macroData;
    private ModelPlane m_plane;
    private ModelGps m_gps;

    @Override
    public void updateDt(int dt_ms) {
        // Here alla model update
        //TODO need ordonnacer here
        m_macroData.updateDt(dt_ms);
        m_plane.updateDt(dt_ms);
        m_gps.updateDt(dt_ms);

    }

    public enum modelType{
        e_modelPlane,
        e_modelGps,
        e_modelMacroData
    }

    public Model createModel(modelType type){
        switch (type){
            case e_modelPlane:
                return new ModelPlane();
            case e_modelGps:
                return new ModelGps();
            case e_modelMacroData:
                return new ModelMacroData();
        };
        return null;
    };

    public void createAllModels(){

        //TODO data and frament must not be associations

        m_plane = new ModelPlane();
        m_gps = new ModelGps(m_plane.dataGps);
        m_macroData = new ModelMacroData(m_gps.dataGps);

        m_fragmentLogger = new FragmentLogger();
        m_fragmentSensor = new FragmentSensor(m_plane.dataSensors, m_plane.dataRadio);
        m_fragmentGps = new FragmentGps(m_plane.dataGps);
        m_fragmentPID = new FragmentPID(m_plane.dataPid);
        m_fragmentWaypoints = new FragmentWaypoints(m_plane.dataGps);
        m_fragmentLinker = new FragmentLinker(m_plane.dataLinkerSelector);
        m_fragmentMacroData = new FragmentMacroData(m_macroData.dataMacroData);
    }


    FragmentInterface getCurrentInterface()
    {
        //TODO implement this and use it in main activity
        return null;
    }


}
