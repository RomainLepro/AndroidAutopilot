package com.example.myapplication.Interfaces;

import android.util.Log;

public class InterfaceLinkerSelector implements Interface{
    public InterfaceLinker m_linkerA;
    public InterfaceLinker m_linkerB;
    public InterfaceLinker m_linkerC;
    public InterfaceLinker m_linker;

    public boolean forcedLinker = false;

    public String getLinkerName()
    {
        if(!forcedLinker)
        {
            return "auto:"+m_linker.m_name;
        }
        return m_linker.m_name;
    }

    public InterfaceLinkerSelector(int numInputs,int numOutputs){
        m_linkerA = new InterfaceLinker(numInputs,numOutputs,"linkerA");
        m_linkerB = new InterfaceLinker(numInputs,numOutputs,"linkerB");
        m_linkerC = new InterfaceLinker(numInputs,numOutputs,"linkerC");
        m_linker = m_linkerA;
    }
    public InterfaceLinkerSelector(){
        m_linkerA = new InterfaceLinker();
        m_linkerB = new InterfaceLinker();
        m_linkerC = new InterfaceLinker();
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
}
