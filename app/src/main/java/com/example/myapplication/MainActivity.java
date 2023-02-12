package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.fragments.Fragment1;
import com.example.myapplication.fragments.Fragment2;
import com.example.myapplication.fragments.Fragment3;
import com.example.myapplication.fragments.Fragment4;
import com.example.myapplication.palne.Plane;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AndroidCommunication implements SensorEventListener {

    public static final int MILLIS = 1000;
    private static final int PERMISSION_FINE_LOCATION = 99;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    public LocationRequest locationRequest;
    List<Location> savedLocations = new ArrayList<>();;
    private Location currentLocation;
    private int locationUpdateCount = 0;


    Toolbar toolbar;
    Fragment fragment1,fragment2,fragment3,fragment4;
    FragmentTransaction ft;


    //SENSOR VARIABLES
    Sensor sensor;
    private SensorManager sensorManager;
    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];
    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];


    private static final int dtUpdateUI_ms = 100;
    private static final int dtUpdateSimulation_ms = 5;

    Plane plane;

    Handler handler;

    final Runnable taskUpdateUi = new Runnable() {
        public void run() {
            Runnable activity = this;
            if(activity!=null)
            {
                if(fragment1.isVisible())
                {
                    ((Fragment1) fragment1).updateView(getLogger(),getDebug());
                }
                if(fragment2.isVisible())
                {
                    ((Fragment2) fragment2).updateView(accelerometerReading, orientationAngles,L_val_radio);
                }
                if(fragment3.isVisible())
                {
                    ((Fragment3) fragment3).updateView(savedLocations,currentLocation,locationUpdateCount);
                }
                if(fragment4.isVisible())
                {
                    // transfert PID values from plane
                    plane.updatePIDGains(((Fragment4)fragment4).getValues()); // not using by name to enable reordering of list

                    ((Fragment4)fragment4).updateViewByName(plane.getResults());

                    // updates PID gain of plane


                }
                handler.postDelayed(this, dtUpdateUI_ms);
            }
        }
    };

    final Runnable taskUpdateSimulation = new Runnable() {
        public void run() {
            Runnable activity = this;
            if(activity!=null)
            {
                handler.postDelayed(this, dtUpdateSimulation_ms);
                extractData();
                plane.orientationAngles = orientationAngles;
                plane.L_val_radio = intToFloatArray(L_val_radio);
                plane.L_val_radio_int = L_val_radio;
                // update plane and its PIDS (with radio and gyros)
                plane.updateDt(dtUpdateSimulation_ms);
                L_val_servos = plane.getResultsInt();
                sendData();
            }
        }
    };

    private float[] intToFloatArray(int[] intArray)
    {
        float[] floatArray = new float[intArray.length];
        for (int i = 0 ; i < intArray.length; i++)
        {
            floatArray[i] = (float) intArray[i];
        }
        return floatArray;
    }


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        plane = new Plane();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        invalidateOptionsMenu();

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Main Page");
        toolbar.setSubtitle("Test Subtitle");
        toolbar.inflateMenu(R.menu.option_menu);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayout,fragment1);
        ft.commit();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(taskUpdateUi,dtUpdateUI_ms);
        handler.postDelayed(taskUpdateSimulation,dtUpdateSimulation_ms);



        //LOCATION
        createLocationRequest();
        //this event is triggered every time the condition are met (here the timer)
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                updateGps();
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
            }
        };
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_FINE_LOCATION);
        startLocationUpdate();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item1:
                Toast.makeText(getApplicationContext(),"LOGGER",Toast.LENGTH_SHORT).show();

                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayout,fragment1);
                ft.commit();
                break;
            case R.id.item2:
                Toast.makeText(getApplicationContext(),"SENSOR",Toast.LENGTH_SHORT).show();

                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayout,fragment2);
                ft.commit();
                break;
            case R.id.item3:
                Toast.makeText(getApplicationContext(),"GPS",Toast.LENGTH_SHORT).show();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayout,fragment3);
                ft.commit();
                break;
            case R.id.item4:
                Toast.makeText(getApplicationContext(),"PID",Toast.LENGTH_SHORT).show();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayout,fragment4);
                ft.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
        // You must implement this callback in your code.
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    @Override
    protected void onPause() {
        super.onPause();

        // Don't receive any more updates from either sensor.
        sensorManager.unregisterListener(this);
    }

    // Get readings from accelerometer and magnetometer. To simplify calculations,
    // consider storing these readings as unit vectors.
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == android.hardware.Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.length);
        } else if (event.sensor.getType() == android.hardware.Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.length);
        }
        updateOrientationAngles();
    }

    // Compute the three orientation angles based on the most recent readings from
    // the device's accelerometer and magnetometer.
    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(rotationMatrix, null,
                accelerometerReading, magnetometerReading);
        // "rotationMatrix" now has up-to-date information.
        SensorManager.getOrientation(rotationMatrix, orientationAngles);
    }




    //LOCATION


    public void saveCurrentLocation()
    {
        savedLocations.add(getCurrentLocation());
    }

    public List<Location> getSavedLocations()
    {
        return savedLocations;
    }

    public Location getCurrentLocation()
    {
        return currentLocation;
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10*MILLIS);
        locationRequest.setFastestInterval(3*MILLIS);
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
    }

    public void startLocationUpdate() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback,  Looper.getMainLooper());
            updateGps();
            Log.i("update","start location update");
        }
        else
        {
            Log.i("update","start location update FAILED");
        }
    }

    public void stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        Log.i("update","stop location update");
    }

    private void updateGps()
    {
        //Log.i("UpdateGps","Update GPS");

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
        {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null) // check that the location isn't null
                    {
                        //Log.i("UpdateGps","not null");
                        locationUpdateCount++;
                        currentLocation = location;
                    }
                    else
                    {
                        //Log.i("UpdateGps","null");
                    }
                }
            });
        }
        else
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                Log.e("UpdateGps","No permission");
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            updateGps();
        }
        else
        {
            Toast.makeText(this, "This app requires permission", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}