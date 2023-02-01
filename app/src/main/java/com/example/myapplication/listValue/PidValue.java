package com.example.myapplication.listValue;

public class PidValue {
    public String name;
    public float Presult;
    public float Iresult;
    public float Dresult;

    public float PIDresult;

    public PidValue(String name) {
        this.name = name;
        this.Presult = 0.f;
        this.Iresult = 0.f;
        this.Dresult = 0.f;
        this.PIDresult = 0.f;

    }

}
