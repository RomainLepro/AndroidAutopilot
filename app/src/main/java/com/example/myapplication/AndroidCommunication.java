package com.example.myapplication;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AndroidCommunication extends AppCompatActivity implements SerialInputOutputManager.Listener{

    private static final int READ_WAIT_MILLIS = 100;
    private static final int WRITE_WAIT_MILLIS = 100;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private String logger =  "";


    /*
    Manage all the usb stuff
     */
    private UsbManager manager;
    private List<UsbSerialDriver> availableDrivers;
    private UsbSerialDriver driver;
    private UsbDeviceConnection connection;
    private UsbSerialPort port;
    private SerialInputOutputManager usbIoManager;

    private final Handler mainLooper;

    public AndroidCommunication()
    {
        mainLooper = new Handler(Looper.getMainLooper());
    }


    TimerTask timer= new TimerTask(){

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //do something periodically
                }
            });

        }
    };


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction() == Manifest.permission.REQUEST_COMPANION_USE_DATA_IN_BACKGROUND) {
                startUsb();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startUsb();

        Timer t = new Timer();
        t.scheduleAtFixedRate(timer , 0 , 1000);

    }

    @Override
    public void onNewData(byte[] data) {
        runOnUiThread(() -> {
            logger +=(new String(data)); });
    }

    private void receive(byte[] data) {

        logger +=(new String(data, StandardCharsets.UTF_8));
    }

    public void send(String message)
    {
        Log.i("send","send");
        if(port!=null && port.isOpen())
        {
            byte[] request;
            String sendString = message;
            logger+=("sending : "+message+"\n");
            request = sendString.getBytes();
            try {
                port.write(request, WRITE_WAIT_MILLIS);
                logger+=("send success\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            logger+=("send failed\n");
        }
    }


    void read()
    {
        if(port!=null && port.isOpen()) {
            byte[] response = new byte[100];
            try {
                if(port.read(response, READ_WAIT_MILLIS)>0)
                {
                    logger+=(new String(response, StandardCharsets.UTF_8));
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startUsb()
    {
        Log.i("startUsb","startUsb");
        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        logger+=("starting USB\n");
        if (availableDrivers.isEmpty()) {
            logger+=("no available driver\n");
            return;
        }

        // Open a connection to the first available driver.
        driver = availableDrivers.get(0);
        connection = manager.openDevice(driver.getDevice());

        if (connection == null) {
            PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
            manager.requestPermission(driver.getDevice(),permissionIntent);
            logger+=("asked permission\n");
            connection = manager.openDevice(driver.getDevice());
            if (connection == null) {
                logger+=("failed to connect\n");
                return;
            }
        }

        port = driver.getPorts().get(0); // Most devices have just one port (port 0)
        logger+=("opening port\n");

        try {
            port.open(connection);
            port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
        } catch (IOException e) {
            e.printStackTrace();
            logger+=("failed to open connection with port\n");
        }

        if(port.isOpen())
        {
            logger+=("connection succeeded !\n");
            byte[] response = new byte[100];
            try {
                int len = port.read(response, READ_WAIT_MILLIS);
            } catch (IOException e) {
                e.printStackTrace();
            }

            usbIoManager = new SerialInputOutputManager(port, this);
            usbIoManager.start();
        }
        else
        {
            logger+=("port not open\n");
        }
    }


    @Override
    public void onRunError(Exception e) {
        Log.e("onRunError","ERROR in arduino");
    }

    public String getLogger()
    {
        Log.i("getLogger",Integer.toString(logger.length()));
        if(logger.length()>500)
        {
            int remove =  logger.length()- 500;
            logger = logger.substring(remove,logger.length());
        }
        return logger;
    }

    public boolean isConnected() {
        if(port!=null)
        {
            return port.isOpen();
        }
        return false;
    }
}
