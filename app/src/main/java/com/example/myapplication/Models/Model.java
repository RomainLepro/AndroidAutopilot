package com.example.myapplication.Models;

import android.util.Log;

import com.example.myapplication.Interfaces.DataInterface;

import java.util.ArrayList;
import java.util.List;

public interface Model {

    List<DataInterface> listData = new ArrayList<DataInterface>();

    public void updateDt(float dt_ms);
    default public void saveData() {
        if(listData.size()==0){
            Log.w("Model save","no data to be saved for this model");
            return;
        }
        for(DataInterface data:listData){
            data.saveData();
        }
    }

    default public void loadData() {
        if(listData.size()==0){
            Log.w("Model load","no data to be loaded for this model");
            return;
        }
        for(DataInterface data:listData){
            data.loadData();
        }
    }

}
