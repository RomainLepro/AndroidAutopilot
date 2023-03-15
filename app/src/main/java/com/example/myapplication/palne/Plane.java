package com.example.myapplication.palne;

public class Plane {
    PID PIDX,PIDY,PIDZ;

    public LinkerInterface linkerInterface;

    public LinkerInterface linkerInterfaceA;
    public LinkerInterface linkerInterfaceB;
    public LinkerInterface linkerInterfaceC;
    public PidInterface pidInterface;

    public float[] L_val_radio;
    public int[] L_val_radio_int;
    public float[]orientationAngles;
    public Plane()
    {

        linkerInterfaceA = new LinkerInterface();
        linkerInterfaceB = new LinkerInterface();
        linkerInterfaceC = new LinkerInterface();

        linkerInterface = linkerInterfaceA;

        pidInterface = new PidInterface();

        PIDX = new PID(2,0.1f,0.1f);
        PIDY = new PID(2,0.1f,0.1f);
        PIDZ = new PID(2,0.1f,0.1f);

        orientationAngles = new float[]{0.f,0.f,0.f};
        L_val_radio = new float[]{0.f,0.f,0.f,0.f};

    }
    public void updateDt(int dt_ms){
        PIDX.updateDt(orientationAngles[2]*400,L_val_radio[0],dt_ms);
        PIDY.updateDt(orientationAngles[1]*400,L_val_radio[1],dt_ms);
        PIDZ.updateDt(orientationAngles[0]*400,L_val_radio[2],dt_ms);

        updateLinkerInputsAndOutputs();
    }

    public void updatePidResults() {
        pidInterface.resultsPids[0] = PIDX.output;
        pidInterface.resultsPids[1] = PIDY.output;
        pidInterface.resultsPids[2] = PIDZ.output;

    }

    public int[] getResultsInt() {
        //TODO use linker here

        int[] L = L_val_radio_int.clone();
        L[0] = format(PIDX.output);
        L[1] = format(PIDY.output);
        L[2] = format(PIDZ.output);
        L[3] = L_val_radio_int[3];
        L[4] = L_val_radio_int[3];
        return L;
    }

    private int format(float output) {
        int maxValue = 500;
        if (output<=-maxValue)return 500-maxValue;
        if(output>=maxValue)return 500+maxValue;
        return (int)output+500;
    }

    public void updatePIDGains() {
        PIDX.updateGains(pidInterface.outputPids[0]);
        PIDY.updateGains(pidInterface.outputPids[1]);
        PIDZ.updateGains(pidInterface.outputPids[2]);
    }

    void switchLinkerInterface(int num){
        if(num==0)linkerInterface = linkerInterfaceA;
        else if(num==1)linkerInterface = linkerInterfaceB;
        else if(num==2)linkerInterface = linkerInterfaceC;
    }



    public void updateLinkerInputsAndOutputs() {
        linkerInterface.inputLinker[0] = PIDX.output;
        linkerInterface.inputLinker[1] = PIDY.output;
        linkerInterface.inputLinker[2] = PIDZ.output;
        linkerInterface.inputLinker[3] = L_val_radio[0];
        linkerInterface.inputLinker[4] = L_val_radio[1];
        linkerInterface.inputLinker[5] = L_val_radio[2];
        linkerInterface.inputLinker[6] = L_val_radio[3];
        linkerInterface.inputLinker[7] = L_val_radio[4];
        linkerInterface.inputLinker[8] = L_val_radio[5];
        linkerInterface.inputLinker[9] = L_val_radio[6];

        linkerInterface.updateOutputs();
    }

}
