package com.example.myapplication.Interfaces;

public class DataRadio implements DataInterface {



    public String L_name_servos[] = {"S1","S2","S3","TH1","TH2","S4","S5","S6"};
    public int L_val_servos_int[] = {500,500,500,000,000,500,500,500};
    public String L_name_radio[] = {"OX","OY","OZ","TH","SA","SB","HE","TE"};
    public int L_val_radio_int[] = {500,500,500,500,500,500,500,500};

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
