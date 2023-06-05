package com.example.myapplication.Models;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.myapplication.ContextProvider;
import com.example.myapplication.Interfaces.DataSensors;

public class ModelImu implements Model, SensorEventListener {

    DataSensors m_dataSensors;
    private ContextProvider m_contextProvider = null;
    private SensorManager sensorManager;

    ModelImu(ContextProvider contextProvider,DataSensors dataSensors)
    {
        m_contextProvider = contextProvider;
        m_dataSensors = dataSensors;
        sensorManager = (SensorManager) m_contextProvider.getActivity().getSystemService(Context.SENSOR_SERVICE);
        onResume();

    }

    @Override
    public void updateDt(float dt_ms) {
        updateOrientationAngles();
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
        // You must implement this callback in your code.
    }

    protected void onResume() {

        // Get updates from the accelerometer and magnetometer at a constant rate.
        // To make batch operations more efficient and reduce power consumption,
        // provide support for delaying updates to the application.
        //
        // In this example, the sensor reporting delay is small enough such that
        // the application receives an update before the system checks the sensor
        // readings again.
        android.hardware.Sensor accelerometer = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
        android.hardware.Sensor magneticField = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) {
            sensorManager.registerListener(this, magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    protected void onPause() {
        // Don't receive any more updates from either sensor.
        sensorManager.unregisterListener(this);
    }

    // Get readings from accelerometer and magnetometer. To simplify calculations,
    // consider storing these readings as unit vectors.
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == android.hardware.Sensor.TYPE_ACCELEROMETER) {

            float[] accelerometerReading = m_dataSensors.accelerometerReading;// TODO useless remove ?
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.length);
        } else if (event.sensor.getType() == android.hardware.Sensor.TYPE_MAGNETIC_FIELD) {
            float[] magnetometerReading = m_dataSensors.magnetometerReading; // TODO useless remove ?
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.length);
        }
    }

    // Compute the three orientation angles based on the most recent readings from
    // the device's accelerometer and magnetometer.
    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        float[] orientationAngles = m_dataSensors.orientationAngles;
        float[] rotationMatrix = m_dataSensors.rotationMatrix;
        float[] magnetometerReading = m_dataSensors.magnetometerReading;
        float[] accelerometerReading = m_dataSensors.accelerometerReading;
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading);
        // "rotationMatrix" now has up-to-date information.
        SensorManager.getOrientation(rotationMatrix, orientationAngles);
    }

}
