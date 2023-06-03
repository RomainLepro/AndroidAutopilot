package com.example.myapplication.Models;

import static java.lang.Double.max;
import static java.lang.Double.min;

import com.example.myapplication.Interfaces.DataGps;
import com.example.myapplication.Interfaces.DataMacroData;

public class ModelMacroData implements Model {

    public DataMacroData dataMacroData;

    public DataGps dataGps;

    ModelMacroData(DataGps gps){
        dataMacroData = new DataMacroData();
        dataGps = gps;
    }

    ModelMacroData(){
        dataMacroData = new DataMacroData();
        dataGps = new DataGps();
    }

    @Override
    public void updateDt(float dt_ms) {

        dataMacroData.maxSpeed.DataValue = (float) max(dataMacroData.maxSpeed.DataValue,dataGps.speed_ms);

        if(dataGps.speed_ms> 0.5)
        {
            dataMacroData.distanceTraveled.DataValue += dataGps.speed_ms*(float)dt_ms / 1000.0;
            dataMacroData.movingTime.DataValue += (float)dt_ms / 1000.0;
        }

        dataMacroData.runningTime.DataValue += (float)dt_ms / 1000.0;
        dataMacroData.averageSpeed.DataValue = dataMacroData.distanceTraveled.DataValue / (float)max(dataMacroData.movingTime.DataValue,0.1f);

    }
}
