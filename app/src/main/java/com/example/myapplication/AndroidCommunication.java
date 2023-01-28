package com.example.myapplication;

import static java.lang.Long.min;

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


    static String L_name_servos[] = {"S1","S2","S2"};

    static int L_val_servos[] = {500,500,500};
    static String L_name_radio[] = {"OX","OY","OZ","TH","SA","SB","HE","TE"};
    static int L_val_radio[] = {500,500,500,500,500,500,500,500};

    private static final int READ_WAIT_MILLIS = 100;
    private static final int WRITE_WAIT_MILLIS = 100;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private String logger = "" ,debug =  "";
    private String word = "";


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
            String message = (new String(data));
            logger += message;
        });
    }

    public void send(String message)
    {
        Log.i("send","send");
        if(port!=null && port.isOpen())
        {
            byte[] request;
            String sendString = message;
            request = sendString.getBytes();
            try {
                port.write(request, WRITE_WAIT_MILLIS);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            debug+=("send failed\n");
        }
    }

    public void sendData()
    {
        if(!isConnected())
        {
            return;
        }
        String mot = "";
        for(int i = 0;i<L_val_servos.length;i++)
        {
            mot += Integer.toString(L_val_servos[i]);
            mot += ";";
        }
        mot += '\n';
        send(mot);
    }

    public void startUsb()
    {
        Log.i("startUsb","startUsb");
        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        debug+=("starting USB\n");
        if (availableDrivers.isEmpty()) {
            debug+=("no available driver\n");
            return;
        }

        // Open a connection to the first available driver.
        driver = availableDrivers.get(0);
        connection = manager.openDevice(driver.getDevice());

        if (connection == null) {
            PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
            manager.requestPermission(driver.getDevice(),permissionIntent);
            debug+=("asked permission\n");
            connection = manager.openDevice(driver.getDevice());
            if (connection == null) {
                debug+=("failed to connect\n");
                return;
            }
        }

        port = driver.getPorts().get(0); // Most devices have just one port (port 0)
        debug+=("opening port\n");

        try {
            port.open(connection);
            port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
        } catch (IOException e) {
            e.printStackTrace();
            debug+=("failed to open connection with port\n");
        }

        if(port.isOpen())
        {
            debug+=("connection succeeded !\n");
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
            debug+=("port not open\n");
        }
    }


    @Override
    public void onRunError(Exception e) {
        Log.e("onRunError","ERROR in arduino");
    }

    public String getLogger()
    {
        if(logger.length()>500)
        {
            int remove =  logger.length()- 500;
            logger = logger.substring(remove,logger.length());
        }
        return logger;
    }

    public String getDebug()
    {
        if(debug.length()>500)
        {
            int remove =  debug.length()- 500;
            debug = debug.substring(remove,debug.length());
        }
        return debug;
    }

    public boolean isConnected() {
        if(port!=null)
        {
            return port.isOpen();
        }
        return false;
    }



    public void extractData_old()
    {
        if(logger.length()<100)
        {
            return;
        }
        int ind = (int) (logger.length()-50);
        String L[] = logger.substring(ind).split(String.valueOf('\n'));


        for(int j=0;j< L.length-1;j++)
        {

            word = L[j];
            if(word.length()>=4)
            {
                String NAME = word.substring(0,2);
                String VAL = word.substring(3,word.length()-1);

                for (int i =0;i<L_name_radio.length;i++) {
                    if(L_name_radio[i].equals(NAME))
                    {
                        try {
                            L_val_radio[i] = Integer.parseInt(VAL);
                        }
                        catch(Exception e){
                            debug+='\n'+"value gotten : " + VAL + " IS NOT A NUMBER" +'\n';
                            debug+="size : " + VAL.length() +'\n';
                            return;
                        }

                        debug+="value gotten : " + NAME +'\n';
                        debug+="value gotten : " + Integer.toString(L_val_radio[i])+'\n';
                        debug+="value gotten : " + VAL+'\n';
                        return;
                    }
                }
                Log.i("name not found : ",NAME);
                debug+="name not found"+'\n';
                return;
            }
        }

    }

    public void extractData()
    {
        int dataSize = 40;
        if(logger.length()<=dataSize*2)
        {
            return;
        }

        String sub = logger.substring(logger.length()-dataSize*2);
        String[] lines = sub.split(Character.toString('\n'));
        if(lines.length <1)
        {
            debug+=("not enough lines, size : " + lines.length + '\n');
            return;
        }
        String[] split = (lines[1]).split(";");

        if(split.length < 5)
        {
            debug+=("not enough data, size : " + split.length + '\n');
            return;
        }
        if(split.length > L_val_radio.length+1)
        {
            debug+=("too much data, size : " + split.length + '\n');
            return;
        }
        for(int i=0;i<split.length-1;i++)
        {
            System.out.println(split[i]);
            try {
                L_val_radio[i] = Integer.parseInt(split[i]);
            }
            catch(Exception e){
                debug+='\n'+"value gotten : " + split[i] + " IS NOT A NUMBER" +'\n';
                return;
            }
        }

    }
}
