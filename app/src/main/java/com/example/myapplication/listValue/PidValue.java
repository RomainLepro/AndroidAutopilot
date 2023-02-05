package com.example.myapplication.listValue;

import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;

public class PidValue {
    public String name;
    public float Presult,Iresult,Dresult,PIDresult;
    public float P,I,D;

    TextView tv_pid  = null;
    TextView tv_p_result = null;
    TextView tv_i_result = null;
    TextView tv_d_result = null;
    TextView tv_pid_result = null;

    EditText tv_p = null;



    public PidValue(String name) {
        this.name = name;
        this.Presult = 0.f;
        this.Iresult = 0.f;
        this.Dresult = 0.f;
        this.PIDresult = 0.f;
        this.P = 11.f;
        this.I = 0.1f;
        this.D = 0.1f;
    }


}
