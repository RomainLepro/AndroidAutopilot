package com.example.myapplication.service;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Log;


import com.example.myapplication.Interfaces.DataLogger;
import com.example.myapplication.Interfaces.DataRadio;
import com.example.myapplication.Models.Model;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.List;


//TODO:
/*
ModelComunication extend ModelDefault;
member => myBroadcastReceiver

myBroadcastReceiver extend BroadcastReceiver
 */


public class MyBroadcastReceiver extends BroadcastReceiver  implements SerialInputOutputManager.Listener {
    public static final int MAX_ANSWER_DELAY_MS = 200;

    public DataRadio dataRadio;

    public DataLogger dataLogger;

    public long lastDeviceAnswer = 0;



    private static final int READ_WAIT_MILLIS = 100;
    private static final int WRITE_WAIT_MILLIS = 100;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private String word = "";

    /*
    Manage all the usb stuff
     */
    private UsbManager usbManager;
    private List<UsbSerialDriver> availableDrivers;
    private UsbSerialDriver driver;
    private UsbDeviceConnection connection;
    private UsbSerialPort port;
    private SerialInputOutputManager usbIoManager;


    private BroadcastReceiver broadcastReceiver;

    public MyBroadcastReceiver(DataRadio dataRadio, DataLogger dataLogger) {
        this.dataLogger = dataLogger;
        this.dataRadio = dataRadio;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        dataLogger.debug+="received action:"+action+"\n";
        if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
            // A USB device has been connected
            // Request permission for the device
            UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            if (device != null) {
                dataLogger.debug+="New Device connected\n";
                PendingIntent permissionIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        new Intent("com.example.USB_PERMISSION"),
                        0
                );

                // Register the BroadcastReceiver to handle the permission result
                IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);//TODO not working, never called
                context.registerReceiver(this,filter);
                usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
                usbManager.requestPermission(device, permissionIntent);
            }
        }
    }

    public void startUsb()
    {
        Log.i("startUsb","startUsb");
        dataLogger.debug+=("starting USB\n");

        if(usbManager==null)
        {
            dataLogger.debug+="usb manager is null\n";
            return;
        }
        availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);
        if(availableDrivers.size()==0)
        {
            dataLogger.debug += "no driver available\n";
            return;
        }
        driver = availableDrivers.get(0);
        port = driver.getPorts().get(0); // Most devices have just one port (port 0)
        dataLogger.debug += "port obtained\n";
        try {
            // Open a connection to the first available driver.
            driver = availableDrivers.get(0);
            connection = usbManager.openDevice(driver.getDevice());
            dataLogger.debug+=("opening port\n");
            port.open(connection);
            port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
        } catch (IOException e) {
            e.printStackTrace();
            dataLogger.debug+=("failed to open connection with port\n");
            return;
        }

        if(port.isOpen())
        {
            dataLogger.debug+=("connection succeeded !\n");
            byte[] response = new byte[100];
            try {
                int len = port.read(response, READ_WAIT_MILLIS);
                usbIoManager = new SerialInputOutputManager(port, this);
                usbIoManager.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            dataLogger.debug+=("port not open\n");
        }
    }

    @Override
    public void onNewData(byte[] data) {
        String message = (new String(data));
        dataLogger.logger_in += message;
        lastDeviceAnswer = System.currentTimeMillis();

    }

    public void send(String message)
    {
        Log.i("send","send");
        if(port!=null && port.isOpen())
        {
            byte[] request;
            String sendString = message;
            dataLogger.logger_out += message;
            request = sendString.getBytes();
            try {
                port.write(request, WRITE_WAIT_MILLIS);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            dataLogger.debug+=("send failed\n");
        }
    }

    public void sendData()
    {
        if(!isConnected())
        {
            return;
        }
        String mot = "";
        for(int i = 0;i<dataRadio.L_val_servos_int.length;i++)
        {
            mot += Integer.toString(dataRadio.L_val_servos_int[i]);
            mot += ";";
        }
        mot += '\n';
        send(mot);
    }
    @Override
    public void onRunError(Exception e) {
        Log.e("onRunError","ERROR in arduino");
    }

    public boolean isConnected() {
        if(port!=null)
        {
            boolean isAnswering = (System.currentTimeMillis()-lastDeviceAnswer)< MAX_ANSWER_DELAY_MS;
            if(!isAnswering)
            {
                dataLogger.debug += "Not Answering for : "+Integer.toString(MAX_ANSWER_DELAY_MS)+"\n";
            }
            return port.isOpen() && isAnswering; //this isn't reliable to know if the connection is open
        }
        return false;
    }

}
