package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.myapplication.Models.Model;
import com.example.myapplication.Models.ModelFactory;
import com.example.myapplication.Models.ModelGps;
import com.example.myapplication.fragments.FragmentGps;
import com.example.myapplication.fragments.FragmentLogger;
import com.example.myapplication.fragments.FragmentMacroData;
import com.example.myapplication.fragments.FragmentPID;
import com.example.myapplication.fragments.FragmentSensor;
import com.example.myapplication.fragments.FragmentWaypoints;
import com.example.myapplication.fragments.FragmentLinker;
import com.example.myapplication.Models.ModelPlane;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Arrays;


public class MainActivity extends AndroidCommunication implements SensorEventListener {

    public static final int MILLIS = 1000;
    private static final int PERMISSION_FINE_LOCATION = 99;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    public LocationRequest locationRequest;

    Toolbar toolbar;
    FragmentTransaction ft;


    //SENSOR VARIABLES
    Sensor sensor;
    private SensorManager sensorManager;

    private static final int dtUpdateUI_ms = 100;
    private static final int dtUpdateSimulation_ms = 5;

    ModelFactory modelFactory;
    Handler handler;

    final Runnable taskUpdateUi = new Runnable() {
        public void run() {
            Runnable activity = this;
            if(activity!=null)
            {
                if(modelFactory.getFragmentLogger().isVisible())
                {
                    ((FragmentLogger) modelFactory.getFragmentLogger()).updateView(getLogger(),getDebug());
                }
                if(modelFactory.getFragmentSensor().isVisible())
                {
                    ((FragmentSensor) modelFactory.getFragmentSensor()).updateView(accelerometerReading, orientationAngles,L_val_radio);
                    ((FragmentSensor) modelFactory.getFragmentSensor()).updateArrows(-modelFactory.getGps().m_interfaceGps.currentCourse_deg, modelFactory.getGps().m_interfaceGps.deltaCourseToNextWaypoint_deg);
                    //((FragmentSensor) fragmentSensor).updateArrows(480, -90);
                }
                if(modelFactory.getFragmentGps().isVisible())
                {
                    ((FragmentGps) modelFactory.getFragmentGps()).updateView();
                }
                if(modelFactory.getFragmentPID().isVisible())
                {
                    // transfert PID values from plane
                    modelFactory.getPlane().updatePIDGains(); // not using by name to enable reordering of list
                    modelFactory.getPlane().updatePidResults();
                    ((FragmentPID) modelFactory.getFragmentPID()).updateView();
                    // updates PID gain of plane
                }
                if(modelFactory.getFragmentLinker().isVisible())
                {
                    float[][] values = ((FragmentLinker) modelFactory.getFragmentLinker()).getValues();
                    //Log.i("LINKER : ",String.valueOf(values[0][0]));

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
                modelFactory.getPlane().orientationAngles = orientationAngles;
                modelFactory.getPlane().L_val_radio = intToFloatArray(L_val_radio);
                modelFactory.getPlane().L_val_radio_int = L_val_radio;
                // update plane and its PIDS (with radio and gyros)
                modelFactory.getPlane().updateDt(dtUpdateSimulation_ms);
                modelFactory.getGps().updateDt(dtUpdateSimulation_ms);
                L_val_servos = modelFactory.getPlane().getResultsInt();
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

        modelFactory = new ModelFactory();
        modelFactory.createAllModels();

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        loadData();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        invalidateOptionsMenu();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Main Page");
        toolbar.setSubtitle("Test Subtitle");
        toolbar.inflateMenu(R.menu.option_menu);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayout, modelFactory.getFragmentLogger());
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
    protected void onStop() {
        saveData();
        super.onStop();
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
                ft.replace(R.id.FrameLayout, modelFactory.getFragmentLogger());
                ft.commit();
                break;
            case R.id.item2:
                Toast.makeText(getApplicationContext(),"SENSOR",Toast.LENGTH_SHORT).show();

                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayout, modelFactory.getFragmentSensor());
                ft.commit();
                break;
            case R.id.item3:
                Toast.makeText(getApplicationContext(),"GPS",Toast.LENGTH_SHORT).show();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayout, modelFactory.getFragmentGps());
                ft.commit();
                break;
            case R.id.item4:
                Toast.makeText(getApplicationContext(),"PID",Toast.LENGTH_SHORT).show();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayout, modelFactory.getFragmentPID());
                ft.commit();
                break;
            case R.id.item5:
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayout, modelFactory.getFragmentWaypoints());
                ft.commit();
                break;
            case R.id.item6:
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayout, modelFactory.getFragmentLinker());
                ft.commit();
                break;
            case R.id.item7:
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayout, modelFactory.getFragmentMacroData());
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
        modelFactory.getPlane().m_interfaceGps.savedLocations.add(modelFactory.getPlane().m_interfaceGps.currentLocation);
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
                        modelFactory.getPlane().m_interfaceGps.locationUpdateCount++;
                        modelFactory.getPlane().m_interfaceGps.currentLocation = location;
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


    SharedPreferences sharedPreferences;

    public void loadData(){

        String linkerInterfaceStringA = sharedPreferences.getString("linkerInterfaceStringA", null);
        modelFactory.getPlane().m_InterfaceLinkerSelector.m_linkerA.loadData(linkerInterfaceStringA);

        String linkerInterfaceStringB = sharedPreferences.getString("linkerInterfaceStringB", null);
        modelFactory.getPlane().m_InterfaceLinkerSelector.m_linkerB.loadData(linkerInterfaceStringB);

        String linkerInterfaceStringC = sharedPreferences.getString("linkerInterfaceStringC", null);
        modelFactory.getPlane().m_InterfaceLinkerSelector.m_linkerC.loadData(linkerInterfaceStringC);

        String pidInterfaceString = sharedPreferences.getString("pidInterfaceString", null);
        modelFactory.getPlane().pidInterface.loadData(pidInterfaceString);

        Log.i("loadingData","LOADING DATA");

    }


    public void saveData(){

        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putString("linkerInterfaceStringA", Arrays.deepToString(modelFactory.getPlane().m_InterfaceLinkerSelector.m_linkerA.getMatrixLinker()));
        editor.putString("linkerInterfaceStringB", Arrays.deepToString(modelFactory.getPlane().m_InterfaceLinkerSelector.m_linkerB.getMatrixLinker()));
        editor.putString("linkerInterfaceStringC", Arrays.deepToString(modelFactory.getPlane().m_InterfaceLinkerSelector.m_linkerC.getMatrixLinker()));

        editor.putString("pidInterfaceString", Arrays.deepToString(modelFactory.getPlane().pidInterface.getValues()));

        editor.apply();

        Log.i("saveData","QUITTING AFTER SAVING");
    }
}