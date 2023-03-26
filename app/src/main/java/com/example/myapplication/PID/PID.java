package com.example.myapplication.PID;

import android.util.Log;

public class PID {
    static int MAX_DT_MS = 100;
    static int MIN_DT_MS = 1;
    static int MAX_I = 100;
    static int MAX_D = 100;

    public float P,I,D;
    public int prev_t_ms;

    private float previous_position = 0;
    private float integral = 0;

    public float output;

    public PID(float P_,float I_,float D_){
        prev_t_ms = 0;
        P = P_;I = I_;D = D_;
    }

    public float update(float position,float goal,int t_ms)
    {
        int dt_ms = (t_ms-prev_t_ms);
        return updateDt(position,goal,dt_ms);
    }

    public float updateDt(float position,float goal,int dt_ms)
    {
        goal-=500;
        float result = 0;
        float dt = (float)(dt_ms)/1000;
        float derivative = (position - previous_position)/dt;
        previous_position = position;
        integral += (goal - position)*dt;

        if(integral>MAX_I)integral=MAX_I;
        if(integral<-MAX_I)integral=-MAX_I;

        if(derivative>MAX_D)derivative=MAX_D;
        if(derivative<-MAX_D)derivative=-MAX_D;

        result += (goal - position) * P + derivative * D + integral * I;
        output = result;
        return result;
    }

    public void updateGains(float[] value) {
        if(value.length!=3)
        {
            Log.e("Error","not right size : "+Integer.toString(value.length));
            return;
        }
        P = value[0];
        I = value[1];
        D = value[2];
    }
}
