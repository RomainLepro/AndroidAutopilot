package com.example.myapplication.Interfaces;


import android.content.SharedPreferences;
import android.util.Log;

public class DataLinker implements DataInterface {
    //WARNNING not using getter or setters
    public String m_name = "DefaultName";
    public int numRows = 10;// number of outputs
    public int numCols = 12;// number of inputs
    public float[][] matrixLinker;

    public float[] inputLinker;

    public float[] outputLinker;

    public DataLinker() {
        inputLinker = new float[numCols];
        outputLinker = new float[numRows];
        matrixLinker = new float[numRows][numCols];
        initialiseMatrix();
    }

    public DataLinker(int numInputs, int numOutputs) {
        this.numRows = numOutputs;
        this.numCols = numInputs;
        inputLinker = new float[numCols];
        outputLinker = new float[numRows];
        matrixLinker = new float[numRows][numCols];
        initialiseMatrix();
    }

    public DataLinker(int numInputs, int numOutputs, String name) {
        this.numRows = numOutputs;
        this.numCols = numInputs;
        m_name = name;
        inputLinker = new float[numCols];
        outputLinker = new float[numRows];
        matrixLinker = new float[numRows][numCols];
        initialiseMatrix();
    }

    public float[][] getMatrixLinker() {
        return matrixLinker;
    }

    public void setMatrixLinker(float[][] matrixLinker) {
        this.matrixLinker = matrixLinker;
    }

    void initialiseMatrix()
    {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if(i==j)
                {
                    matrixLinker[i][j] = 1.f;
                }
                else
                {
                    matrixLinker[i][j] = 0.f;
                }
            }
        }
    }

    public void loadData(String matrixString)
    {
        float[][] linkerMatrix;
        if (matrixString != null) {
            matrixString = matrixString.replace("[[", "[");
            matrixString = matrixString.replace("]]", "]");
            String[] rows = matrixString.split("], \\[");
            String[] columns = {""};
            linkerMatrix = new float[rows.length][];
            for (int i = 0; i < rows.length; i++) {
                rows[i] = rows[i].replace("[","");
                rows[i] = rows[i].replace("]","");
                columns = rows[i].split(",");
                linkerMatrix[i] = new float[columns.length];
                for (int j = 0; j < columns.length; j++) {
                    linkerMatrix[i][j] = Float.parseFloat(columns[j]);
                    Log.i("linkerMatrix",columns[j]);
                }
            }
            if(rows.length == numRows && columns.length == numCols) {
                this.matrixLinker = (linkerMatrix);
            }
        }
    }

    public void updateOutputs()
    {
        // warning cols and rows are inverted
        for (int i = 0; i < numRows; i++) {
            outputLinker[i] = 0;
            for (int j = 0; j < numCols; j++) {
                outputLinker[i] += inputLinker[j] * matrixLinker[i][j];
            }
        }
    }

    @Override
    public void saveData() {
        SharedPreferences s;

    }

    @Override
    public void loadData() {

    }
}
