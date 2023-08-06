package com.example.myapplication.Models;

import android.util.Log;

import com.example.myapplication.ContextProvider;
import com.example.myapplication.Interfaces.DataInterface;

import java.util.ArrayList;
import java.util.List;

public class ModelDefault implements Model{

    public List<DataInterface> m_listData = null;

    ContextProvider m_contextProvider = null;

    @Override
    public void updateDt(float dt_ms) {

    }

    @Override
    public void saveData() {
        if(m_listData == null)
        {
            Log.w("Model save","m_listData is null");
            return;
        }
        if(m_listData.size()==0){
            Log.w("Model save","no data to be saved for this model");
            return;
        }
        Log.i("content : ",m_listData.toString());
        for(DataInterface data:m_listData){
            data.saveData();
        }
    }

    @Override
    public void loadData() {
        if(m_listData == null)
        {
            Log.w("Model save","m_listData is null");
            return;
        }
        if(m_listData.size()==0){
            Log.w("Model load","no data to be loaded for this model");
            return;
        }
        Log.i("content : ",m_listData.toString());
        for(DataInterface data:m_listData){
            data.loadData();
        }
    }

    @Override
    public void changeContext(ContextProvider contextProvider) {
        m_contextProvider = contextProvider;
    }
}
