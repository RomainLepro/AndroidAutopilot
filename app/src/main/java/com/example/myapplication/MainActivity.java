package com.example.myapplication;

import static java.lang.Math.max;
import static java.lang.Math.min;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.Models.ModelFactory;
import com.example.myapplication.fragments.FragmentGps;
import com.example.myapplication.fragments.FragmentLogger;
import com.example.myapplication.fragments.FragmentMacroData;
import com.example.myapplication.fragments.FragmentPID;
import com.example.myapplication.fragments.FragmentSensor;
import com.example.myapplication.fragments.FragmentLinker;
import com.example.myapplication.fragments.FragmentSms;

import java.util.Arrays;


public class MainActivity extends AndroidCommunication implements ContextProvider {

    public static final int MILLIS = 1000;
    public static final int PERMISSION_FINE_LOCATION = 99;

    Toolbar toolbar;
    FragmentTransaction ft;

    private static final int dtUpdateUI_ms = 100;
    private static final int dtUpdateSimulation_ms = 5;
    long startTime_us = 0;
    long endTime_us = 0;


    ModelFactory modelFactory;
    Handler handler;

    final Runnable taskUpdateUi = new Runnable() {
        public void run() {
            Runnable activity = this;
            if(activity!=null)
            {
                //TODO all this shit should be in the implementation of the factory
                if(modelFactory.getFragmentLogger().isVisible())
                {
                    ((FragmentLogger) modelFactory.getFragmentLogger()).updateView();
                }
                if(modelFactory.getFragmentSensor().isVisible())
                {
                    ((FragmentSensor) modelFactory.getFragmentSensor()).updateView();
                    ((FragmentSensor) modelFactory.getFragmentSensor()).updateArrows(-modelFactory.getGps().dataGps.currentCourse_deg, modelFactory.getGps().dataGps.deltaCourseToNextWaypoint_deg);
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
                    ((FragmentLinker)modelFactory.getFragmentLinker()).updateView();
                    //Log.i("LINKER : ",String.valueOf(values[0][0]));
                }
                if(modelFactory.getFragmentMacroData().isVisible())
                {
                    ((FragmentMacroData)modelFactory.getFragmentMacroData()).updateView();
                }
                if(modelFactory.getFragmentSms().isVisible())
                {
                    ((FragmentSms)modelFactory.getFragmentSms()).updateView();
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

                //modelFactory.getPlane().dataRadio.L_val_radio = modelFactory.getPlane().intToFloatArray(L_val_radio);
                //modelFactory.getPlane().dataRadio.L_val_radio_int = L_val_radio;
                // update plane and its PIDS (with radio and gyros)

                endTime_us = System.nanoTime();
                float dt_ms = (endTime_us - startTime_us) / 1000000.f;
                startTime_us = endTime_us;

                dt_ms = max((float)dtUpdateSimulation_ms/10.f,dt_ms);
                dt_ms = min((float)dtUpdateSimulation_ms*10.f,dt_ms);

                updateDt(dt_ms); //TODO put this inside a model in factory
                modelFactory.updateDt(dt_ms);
                sendData();
            }
        }
    };




    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        modelFactory = new ModelFactory(this);
        modelFactory.createAllModels();
        modelFactory.loadSharedPreferences( getSharedPreferences("MyPreferences", Context.MODE_PRIVATE));
        modelFactory.loadData();

        dataRadio = modelFactory.getPlane().dataRadio; // TODO be removed (see AndroidComunication-
        dataLogger = ((FragmentLogger)(modelFactory.getFragmentLogger())).m_interfaceLogger; // TODO be removed (see AndroidComunication-

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

        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(taskUpdateUi,dtUpdateUI_ms);
        handler.postDelayed(taskUpdateSimulation,dtUpdateSimulation_ms);

    }

    @Override
    protected void onStop() {
        modelFactory.saveData();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //TODO move into factory
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
            case R.id.item8:
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayout, modelFactory.getFragmentSms());
                ft.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        modelFactory.loadData();
        super.onResume();

    }

    @Override
    protected void onPause() {
        modelFactory.saveData();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            Log.i("Main activity gpas permission","Permission Granted");
            //TODO update position
        }
        else
        {
            Toast.makeText(this, "This app requires permission", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    SharedPreferences sharedPreferences;

    public void loadData(){
        
        //TODO make load and save data methode in Interfaces data
        Log.i("loadingData","LOADING DATA");



    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
