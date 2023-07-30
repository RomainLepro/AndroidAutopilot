package com.example.myapplication.Models;


import android.content.SharedPreferences;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.myapplication.ContextProvider;
import com.example.myapplication.Interfaces.DataGps;
import com.example.myapplication.Interfaces.DataInterface;
import com.example.myapplication.Interfaces.DataLinkerSelector;
import com.example.myapplication.Interfaces.DataLogger;
import com.example.myapplication.Interfaces.DataMacroData;
import com.example.myapplication.Interfaces.DataPid;
import com.example.myapplication.Interfaces.DataRadio;
import com.example.myapplication.Interfaces.DataSensors;
import com.example.myapplication.Interfaces.DataSms;
import com.example.myapplication.fragments.FragmentGps;
import com.example.myapplication.fragments.FragmentLinker;
import com.example.myapplication.fragments.FragmentLogger;
import com.example.myapplication.fragments.FragmentMacroData;
import com.example.myapplication.fragments.FragmentPID;
import com.example.myapplication.fragments.FragmentSensor;
import com.example.myapplication.fragments.FragmentSms;
import com.example.myapplication.fragments.FragmentWaypoints;

import java.util.ArrayList;
import java.util.List;

public class ModelFactory extends ModelDefault{

    private DataInterface dataGps,dataLinkerSelector,dataLogger,
            dataMacroData,dataPid,dataRadio,dataSensors,dataSms;
    private Fragment m_fragmentLogger,m_fragmentSensor,m_fragmentGps,m_fragmentPID,
            m_fragmentWaypoints,m_fragmentLinker,m_fragmentMacroData,m_fragmentSms;
    private Fragment m_activeFragment,m_previousFragment;

    private Model m_macroData,m_plane, m_gps,m_sms, m_imu, m_comunication;

    public Fragment getFragmentLogger() { return m_fragmentLogger;    }
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
    public Fragment getFragmentSms() {return m_fragmentSms;}

    public ModelPlane getPlane() {
        return (ModelPlane) m_plane;
    }
    public ModelGps getGps() {
        return (ModelGps) m_gps;
    }
    public ModelCommunication getCommunication(){return (ModelCommunication) m_comunication;}


    public Fragment getActiveFragment(){return m_activeFragment;}
    public Fragment getPreviousFragment(){return m_previousFragment;}
    public void setActiveFragment(Fragment desiredFragment)
    {
        m_previousFragment = m_activeFragment;
        m_activeFragment = desiredFragment;
        return;
    }


    public List<Fragment> m_listFragments = new ArrayList<Fragment>();
    public List<Model> m_listModels = new ArrayList<Model>();
    private ContextProvider m_contextProvider = null;
    public ModelFactory(ContextProvider contextProvider){
        m_contextProvider = contextProvider;
        m_listData = new ArrayList<DataInterface>();
    }

    @Override
    public void updateDt(float dt_ms) {

        for (Model model:m_listModels) {
            model.updateDt(dt_ms);
        }
    }

    public void createAllModels(){
        Log.i("createAllModels","createAllModels");

        dataGps = new DataGps();m_listData.add(dataGps);
        dataLinkerSelector = new DataLinkerSelector();m_listData.add(dataLinkerSelector);
        dataLogger = new DataLogger();m_listData.add(dataLogger);
        dataMacroData = new DataMacroData();m_listData.add(dataMacroData);
        dataPid = new DataPid();m_listData.add(dataPid);
        dataRadio = new DataRadio();m_listData.add(dataRadio);
        dataSensors = new DataSensors();m_listData.add(dataSensors);
        dataSms = new DataSms();m_listData.add(dataSms);

        Log.i("dataListSize",Integer.toString(m_listData.size()));

        m_plane = new ModelPlane((DataLinkerSelector)dataLinkerSelector,(DataPid) dataPid,(DataGps) dataGps,
                (DataRadio) dataRadio,(DataSensors) dataSensors);m_listModels.add(m_plane);
        m_gps = new ModelGps(m_contextProvider,(DataGps) dataGps);m_listModels.add(m_gps);
        m_macroData = new ModelMacroData((DataGps) dataGps);m_listModels.add(m_macroData);
        m_sms = new ModelSms(m_contextProvider, (DataGps) dataGps,(DataSms) dataSms);m_listModels.add(m_sms);
        m_imu = new ModelImu(m_contextProvider, (DataSensors) dataSensors);m_listModels.add(m_imu);
        m_comunication = new ModelCommunication((DataRadio)dataRadio,(DataLogger)dataLogger);m_listModels.add(m_comunication);

        m_fragmentLogger = new FragmentLogger();m_listFragments.add(m_fragmentLogger);
        m_fragmentSensor = new FragmentSensor((DataSensors) dataSensors, (DataRadio) dataRadio,(DataGps) dataGps);m_listFragments.add(m_fragmentSensor);
        m_fragmentGps = new FragmentGps((DataGps) dataGps, (ModelGps) m_gps);m_listFragments.add(m_fragmentGps);
        m_fragmentPID = new FragmentPID((DataPid) dataPid);m_listFragments.add(m_fragmentPID);
        m_fragmentWaypoints = new FragmentWaypoints((DataGps) dataGps);m_listFragments.add(m_fragmentWaypoints);
        m_fragmentLinker = new FragmentLinker((DataLinkerSelector) dataLinkerSelector);m_listFragments.add(m_fragmentLinker);
        m_fragmentMacroData = new FragmentMacroData((DataMacroData) dataMacroData);m_listFragments.add(m_fragmentMacroData);
        m_fragmentSms = new FragmentSms((DataSms) dataSms);m_listFragments.add(m_fragmentSms);

        m_activeFragment = m_fragmentLogger;
        m_previousFragment = m_fragmentLogger;
    }

    public void loadSharedPreferences(SharedPreferences sharedPreferences) {
        for(DataInterface data:m_listData){
            data.loadSharedPreferences(sharedPreferences);
        }
    }

}
