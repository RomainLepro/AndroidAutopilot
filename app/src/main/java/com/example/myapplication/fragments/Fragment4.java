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
    PidValueAdapter adapter;
    ArrayList<PidValue> arrayPidValues;
    ViewGroup viewGroup;


    public Fragment4() {
        // Required empty public constructor
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
        viewGroup = container;

        view = inflater.inflate(R.layout.fragment_4, container, false);

        arrayPidValues = new ArrayList<PidValue>();

        arrayPidValues.add(new PidValue("PID_X"));
        arrayPidValues.add(new PidValue("PID_Y"));
        arrayPidValues.add(new PidValue("PID_Z"));
        arrayPidValues.add(new PidValue("PID_T"));
        arrayPidValues.add(new PidValue("PID_T2"));

        adapter = new PidValueAdapter(getContext(),arrayPidValues);
        simpleList = (ListView)view.findViewById(R.id.simpleListView);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.content, R.id.tv_pid, countryList);
        simpleList.setAdapter(adapter);

        btn_update    = view.findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
    public void updateView()
    {
        float[] results = {123,456,0.789f};
        updateView(results);
    }
    //called to update the veiw from main activity
    public void updateView(float[] results)
    {
        if(arrayPidValues==null || results.length>arrayPidValues.size())
        {
            return;
        }
        for(int i =0;i<results.length;i++)
        {
            arrayPidValues.get(i).PIDresult=results[i];

        }

        adapter.updateViews();
        //simpleList.getAdapter().notifyDatasetChanged();

        //simpleList.invalidateViews();
        //synchronized(simpleList.getAdapter()){simpleList.getAdapter().notifyAll();}
    }

    public float[] getValues()
    {
        float[] output = {};
        for(int i =0;i<arrayPidValues.size();i++)
        {
            arrayPidValues.get(i);
        }
        return output;
    }

}