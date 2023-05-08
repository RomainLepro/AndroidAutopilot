package com.example.myapplication.Models;

import static java.lang.Math.max;
import static java.lang.Math.min;

import com.example.myapplication.Interfaces.InterfaceGps;
import com.example.myapplication.Interfaces.InterfaceSensors;
import com.example.myapplication.Interfaces.InterfaceLinker;
import com.example.myapplication.Interfaces.InterfaceLinkerSelector;
import com.example.myapplication.Interfaces.InterfacePid;
import com.example.myapplication.Interfaces.InterfaceRadio;
import com.example.myapplication.PID.PID;

public class ModelPlane implements Model {
    PID PIDX,PIDY,PIDZ;

    public InterfaceLinkerSelector m_InterfaceLinkerSelector;
    public InterfaceLinker linkerInterface;
    public InterfacePid pidInterface;
    public InterfaceGps m_interfaceGps;
    public InterfaceSensors gyroInterface;
    public InterfaceRadio radioInterface;

    public InterfaceSensors sensorsInterface;


    public float[] L_val_radio;
    public int[] L_val_radio_int;
    public float[]orientationAngles;
    public ModelPlane()
    {
        int numInputs = 11;
        int numOutputs = 6;


        m_InterfaceLinkerSelector = new InterfaceLinkerSelector(numInputs,numOutputs);

        linkerInterface = m_InterfaceLinkerSelector.m_linker;

        pidInterface = new InterfacePid();

        m_interfaceGps = new InterfaceGps();

        gyroInterface = new InterfaceSensors();

        radioInterface = new InterfaceRadio();

        sensorsInterface = new InterfaceSensors();

        PIDX = new PID(2,0.1f,0.1f);
        PIDY = new PID(2,0.1f,0.1f);
        PIDZ = new PID(2,0.1f,0.1f);

        orientationAngles = new float[]{0.f,0.f,0.f};
        L_val_radio = new float[]{0.f,0.f,0.f,0.f};

    }

    public int analogToInt(float inputRadio)
    {
        return max(0,min(2,(int)((inputRadio - 500.f)/200.f +1.f)));
    }
    public void updateDt(int dt_ms){
        PIDX.updateDt(orientationAngles[2]*400,L_val_radio[0],dt_ms);
        PIDY.updateDt(orientationAngles[1]*400,L_val_radio[1],dt_ms);
        PIDZ.updateDt(orientationAngles[0]*400,L_val_radio[2],dt_ms);

        updateLinkerInputsAndOutputs();
    }

    public void updatePidResults() {
        pidInterface.resultsPids[0] = PIDX.output;
        pidInterface.resultsPids[1] = PIDY.output;
        pidInterface.resultsPids[2] = PIDZ.output;
    }

    public int[] getResultsInt() {

        int[] L = L_val_radio_int.clone();
        L[0] = format(linkerInterface.outputLinker[0]);
        L[1] = format(linkerInterface.outputLinker[1]);
        L[2] = format(linkerInterface.outputLinker[2]);
        L[3] = format(linkerInterface.outputLinker[3]);
        L[4] = format(linkerInterface.outputLinker[4]);
        L[5] = format(linkerInterface.outputLinker[5]);
        return L;
    }

    private int format(float output) {
        int maxValue = 500;
        if (output<=-maxValue)return 500-maxValue;
        if(output>=maxValue)return 500+maxValue;
        return (int)output+500;
    }

    public void updatePIDGains() {
        PIDX.updateGains(pidInterface.outputPids[0]);
        PIDY.updateGains(pidInterface.outputPids[1]);
        PIDZ.updateGains(pidInterface.outputPids[2]);
    }

    public void updateLinkerInputsAndOutputs() {
        m_InterfaceLinkerSelector.requestSelectLinker(analogToInt(L_val_radio[5]));

        linkerInterface = m_InterfaceLinkerSelector.m_linker;

        linkerInterface.inputLinker[0] = PIDX.output;
        linkerInterface.inputLinker[1] = PIDY.output;
        linkerInterface.inputLinker[2] = PIDZ.output;

        linkerInterface.inputLinker[3] = L_val_radio[0]-500;
        linkerInterface.inputLinker[4] = L_val_radio[1]-500;
        linkerInterface.inputLinker[5] = L_val_radio[2]-500;
        linkerInterface.inputLinker[6] = L_val_radio[3]-500;
        linkerInterface.inputLinker[7] = L_val_radio[4]-500;
        linkerInterface.inputLinker[8] = L_val_radio[5]-500;
        linkerInterface.inputLinker[9] = analogToInt(L_val_radio[5]);


        linkerInterface.inputLinker[10] = m_interfaceGps.deltaCourseToNextWaypoint_deg;
        linkerInterface.updateOutputs();
    }

}
