package com.example.myapplication.Interfaces;

import android.provider.ContactsContract;

import java.util.Arrays;
import java.util.List;

public class DataMacroData implements DataInterface {

    @Override
    public void saveData() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void loadData(String dataName) {

    }

    public class DataStruct
    {
        public DataStruct(String name,float value,String unit)
        {
            DataName = name;
            DataValue = value;
            DataUnit = unit;
        }

        public DataStruct(String name,String unit)
        {
            DataName = name;
            DataValue = 0.f;
            DataUnit = unit;
        }
        public String DataName = "Null";
        public float DataValue = 0.f;
        public String DataUnit = "";
    }


    public DataStruct averageSpeed = new DataStruct("averageSpeed","m/s");
    public DataStruct maxSpeed = new DataStruct("maxSpeed","m/s");
    public DataStruct runningTime = new DataStruct("runningTime","s");
    public DataStruct movingTime = new DataStruct("movingTime","s");
    public DataStruct distanceTraveled = new DataStruct("distanceTraveled","m");

    public List<DataStruct> m_dataList = Arrays.asList(averageSpeed,maxSpeed,runningTime,movingTime,distanceTraveled);

}
