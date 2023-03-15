package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.myapplication.R;
import com.example.myapplication.listValue.PidValue;
import com.example.myapplication.palne.PidInterface;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPID#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPID extends Fragment {

    View view;

    ListView simpleList;
    Button btn_update;


    //all this is to be removed
    ArrayList<PidValue> arrayPidValues;
    TableLayout tableLayout;


    //this should remove the rest

    PidInterface m_pidInterface;

    public FragmentPID() {

        m_pidInterface = new PidInterface();

        arrayPidValues = new ArrayList<PidValue>();
        arrayPidValues.add(new PidValue(m_pidInterface.pidNameList[0]));
        arrayPidValues.add(new PidValue(m_pidInterface.pidNameList[1]));
        arrayPidValues.add(new PidValue(m_pidInterface.pidNameList[2]));
    }

    public FragmentPID(PidInterface pidInterface) {

        m_pidInterface = pidInterface;

        arrayPidValues = new ArrayList<PidValue>();
        arrayPidValues.add(new PidValue(m_pidInterface.pidNameList[0],pidInterface.outputPids[0]));
        arrayPidValues.add(new PidValue(m_pidInterface.pidNameList[1],pidInterface.outputPids[1]));
        arrayPidValues.add(new PidValue(m_pidInterface.pidNameList[2],pidInterface.outputPids[2]));
    }

    public static FragmentPID newInstance() {
        FragmentPID fragment = new FragmentPID();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("FragmentPID","onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_pid, container, false);

        tableLayout = (TableLayout) view.findViewById(R.id.tableLayout);
        for(int i =0;i<3;i++)
        {
            TableRow row = (TableRow) tableLayout.getChildAt(i*4);
            TableRow rowP = (TableRow) tableLayout.getChildAt(i*4+1);
            TableRow rowI = (TableRow) tableLayout.getChildAt(i*4+2);
            TableRow rowD = (TableRow) tableLayout.getChildAt(i*4+3);
            arrayPidValues.get(i).init(row,rowP,rowI,rowD);
        }

        return view;
    }

    private PidValue getPid(String pidName)
    {
        for(int i =0;i<arrayPidValues.size();i++)
        {
            if(arrayPidValues.get(i).name.equalsIgnoreCase(pidName))
            {
                return arrayPidValues.get(i);
            };
        }
        return null;
    }

    //called to update the veiw from main activity (update pid values used in plane and outputs)
    public void updateView() {
        for (int i = 0; i < m_pidInterface.pidCount; i++) {
            arrayPidValues.get(i).PIDresult = m_pidInterface.resultsPids[i];
            m_pidInterface.outputPids = getValues();
            arrayPidValues.get(i).update();
        }
    }

    private float[][] getValues()
    {
        float[][] output = {null,null,null};
        for(int i =0;i<arrayPidValues.size();i++)
        {
            output[i] =   arrayPidValues.get(i).getPID();
        }
        return output;
    }

}