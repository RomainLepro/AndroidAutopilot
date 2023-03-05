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
    String[] pidNameList =  {"PIDX","PIDY","PIDZ" } ;


    //this should remove the rest

    PidInterface m_pidInterface;

    public FragmentPID() {
        // Required empty public constructor
        arrayPidValues = new ArrayList<PidValue>();

        arrayPidValues.add(new PidValue(pidNameList[0]));
        arrayPidValues.add(new PidValue(pidNameList[1]));
        arrayPidValues.add(new PidValue(pidNameList[2]));


        m_pidInterface = new PidInterface();
    }

    public FragmentPID(PidInterface pidInterface) {
        // This constructor should be called to allow plane and PID fragment to comunicate
        m_pidInterface = new PidInterface();
    }

    public static FragmentPID newInstance(String param1, String param2) {
        FragmentPID fragment = new FragmentPID();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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

    //called to update the veiw from main activity
    public void updateView(float[] results)
    {
        for(int i = 0;i<results.length;i++)
        {
            arrayPidValues.get(i).PIDresult = results[i];
            arrayPidValues.get(i).update();
        }

    }

    public void updateViewByName(float[] results)
    {
        assert(results.length==pidNameList.length);
        for(int i =0;i<pidNameList.length;i++)
        {
            if(getPid(pidNameList[i])!=null)
            {
                getPid(pidNameList[i]).PIDresult = results[i];
            }
            else
            {
                Log.w("getValuesByName",pidNameList[i]+" not found");
            }
        }
        for(int i = 0;i<arrayPidValues.size();i++)
        {
            arrayPidValues.get(i).update();
        }
    }

    public float[][] getValues()
    {
        float[][] output = {null,null,null};
        for(int i =0;i<arrayPidValues.size();i++)
        {
            output[i] =   arrayPidValues.get(i).getPID();
        }
        return output;
    }

    public float[][] getValuesByName()
    {
        float[][] output = {null,null,null};
        float[] vide = {0,0,0};
        for(int i =0;i<pidNameList.length;i++)
        {
            if(getPid(pidNameList[i])!=null)
            {
                output[i] =   getPid(pidNameList[i]).getPID();
            }
            else
            {
                output[i] = vide;
                Log.w("getValuesByName",pidNameList[i]+" not found");
            }

        }
        return output;
    }

}