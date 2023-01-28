package com.example.myapplication;

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
        float result = 0;
        float dt = (float)(t_ms-prev_t_ms)/1000;
        float derivative = (position - previous_position)/dt;
        integral += (goal - position)*dt;

        if(integral>MAX_I)integral=MAX_I;
        if(integral<-MAX_I)integral=-MAX_I;

        if(derivative>MAX_D)derivative=MAX_D;
        if(derivative<-MAX_D)derivative=-MAX_D;

        result += (goal - position) * P + derivative * D + integral * I;

        return result;
    }
}
