package com.example.myapplication.Models;

import android.util.Log;

import com.example.myapplication.Interfaces.DataInterface;

import java.util.ArrayList;
import java.util.List;

public interface Model {



    public void updateDt(float dt_ms);
    public void saveData() ;

    public void loadData() ;

}
