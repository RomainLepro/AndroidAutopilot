package com.example.myapplication.Interfaces;

import android.content.Context;
import android.content.SharedPreferences;

public class DataLinkerSelector implements DataInterface {
    public DataLinker m_linkerA;
    public DataLinker m_linkerB;
    public DataLinker m_linkerC;
    public DataLinker m_linker;

    public boolean forcedLinker = false;

    public String getLinkerName()
    {
        if(!forcedLinker)
        {
            return "auto:"+m_linker.m_name;
        }
        return m_linker.m_name;
    }

    public DataLinkerSelector(int numInputs, int numOutputs){
        m_linkerA = new DataLinker(numInputs,numOutputs,"linkerA");
        m_linkerB = new DataLinker(numInputs,numOutputs,"linkerB");
        m_linkerC = new DataLinker(numInputs,numOutputs,"linkerC");
        m_linker = m_linkerA;
    }
    public DataLinkerSelector(){
        m_linkerA = new DataLinker();
        m_linkerB = new DataLinker();
        m_linkerC = new DataLinker();
        m_linker = m_linkerA;
    }

    public void selectLinker(int linkerNumber)
    {
        //Log.i("select",Integer.toString(linkerNumber));
        forcedLinker = true;
        switch(linkerNumber){
            case 0:
                m_linker = m_linkerA;
                break;
            case 1:
                m_linker =   m_linkerB;
                break;
            case 2:
                m_linker = m_linkerC;
                break;
            case 3:
                forcedLinker = false;
                break;
        }
    }

    public void requestSelectLinker(int linkerNumber)
    {
        if(forcedLinker){
            return;
        }
        //Log.i("request select",Integer.toString(linkerNumber));
        switch(linkerNumber){
            case 0:
                m_linker = m_linkerA;
                break;
            case 1:
                m_linker =   m_linkerB;
                break;
            case 2:
                m_linker = m_linkerC;
                break;
            default:
                m_linker = m_linkerA;
                break;
        }
    }

    public void resetCurrentLinker(){
        m_linker.initialiseMatrix();
    }

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
