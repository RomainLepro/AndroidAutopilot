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

import com.example.myapplication.Interfaces.DataLogger;
import com.example.myapplication.Interfaces.DataRadio;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.List;


public class AndroidCommunication extends AppCompatActivity implements SerialInputOutputManager.Listener{



    //TODO use data interfaceRadio, should be a model

    public DataRadio dataRadio = new DataRadio();

    public DataLogger dataLogger = new DataLogger();



    private static final int READ_WAIT_MILLIS = 100;
    private static final int WRITE_WAIT_MILLIS = 100;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
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
    }

    @Override
    public void onNewData(byte[] data) {
        runOnUiThread(() -> {
            String message = (new String(data));
            dataLogger.logger_in += message;
        });
    }

    public void updateDt(float dt_ms)
    {
        extractData();
        getLogger_in();
        getLogger_out();
        getDebug();
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

    public void startUsb()
    {
        Log.i("startUsb","startUsb");
        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        dataLogger.debug+=("starting USB\n");
        if (availableDrivers.isEmpty()) {
            dataLogger.debug+=("no available driver\n");
            return;
        }

        // Open a connection to the first available driver.
        driver = availableDrivers.get(0);
        connection = manager.openDevice(driver.getDevice());

        if (connection == null) {
            PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
            manager.requestPermission(driver.getDevice(),permissionIntent);
            dataLogger.debug+=("asked permission\n");
            connection = manager.openDevice(driver.getDevice());
            if (connection == null) {
                dataLogger.debug+=("failed to connect\n");
                return;
            }
        }

        port = driver.getPorts().get(0); // Most devices have just one port (port 0)
        dataLogger.debug+=("opening port\n");

        try {
            port.open(connection);
            port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
        } catch (IOException e) {
            e.printStackTrace();
            dataLogger.debug+=("failed to open connection with port\n");
        }

        if(port.isOpen())
        {
            dataLogger.debug+=("connection succeeded !\n");
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
            dataLogger.debug+=("port not open\n");
        }
    }


    @Override
    public void onRunError(Exception e) {
        Log.e("onRunError","ERROR in arduino");
    }

    public String getLogger_in()
    {
        if(dataLogger.logger_in.length()>dataLogger.maxLogSize)
        {
            int remove =  dataLogger.logger_in.length()- dataLogger.maxLogSize;
            dataLogger.logger_in = dataLogger.logger_in.substring(remove, dataLogger.logger_in.length());
        }
        return dataLogger.logger_in;
    }

    public String getLogger_out()
    {
        if(dataLogger.logger_out.length()>dataLogger.maxLogSize)
        {
            int remove =  dataLogger.logger_out.length()- dataLogger.maxLogSize;
            dataLogger.logger_out = dataLogger.logger_out.substring(remove, dataLogger.logger_out.length());
        }
        return dataLogger.logger_out;
    }

    public String getDebug()
    {
        if(dataLogger.debug.length()>dataLogger.maxLogSize)
        {
            int remove =  dataLogger.debug.length()- dataLogger.maxLogSize;
            dataLogger.debug = dataLogger.debug.substring(remove,dataLogger.debug.length());
        }
        return dataLogger.debug;
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
        if(dataLogger.logger_in.length()<100)
        {
            return;
        }
        int ind = (int) (dataLogger.logger_in.length()-50);
        String L[] = dataLogger.logger_in.substring(ind).split(String.valueOf('\n'));


        for(int j=0;j< L.length-1;j++)
        {

            word = L[j];
            if(word.length()>=4)
            {
                String NAME = word.substring(0,2);
                String VAL = word.substring(3,word.length()-1);

                for (int i =0;i< dataRadio.L_name_radio.length;i++) {
                    if(dataRadio.L_name_radio[i].equals(NAME))
                    {
                        try {
                            dataRadio.L_val_radio_int[i] = Integer.parseInt(VAL);
                        }
                        catch(Exception e){
                            dataLogger.debug+='\n'+"value gotten : " + VAL + " IS NOT A NUMBER" +'\n';
                            dataLogger.debug+="size : " + VAL.length() +'\n';
                            return;
                        }

                        dataLogger.debug+="value gotten : " + NAME +'\n';
                        dataLogger.debug+="value gotten : " + Integer.toString(dataRadio.L_val_radio_int[i])+'\n';
                        dataLogger.debug+="value gotten : " + VAL+'\n';
                        return;
                    }
                }
                Log.i("name not found : ",NAME);
                dataLogger.debug+="name not found"+'\n';
                return;
            }
        }

    }

    public void extractData()
    {
        int dataSize = 40;
        if(dataLogger.logger_in.length()<=dataSize*2)
        {
            return;
        }

        String sub = dataLogger.logger_in.substring(dataLogger.logger_in.length()-dataSize*2);
        String[] lines = sub.split(Character.toString('\n'));
        if(lines.length <1)
        {
            dataLogger.debug+=("not enough lines, size : " + lines.length + '\n');
            return;
        }
        String[] split = (lines[1]).split(";");

        if(split.length < 5)
        {
            dataLogger.debug+=("not enough data, size : " + split.length + '\n');
            return;
        }
        if(split.length > dataRadio.L_val_radio_int.length+1)
        {
            dataLogger.debug+=("too much data, size : " + split.length + '\n');
            return;
        }
        for(int i=0;i<split.length-1;i++)
        {
            //System.out.println(split[i]); TODO useless ?
            try {
                dataRadio.L_val_radio_int[i] = Integer.parseInt(split[i]);
            }
            catch(Exception e){
                dataLogger.debug+='\n'+"value gotten : " + split[i] + " IS NOT A NUMBER" +'\n';
                return;
            }
        }

    }


}
