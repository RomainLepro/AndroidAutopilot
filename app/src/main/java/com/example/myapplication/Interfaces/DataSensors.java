package com.example.myapplication.Interfaces;

public class DataSensors extends DataDefault {

    public final float[] accelerometerReading = new float[3];
    public final float[] magnetometerReading = new float[3];
    public final float[] rotationMatrix = new float[9];
    public final float[] orientationAngles_rad = new float[3];

    @Override
    public void saveData() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void loadData(String dataName) {

    }
}
