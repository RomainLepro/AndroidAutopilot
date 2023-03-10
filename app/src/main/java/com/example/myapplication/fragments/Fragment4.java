package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.listValue.PidValue;
import com.example.myapplication.listValue.PidValueAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment4#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment4 extends Fragment {

    View view;

    ListView simpleList;
    Button btn_update;

    String countryList[] = {"OX","OY","OZ","TH"};

    ArrayList<PidValue> arrayPidValues;
    TableLayout tableLayout;




    public Fragment4() {
        // Required empty public constructor
        arrayPidValues = new ArrayList<PidValue>();

        arrayPidValues.add(new PidValue("PIDX: "));
        arrayPidValues.add(new PidValue("PIDY: "));
        arrayPidValues.add(new PidValue("PIDZ: "));
        //--arrayPidValues.add(new PidValue("PID_T"));     
    }

    public static Fragment4 newInstance(String param1, String param2) {
        Fragment4 fragment = new Fragment4();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Fragment4","onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_4, container, false);

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

    //called to update the veiw from main activity
    public void updateView(float[] results)
    {
        for(int i = 0;i<results.length;i++)
        {
            arrayPidValues.get(i).PIDresult = results[i];
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

}