package com.example.myapplication.Models;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.ContextProvider;
import com.example.myapplication.Interfaces.DataGps;
import com.example.myapplication.Interfaces.DataSms;

public class ModelSms extends BroadcastReceiver implements Model {

    public DataSms dataSms;
    public DataGps dataGps;
    private static final int PERMISSION_REQUEST_SEND_SMS = 123;
    private ContextProvider m_contextProvider = null;

    @Deprecated
    public ModelSms(ContextProvider contextProvider, DataGps gps) {
        m_contextProvider = contextProvider;
        dataSms = new DataSms();
        dataGps = gps;
    }

    public ModelSms(ContextProvider contextProvider, DataGps dataGps,DataSms dataSms) {
        m_contextProvider = contextProvider;
        this.dataSms = dataSms;
        this.dataGps = dataGps;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        String message = smsMessage.getMessageBody();
                        String sender = smsMessage.getDisplayOriginatingAddress();
                        // Handle the received SMS here
                    }
                }
            }
        }
    }

    @Override
    public void updateDt(float dt_ms) {
        time_ms += dt_ms;

        // Check if the permission is granted
        if (ContextCompat.checkSelfPermission(m_contextProvider.getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(m_contextProvider.getActivity(), new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SEND_SMS);
            }
        else {
            // Permission is already granted, proceed with sending SMS
            sendMessage();
        }
    }

    public void sendMessage(){
        if(time_ms-prev_time_ms > dataSms.refreshRate_ms) {
            prev_time_ms = time_ms;
            if (dataSms.sendingData) {
                String message = "message number:";
                message += Integer.toString(dataSms.datSentCount);
                dataSms.datSentCount++;
                Log.i("smsSend", message);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(dataSms.toNumber, null, message, null, null);
            }

            if (dataSms.sendingGps) {
                String message = "GPS:";
                message += " lat:" + Float.toString(dataGps.latitude_deg);
                message += " log:" + Float.toString(dataGps.longitude_deg);
                Log.i("smsSend", message);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(dataSms.toNumber, null, message, null, null);
            }
        }
        if (dataSms.sendingPing) {
            String message = "Ping";
            Log.i("smsSend", message);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(dataSms.toNumber, null, message, null, null);
            dataSms.sendingPing = false;
        }
    }
    private float time_ms = 0.f;
    private float prev_time_ms = 0.f;
}