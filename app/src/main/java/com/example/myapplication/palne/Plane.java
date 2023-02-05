package com.example.myapplication.palne;

import android.util.Log;

public class Plane {
    PID PIDX,PIDY,PIDZ;

    public float[] L_val_radio;
    public float[]orientationAngles;
    public Plane()
    {

        PIDX = new PID(2,0.1f,0.1f);
        PIDY = new PID(2,0.1f,0.1f);
        PIDZ = new PID(2,0.1f,0.1f);
        orientationAngles = new float[]{0.f,0.f,0.f};
        L_val_radio = new float[]{0.f,0.f,0.f};
    }
    public void updateDt(int dt_ms){
        PIDX.updateDt(orientationAngles[0]*100,L_val_radio[0],dt_ms);
        PIDY.updateDt(orientationAngles[1]*100,L_val_radio[1],dt_ms);
        PIDZ.updateDt(orientationAngles[2]*100,L_val_radio[2],dt_ms);
    }

    public float[] getResults() {
        float[] L = {0,0,0};
        L[0] = PIDX.output;
        L[1] = PIDY.output;
        L[2] = PIDZ.output;
        return L;
    }

    public int[] getResultsInt() {
        int[] L = {0,0,0};
        L[0] = (int)PIDX.output;
        L[1] = (int)PIDY.output;
        L[2] = (int)PIDZ.output;
        return L;
    }

    public void updatePIDGains(float[][] values) {
        PIDX.updateGains(values[0]);
        PIDY.updateGains(values[1]);
        PIDZ.updateGains(values[2]);

    }
}
