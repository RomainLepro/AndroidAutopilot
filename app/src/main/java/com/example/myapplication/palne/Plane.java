package com.example.myapplication.palne;

import android.content.SharedPreferences;
import android.util.Log;

public class Plane {
    PID PIDX,PIDY,PIDZ;

    public LinkerInterface linkerInterface;

    public float[] L_val_radio;
    public int[] L_val_radio_int;
    public float[]orientationAngles;
    public Plane()
    {
        linkerInterface = new LinkerInterface();
        PIDX = new PID(2,0.1f,0.1f);
        PIDY = new PID(2,0.1f,0.1f);
        PIDZ = new PID(2,0.1f,0.1f);
        orientationAngles = new float[]{0.f,0.f,0.f};
        L_val_radio = new float[]{0.f,0.f,0.f,0.f};

    }
    public void updateDt(int dt_ms){
        PIDX.updateDt(orientationAngles[2]*400,L_val_radio[0],dt_ms);
        PIDY.updateDt(orientationAngles[1]*400,L_val_radio[1],dt_ms);
        PIDZ.updateDt(orientationAngles[0]*400,L_val_radio[2],dt_ms);
    }

    public float[] getResults() {
        float[] L = {0,0,0};
        L[0] = PIDX.output;
        L[1] = PIDY.output;
        L[2] = PIDZ.output;
        return L;
    }

    public int[] getResultsInt() {
        int[] L = L_val_radio_int.clone();
        L[0] = format(PIDX.output);
        L[1] = format(PIDY.output);
        L[2] = format(PIDZ.output);
        L[3] = L_val_radio_int[3];
        L[4] = L_val_radio_int[3];
        return L;
    }

    private int format(float output) {
        int maxValue = 500;
        if (output<=-maxValue)return 500-maxValue;
        if(output>=maxValue)return 500+maxValue;
        return (int)output+500;
    }

    public void updatePIDGains(float[][] values) {
        PIDX.updateGains(values[0]);
        PIDY.updateGains(values[1]);
        PIDZ.updateGains(values[2]);
    }

}
