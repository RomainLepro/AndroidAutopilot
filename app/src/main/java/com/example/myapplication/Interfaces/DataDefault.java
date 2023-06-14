package com.example.myapplication.Interfaces;

import android.content.SharedPreferences;

public class DataDefault implements DataInterface{

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

    public void loadSharedPreferences(SharedPreferences inSharedPreferences){
        sharedPreferences = inSharedPreferences;
        editor = sharedPreferences.edit();
    }


    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor = null;
}
