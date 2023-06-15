package com.example.myapplication.Interfaces;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Arrays;

public class DataLinkerSelector extends DataDefault {
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
        m_linkerA = new DataLinker("linkerA");
        m_linkerB = new DataLinker("linkerB");
        m_linkerC = new DataLinker("linkerC");
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
        editor.putString("linkerInterfaceStringA", Arrays.deepToString(m_linkerA.getMatrixLinker()));
        editor.putString("linkerInterfaceStringB", Arrays.deepToString(m_linkerB.getMatrixLinker()));
        editor.putString("linkerInterfaceStringC", Arrays.deepToString(m_linkerC.getMatrixLinker()));

        editor.apply();
    }

    @Override
    public void reset() {

    }

    @Override
    public void loadData() {

        String linkerInterfaceStringA = sharedPreferences.getString("linkerInterfaceStringA", null);
        m_linkerA.loadData(linkerInterfaceStringA);

        String linkerInterfaceStringB = sharedPreferences.getString("linkerInterfaceStringB", null);
        m_linkerB.loadData(linkerInterfaceStringB);

        String linkerInterfaceStringC = sharedPreferences.getString("linkerInterfaceStringC", null);
        m_linkerC.loadData(linkerInterfaceStringC);

        forcedLinker = true;
    }

    @Override
    public void loadData(String dataName) {
    }
}
