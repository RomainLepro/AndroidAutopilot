package com.example.myapplication.service;

import static java.lang.Math.max;
import static java.lang.Math.min;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;



/*
To start this forground service :

Intent serviceIntent = new Intent(context, MyForegroundService.class);
ContextCompat.startForegroundService(context, serviceIntent);

// Configure notification for the foreground service
Notification notification = createNotification(); // Create a custom notification
startForeground(NOTIFICATION_ID, notification);





 */

public class MyForegroundService extends Service {
    private static final int NOTIFICATION_ID = 1;


    final Runnable taskUpdateSimulation = new Runnable() {
        public void run() {
            Runnable activity = this;
            if(activity!=null)
            {

            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        // Perform initialization tasks here
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Handle the start command for your foreground service
        // Return START_STICKY or other appropriate value based on your requirements

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(taskUpdateSimulation,10);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Perform any cleanup tasks here
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
