package com.example.myapplication.Models;

import android.util.Log;

import com.example.myapplication.ContextProvider;
import com.example.myapplication.Interfaces.DataInterface;

import java.util.ArrayList;
import java.util.List;

public interface Model {


    //Todo public string getModelName();
    public void updateDt(float dt_ms);
    public void saveData() ;
    public void loadData() ;
    public void changeContext(ContextProvider contextProvider);

}
