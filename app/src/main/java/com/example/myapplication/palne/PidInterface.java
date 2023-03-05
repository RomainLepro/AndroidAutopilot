package com.example.myapplication.palne;

import com.example.myapplication.listValue.PidValue;

import java.util.ArrayList;

public class PidInterface {

    ArrayList<PidValue> arrayPidValues;

    String[] pidNameList =  {"PIDX","PIDY","PIDZ" } ;
    float[][] outputPids;

    int pidCount;

    public PidInterface() {
        pidCount = pidNameList.length;
        outputPids = new float[pidCount][3];

        // Required empty public constructor
        arrayPidValues = new ArrayList<PidValue>();
        for(int i = 0; i<pidCount;i++)
        {
            arrayPidValues.add(new PidValue(pidNameList[i]));
            outputPids[i]=null;
        }

    }

    public float[][] getValues()
    {
        for(int i =0;i<arrayPidValues.size();i++)
        {
            outputPids[i] =   arrayPidValues.get(i).getPID();
        }
        return outputPids;
    }
}
