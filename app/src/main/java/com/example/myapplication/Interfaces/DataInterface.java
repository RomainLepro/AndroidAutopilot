package com.example.myapplication.Interfaces;

import android.content.Context;
import android.content.SharedPreferences;

public interface DataInterface {

    void saveData();
    void reset();
    void loadData();
    void loadData(String dataName);

    void loadSharedPreferences(SharedPreferences sharedPreferences);

}
