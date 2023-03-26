package com.example.myapplication.Models;

import com.example.myapplication.Interfaces.InterfaceGps;

public class ModelGps {
    public InterfaceGps m_interfaceGps;
    public ModelGps(){
        m_interfaceGps = new InterfaceGps();
    }

    public ModelGps(InterfaceGps interfaceGps){
        m_interfaceGps = interfaceGps;
    }


}
