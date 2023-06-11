package com.example.myapplication.Models;


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
import com.example.myapplication.fragments.FragmentInterface;
import com.example.myapplication.fragments.FragmentLinker;
import com.example.myapplication.fragments.FragmentLogger;
import com.example.myapplication.fragments.FragmentMacroData;
import com.example.myapplication.fragments.FragmentPID;
import com.example.myapplication.fragments.FragmentSensor;
import com.example.myapplication.fragments.FragmentSms;
import com.example.myapplication.fragments.FragmentWaypoints;

import java.util.ArrayList;
import java.util.List;

public class ModelFactory implements Model{

    private DataInterface dataGps,dataLinkerSelector,dataLogger,
            dataMacroData,dataPid,dataRadio,dataSensors,dataSms;
    private Fragment m_fragmentLogger,m_fragmentSensor,m_fragmentGps,m_fragmentPID,
            m_fragmentWaypoints,m_fragmentLinker,m_fragmentMacroData,m_fragmentSms;

    private Model m_macroData,m_plane, m_gps,m_sms, m_imu;

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

    public List<Fragment> m_listFragments = new ArrayList<Fragment>();
    public List<Model> m_listModels = new ArrayList<Model>();
    public List<DataInterface> m_listData = new ArrayList<DataInterface >();
    private ContextProvider m_contextProvider = null;
    public ModelFactory(ContextProvider contextProvider){
        m_contextProvider = contextProvider;
    }

    @Override
    public void updateDt(float dt_ms) {

        for (Model model:m_listModels) {
            model.updateDt(dt_ms);
        }
    }

    public void createAllModels(){

        dataGps = new DataGps();m_listData.add(dataGps);
        dataLinkerSelector = new DataLinkerSelector();m_listData.add(dataLinkerSelector);
        dataLogger = new DataLogger();m_listData.add(dataLogger);
        dataMacroData = new DataMacroData();m_listData.add(dataMacroData);
        dataPid = new DataPid();m_listData.add(dataPid);
        dataRadio = new DataRadio();m_listData.add(dataRadio);
        dataSensors = new DataSensors();m_listData.add(dataSensors);
        dataSms = new DataSms();m_listData.add(dataSms);

        m_plane = new ModelPlane((DataLinkerSelector)dataLinkerSelector,(DataPid) dataPid,(DataGps) dataGps,
                (DataRadio) dataRadio,(DataSensors) dataSensors);m_listModels.add(m_plane);
        m_gps = new ModelGps(m_contextProvider,(DataGps) dataGps);m_listModels.add(m_gps);
        m_macroData = new ModelMacroData((DataGps) dataGps);m_listModels.add(m_macroData);
        m_sms = new ModelSms(m_contextProvider, (DataGps) dataGps,(DataSms) dataSms);m_listModels.add(m_sms);
        m_imu = new ModelImu(m_contextProvider, (DataSensors) dataSensors);m_listModels.add(m_imu);

        m_fragmentLogger = new FragmentLogger();m_listFragments.add(m_fragmentLogger);
        m_fragmentSensor = new FragmentSensor((DataSensors) dataSensors, (DataRadio) dataRadio);m_listFragments.add(m_fragmentSensor);
        m_fragmentGps = new FragmentGps((DataGps) dataGps, (ModelGps) m_gps);m_listFragments.add(m_fragmentGps);
        m_fragmentPID = new FragmentPID((DataPid) dataPid);m_listFragments.add(m_fragmentPID);
        m_fragmentWaypoints = new FragmentWaypoints((DataGps) dataGps);m_listFragments.add(m_fragmentWaypoints);
        m_fragmentLinker = new FragmentLinker((DataLinkerSelector) dataLinkerSelector);m_listFragments.add(m_fragmentLinker);
        m_fragmentMacroData = new FragmentMacroData((DataMacroData) dataMacroData);m_listFragments.add(m_fragmentMacroData);
        m_fragmentSms = new FragmentSms((DataSms) dataSms);m_listFragments.add(m_fragmentSms);

        loadData();
    }

}
