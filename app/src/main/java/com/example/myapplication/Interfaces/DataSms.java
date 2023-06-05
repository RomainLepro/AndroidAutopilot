package com.example.myapplication.Interfaces;

public class DataSms implements DataInterface{

    public String fromNumber = "0780373319";
    public String toNumber = "0780373319";
    public int refreshRate_ms = 10000; // 10s refreshRate

    public int datSentCount = 0;

    public int gpsDataCount = 1;
    public boolean sendingData = false;
    public boolean sendingGps = false;
    public boolean sendingPing = false;

    @Override
    public void saveData() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void loadData(String dataName) {

    }
}
