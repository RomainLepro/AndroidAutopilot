package com.example.myapplication.palne;


import android.util.Log;

public class PidInterface {



    public float[][] outputPids;

    public float[] resultsPids;

    public String[] pidNameList =  {"PIDX","PIDY","PIDZ" } ;

    public int pidCount = 3;


    float default_PIDX = 1.f,default_PIDY = 0.1f,default_PIDZ = 0.1f;

    public PidInterface() {
        pidCount = pidNameList.length;
        outputPids = new float[pidCount][3];
        resultsPids = new float[pidCount];

        // Required empty public constructor
        for(int i = 0; i<pidCount;i++)
        {
            outputPids[i][0] = default_PIDX;
            outputPids[i][1] = default_PIDX;
            outputPids[i][2] = default_PIDX;

            resultsPids[i] = 0;
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
                    Log.i("linkerMatrix",columns[j]);
                }
            }
            this.outputPids = (linkerMatrix);
        }
    }

}
