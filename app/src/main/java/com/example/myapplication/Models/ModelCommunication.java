package com.example.myapplication.Models;

import android.content.BroadcastReceiver;


import com.example.myapplication.Interfaces.DataLogger;
import com.example.myapplication.Interfaces.DataRadio;
import com.example.myapplication.service.MyBroadcastReceiver;


//TODO:
/*
ModelComunication extend ModelDefault;
member => myBroadcastReceiver

myBroadcastReceiver extend BroadcastReceiver
 */


public class ModelCommunication extends ModelDefault {

    public DataRadio dataRadio = new DataRadio();

    public DataLogger dataLogger = new DataLogger();

    public MyBroadcastReceiver myBroadcastReceiver;

    /*
    Manage all the usb stuff
     */


    private BroadcastReceiver broadcastReceiver;

    public ModelCommunication(DataRadio dataRadio, DataLogger dataLogger)
    {
        this.dataRadio = dataRadio;
        this.dataLogger = dataLogger;
        myBroadcastReceiver = new MyBroadcastReceiver(this.dataRadio, this.dataLogger);
    }


    public void updateDt(float dt_ms)
    {
        if(dataLogger.requestConnection)
        {
            myBroadcastReceiver.startUsb();
            dataLogger.requestConnection = false;
        }

        extractData();
        getLogger_in();
        getLogger_out();
        getDebug();
        myBroadcastReceiver.sendData();
    }

    @Override
    public void saveData() {

    }

    @Override
    public void loadData() {

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
