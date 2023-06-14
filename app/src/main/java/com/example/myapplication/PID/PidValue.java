package com.example.myapplication.PID;

import android.util.Log;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;

public class PidValue {
    public String name;
    public float Presult,Iresult,Dresult,PIDresult;
    public float P,I,D;

    EditText tv_title;
    TextView tv_p_result,tv_i_result,tv_d_result;
    TextView tv_pid,tv_pid_result;
    EditText tv_p,tv_i,tv_d;

    Switch sw_invert;

    boolean invert;

    public PidValue(String name,float[] PIDS) {
        this.name = name;
        this.Presult = 0.f;
        this.Iresult = 0.f;
        this.Dresult = 0.f;
        this.PIDresult = 123.4f;
        this.P = PIDS[0];
        this.I = PIDS[1];
        this.D = PIDS[2];
        this.invert = false;
    }


    public void init(TableRow rowTitle,TableRow rowP,TableRow rowI,TableRow rowD)
    {
        tv_title = (EditText)rowTitle.getChildAt(0); // fixed
        tv_title.setText(name);

        Log.i("init","rowP:"+Integer.toString(rowP.getChildCount()));

        tv_p_result = (TextView)rowP.getChildAt(2);
        tv_i_result = (TextView)rowI.getChildAt(2);
        tv_d_result = (TextView)rowD.getChildAt(2);

        tv_pid_result = (TextView)rowTitle.getChildAt(1);

        tv_p = (EditText)rowP.getChildAt(1);
        tv_i = (EditText)rowI.getChildAt(1);
        tv_d = (EditText)rowD.getChildAt(1);

        sw_invert = (Switch)rowTitle.getChildAt(2);

        //init p i d values
        tv_p.setText(Float.toString(P));
        tv_i.setText(Float.toString(I));
        tv_d.setText(Float.toString(D));

         sw_invert.setChecked(invert);

        update();
    }

    public void update()
    {
        name = tv_title.getText().toString();
        tv_p_result.setText(String.format("%.2f",Presult));
        tv_i_result.setText(String.format("%.2f",Iresult));
        tv_d_result.setText(String.format("%.2f",Dresult));
        tv_pid_result.setText(String.format("%.2f",PIDresult));
        P = toFloat(tv_p.getText().toString());
        I = toFloat(tv_i.getText().toString());
        D = toFloat(tv_d.getText().toString());
        invert = sw_invert.isChecked();
        //Log.i("update",Float.toString(P));
    }

    public void setUpdate()
    {
        tv_p.setText(Float.toString(P));
        tv_i.setText(Float.toString(I));
        tv_d.setText(Float.toString(D));
    }


    float toFloat(String val)
    {
        if(val==null || val=="" ||val.isEmpty())  return 0.f;
        return  Float.parseFloat(val);
    }

    public float[] getPID()
    {
        if(invert)
        {
            float[] res = {-P,-I,-D};
            return res;
        }
        float[] res = {P,I,D};
        return res;
    }

    public void setPID(float P,float I,float D)
    {
        this.P = P;
        this.I = I;
        this.D = D;
        setUpdate();
    }




}
