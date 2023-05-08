package com.example.myapplication.Interfaces;

public class DataSensors implements DataInterface {

    public float Gx=0;
    public float Gy=0;
    public float Gz=0;

    public final float[] accelerometerReading = new float[3];
    public final float[] magnetometerReading = new float[3];
    public final float[] rotationMatrix = new float[9];
    public final float[] orientationAngles = new float[3];
}
