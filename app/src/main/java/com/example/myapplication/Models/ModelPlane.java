package com.example.myapplication.Models;

import static java.lang.Math.max;
import static java.lang.Math.min;

import android.util.Log;

import com.example.myapplication.Interfaces.DataGps;
import com.example.myapplication.Interfaces.DataSensors;
import com.example.myapplication.Interfaces.DataLinker;
import com.example.myapplication.Interfaces.DataLinkerSelector;
import com.example.myapplication.Interfaces.DataPid;
import com.example.myapplication.Interfaces.DataRadio;
import com.example.myapplication.PID.PID;

public class ModelPlane implements Model {
    PID PIDX,PIDY,PIDZ;
    public DataLinkerSelector dataLinkerSelector;
    public DataLinker dataLinker;
    public DataPid dataPid;
    public DataGps dataGps;
    public DataRadio dataRadio;
    public DataSensors dataSensors;

    public ModelPlane()
    {
        int numInputs = 11;
        int numOutputs = 6;

        dataLinkerSelector = new DataLinkerSelector(numInputs,numOutputs);

        dataLinker = dataLinkerSelector.m_linker;

        dataPid = new DataPid();

        dataGps = new DataGps();

        dataRadio = new DataRadio();

        dataSensors = new DataSensors();

        PIDX = new PID(2,0.1f,0.1f);
        PIDY = new PID(2,0.1f,0.1f);
        PIDZ = new PID(2,0.1f,0.1f);
    }

    public int analogToInt(float inputRadio)
    {
        return max(0,min(2,(int)((inputRadio - 500.f)/200.f +1.f)));
    }

    public float[] intToFloatArray(int[] intArray)
    {
        float[] floatArray = new float[intArray.length];
        for (int i = 0 ; i < intArray.length; i++)
        {
            floatArray[i] = (float) intArray[i];
        }
        return floatArray;
    }
    public void updateDt(float dt_ms){
        //Log.i("debug",Float.toString(dataSensors.accelerometerReading[0]));

        PIDX.updateDt(dataSensors.orientationAngles[2]*400, dataRadio.L_val_radio[0],dt_ms);
        PIDY.updateDt(dataSensors.orientationAngles[1]*400, dataRadio.L_val_radio[1],dt_ms);
        PIDZ.updateDt(dataSensors.orientationAngles[0]*400, dataRadio.L_val_radio[2],dt_ms);

        updateLinkerInputsAndOutputs();
    }

    public void updatePidResults() {
        dataPid.resultsPids[0] = PIDX.output;
        dataPid.resultsPids[1] = PIDY.output;
        dataPid.resultsPids[2] = PIDZ.output;
    }

    public int[] getResultsInt() {

        int[] L = dataRadio.L_val_radio_int.clone();
        L[0] = format(dataLinker.outputLinker[0]);
        L[1] = format(dataLinker.outputLinker[1]);
        L[2] = format(dataLinker.outputLinker[2]);
        L[3] = format(dataLinker.outputLinker[3]);
        L[4] = format(dataLinker.outputLinker[4]);
        L[5] = format(dataLinker.outputLinker[5]);
        return L;
    }

    private int format(float output) {
        int maxValue = 500;
        if (output<=-maxValue)return 500-maxValue;
        if(output>=maxValue)return 500+maxValue;
        return (int)output+500;
    }

    public void updatePIDGains() {
        PIDX.updateGains(dataPid.outputPids[0]);
        PIDY.updateGains(dataPid.outputPids[1]);
        PIDZ.updateGains(dataPid.outputPids[2]);
    }

    public void updateLinkerInputsAndOutputs() {
        dataLinkerSelector.requestSelectLinker(analogToInt(dataRadio.L_val_radio[5]));

        dataLinker = dataLinkerSelector.m_linker;

        dataLinker.inputLinker[0] = PIDX.output;
        dataLinker.inputLinker[1] = PIDY.output;
        dataLinker.inputLinker[2] = PIDZ.output;

        dataLinker.inputLinker[3] = dataRadio.L_val_radio[0]-500;
        dataLinker.inputLinker[4] = dataRadio.L_val_radio[1]-500;
        dataLinker.inputLinker[5] = dataRadio.L_val_radio[2]-500;
        dataLinker.inputLinker[6] = dataRadio.L_val_radio[3]-500;
        dataLinker.inputLinker[7] = dataRadio.L_val_radio[4]-500;
        dataLinker.inputLinker[8] = dataRadio.L_val_radio[5]-500;
        dataLinker.inputLinker[9] = analogToInt(dataRadio.L_val_radio[5]);


        dataLinker.inputLinker[10] = dataGps.deltaCourseToNextWaypoint_deg;
        dataLinker.updateOutputs();
    }

}
