package com.example.myapplication.PID;

import android.util.Log;

public class PID {
    static float  MAX_DT_MS = 100;
    static float MIN_DT_MS = 1;
    static float MAX_I = 300;

    static float MAX_INTEGRAL = 300;
    static float  MAX_D = 300;

    static float  MAX_P = 1000;
    static float filter = 0.2f;

    public float P,I,D;
    public int prev_t_ms;

    private float previous_position = 0;
    private float integral = 0;
    private float derivative = 0;

    private float proportional = 0;

    public float outputs[] = {0,0,0};
    public float output = 0;

    public PID(float P_,float I_,float D_){
        prev_t_ms = 0;
        P = P_;I = I_;D = D_;
    }

    public float update(float position,float goal,float t_ms)
    {
        float dt_ms = (t_ms-prev_t_ms);
        return updateDt(position,goal,dt_ms);
    }

    public float updateDt(float position,float goal,float dt_ms)
    {
        goal-=500;
        float result = 0;
        float dt_s = dt_ms/1000;
        derivative = derivative * (1-filter) + (position - previous_position)/dt_s * filter;
        previous_position = position;
        integral += (goal - position)*dt_s;

        if(integral>MAX_INTEGRAL) integral=MAX_INTEGRAL;
        if(integral<-MAX_INTEGRAL)integral=-MAX_INTEGRAL;

        proportional = (goal - position);

        outputs[0]=proportional * P;
        outputs[1]=integral * I;
        outputs[2]=derivative * D;

        // Limit gain values

        if(outputs[0]>MAX_P) outputs[0]=MAX_P;
        if(outputs[0]<-MAX_P)outputs[0]=-MAX_P;

        if(outputs[1]>MAX_I) outputs[1]=MAX_I;
        if(outputs[1]<-MAX_I)outputs[1]=-MAX_I;

        if(outputs[2]>MAX_D) outputs[2]=MAX_D;
        if(outputs[2]<-MAX_D)outputs[2]=-MAX_D;

        result += outputs[0]  + outputs[1] + outputs[2];
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
