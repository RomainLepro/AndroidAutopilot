package com.example.myapplication.Interfaces;


import android.util.Log;

import java.util.Arrays;

public class DataPid extends DataDefault {
    public float[][] outputPids;

    public float[] resultPids;

    public String[] pidNameList =  {"PIDX","PIDY","PIDZ" } ;

    public int pidCount = 3;

    public boolean updatedFromModel = true;


    float default_PIDX = 1.f,default_PIDY = 0.1f,default_PIDZ = 0.1f;


    public DataPid() {
        pidCount = pidNameList.length;
        outputPids = new float[pidCount][3];
        resultPids = new float[pidCount];


        // Required empty public constructor
        for(int i = 0; i<pidCount;i++)
        {
            outputPids[i][0] = default_PIDX;
            outputPids[i][1] = default_PIDY;
            outputPids[i][2] = default_PIDZ;

            resultPids[i] = 0;
        }

    }

    public float[][] getValues()
    {
        return outputPids;
    }

    public void loadData(String matrixString)
    {
        Log.d("PID DATA",matrixString);
        float[][] linkerMatrix;
        if (matrixString != null) {
            matrixString = matrixString.replace("[[", "[");
            matrixString = matrixString.replace("]]", "]");
            String[] rows = matrixString.split("], \\[");
            linkerMatrix = new float[rows.length][];
            for (int i = 0; i < rows.length; i++) {
                rows[i] = rows[i].replace("[","");
                rows[i] = rows[i].replace("]","");
                String[] columns = rows[i].split(",");
                linkerMatrix[i] = new float[columns.length];
                for (int j = 0; j < columns.length; j++) {
                    linkerMatrix[i][j] = Float.parseFloat(columns[j]);
                    Log.i("PID data",columns[j]);
                }
            }
            this.outputPids = (linkerMatrix);
        }
        updatedFromModel = true;
    }

    @Override
    public void saveData() {
        if(sharedPreferences==null)
        {
            Log.w("saveData","saveData impossible");
            return;
        }
        Log.d("PID DATA save", Arrays.deepToString(this.getValues()));
        editor.putString("pidInterfaceString", Arrays.deepToString(this.getValues()));
        editor.apply();
    }

    @Override
    public void reset() {

    }

    @Override
    public void loadData() {
        if(sharedPreferences==null)
        {
            Log.w("loadData","loadData impossible");
            return;
        }
        String pidInterfaceString = sharedPreferences.getString("pidInterfaceString", null);
        loadData(pidInterfaceString);
    }
}
