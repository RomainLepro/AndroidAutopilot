package com.example.myapplication;

import android.content.Context;
import android.hardware.SensorManager;

public class Sensor {

    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];

    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];

    private long prevTime_ms  = 0;
    private static final int dtUpdateUI_ms = 100;


}
