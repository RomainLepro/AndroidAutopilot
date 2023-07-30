package com.example.myapplication.Models;

import static java.lang.Double.max;
import static java.lang.Double.min;

import android.util.Log;

import com.example.myapplication.Interfaces.DataGps;
import com.example.myapplication.Interfaces.DataMacroData;

public class ModelMacroData extends ModelDefault {

    public DataMacroData dataMacroData;

    public DataGps dataGps;

    ModelMacroData(DataGps gps,DataMacroData data){
        dataMacroData = data;
        dataGps = gps;
    }

    @Override
    public void updateDt(float dt_ms) {

        dataMacroData.maxSpeed.DataValue = (float) max(dataMacroData.maxSpeed.DataValue,dataGps.speed_ms);

        if(dataGps.speed_ms> 0.5)
        {
            dataMacroData.distanceTraveled.DataValue += dataGps.speed_ms*dt_ms / 1000.0;
            dataMacroData.movingTime.DataValue += dt_ms / 1000.0;
        }
        dataMacroData.runningTime.DataValue += dt_ms / 1000.0;
        dataMacroData.averageSpeed.DataValue = dataMacroData.distanceTraveled.DataValue / (float)max(dataMacroData.movingTime.DataValue,0.1f);

        if(dataMacroData.resetRequest == true){
            dataMacroData.resetRequest = false;
            dataMacroData.reset();
        }

    }
}
