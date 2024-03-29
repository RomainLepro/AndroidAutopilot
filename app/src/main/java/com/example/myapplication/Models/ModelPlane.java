package com.example.myapplication.Models;

import static java.lang.Math.max;
import static java.lang.Math.min;

import com.example.myapplication.Interfaces.DataGps;
import com.example.myapplication.Interfaces.DataSensors;
import com.example.myapplication.Interfaces.DataLinker;
import com.example.myapplication.Interfaces.DataLinkerSelector;
import com.example.myapplication.Interfaces.DataPid;
import com.example.myapplication.Interfaces.DataRadio;
import com.example.myapplication.PID.PID;

public class ModelPlane extends ModelDefault {
    PID PIDX,PIDY,PIDZ;
    public DataLinkerSelector dataLinkerSelector;
    public DataLinker dataLinker;
    public DataPid dataPid;
    public DataGps dataGps;
    public DataRadio dataRadio;
    public DataSensors dataSensors;

    public ModelPlane(DataLinkerSelector dataLinkerSelector,DataPid dataPid,DataGps dataGps,DataRadio dataRadio,DataSensors dataSensors)
    {
        int numInputs = 11;
        int numOutputs = 6;

        this.dataLinkerSelector = dataLinkerSelector;

        dataLinker = dataLinkerSelector.m_linker; // just for convenience

        this.dataPid = dataPid;

        this.dataGps = dataGps;

        this.dataRadio = dataRadio;

        this.dataSensors = dataSensors;

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

        PIDX.updateDt(dataSensors.orientationAngles_rad[2]*400, dataRadio.L_val_radio_int[0],dt_ms);
        PIDY.updateDt(dataSensors.orientationAngles_rad[1]*400, dataRadio.L_val_radio_int[1],dt_ms);
        PIDZ.updateDt(dataSensors.orientationAngles_rad[0]*400, dataRadio.L_val_radio_int[2],dt_ms);

        updateLinkerInputsAndOutputs();
        dataRadio.L_val_servos_int = getResultsInt();
    }

    public void updatePidResults() {
        dataPid.resultPids[0] = PIDX.output;
        dataPid.resultPids[1] = PIDY.output;
        dataPid.resultPids[2] = PIDZ.output;
    }

    public int[] getResultsInt() {

        int[] L = dataRadio.L_val_radio_int.clone();
        // formated data must be between -500 and 500

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
        dataLinkerSelector.requestSelectLinker(dataRadio.L_val_radio_int[5]);

        dataLinker = dataLinkerSelector.m_linker;

        dataLinker.inputLinker[0] = PIDX.output;
        dataLinker.inputLinker[1] = PIDY.output;
        dataLinker.inputLinker[2] = PIDZ.output;

        dataLinker.inputLinker[3] = dataRadio.L_val_radio_int[0]-500;
        dataLinker.inputLinker[4] = dataRadio.L_val_radio_int[1]-500;
        dataLinker.inputLinker[5] = dataRadio.L_val_radio_int[2]-500;
        dataLinker.inputLinker[6] = dataRadio.L_val_radio_int[3]-500;
        dataLinker.inputLinker[7] = dataRadio.L_val_radio_int[4]-500;
        dataLinker.inputLinker[8] = dataRadio.L_val_radio_int[5]-500;
        dataLinker.inputLinker[9] = analogToInt(dataRadio.L_val_radio_int[5]);


        dataLinker.inputLinker[10] = dataGps.deltaCourseToNextWaypoint_deg;
        dataLinker.updateOutputs();
    }

}
