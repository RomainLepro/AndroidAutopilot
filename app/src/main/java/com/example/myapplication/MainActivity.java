package com.example.myapplication;

import static java.lang.Math.max;
import static java.lang.Math.min;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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


public class MainActivity extends AppCompatActivity implements ContextProvider {

    public static final int MILLIS = 1000;
    public static final int PERMISSION_FINE_LOCATION = 99;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
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
                modelFactory.updateUI();
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

                endTime_us = System.nanoTime();
                float dt_ms = (endTime_us - startTime_us) / 1000000.f;
                startTime_us = endTime_us;
                dt_ms = max((float)dtUpdateSimulation_ms/10.f,dt_ms);
                dt_ms = min((float)dtUpdateSimulation_ms*10.f,dt_ms);

                modelFactory.updateDt(dt_ms);
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

        IntentFilter filter = new IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        registerReceiver(modelFactory.getCommunication().myBroadcastReceiver, filter);

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
                modelFactory.setActiveFragment(modelFactory.getFragmentLogger());
                break;
            case R.id.item2:
                Toast.makeText(getApplicationContext(),"SENSOR",Toast.LENGTH_SHORT).show();
                modelFactory.setActiveFragment(modelFactory.getFragmentSensor());
                break;
            case R.id.item3:
                Toast.makeText(getApplicationContext(),"GPS",Toast.LENGTH_SHORT).show();
                modelFactory.setActiveFragment(modelFactory.getFragmentGps());
                break;
            case R.id.item4:
                Toast.makeText(getApplicationContext(),"PID",Toast.LENGTH_SHORT).show();
                modelFactory.setActiveFragment(modelFactory.getFragmentPID());
                break;
            case R.id.item5:
                modelFactory.setActiveFragment(modelFactory.getFragmentWaypoints());
                break;
            case R.id.item6:
                modelFactory.setActiveFragment(modelFactory.getFragmentLinker());
                break;
            case R.id.item7:
                modelFactory.setActiveFragment(modelFactory.getFragmentMacroData());
                break;
            case R.id.item8:
                modelFactory.setActiveFragment(modelFactory.getFragmentSms());
                break;
        }

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayout, modelFactory.getActiveFragment());
        ft.commit();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        modelFactory.setActiveFragment(modelFactory.getPreviousFragment());
        Toast.makeText(getApplicationContext(),"RETURN",Toast.LENGTH_SHORT).show();
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayout, modelFactory.getActiveFragment());
        ft.commit();
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
        }
        else
        {
            Toast.makeText(this, "This app requires permission", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    SharedPreferences sharedPreferences;

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
