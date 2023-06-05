package com.example.myapplication.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.myapplication.Interfaces.DataMacroData;
import com.example.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMacroData#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMacroData extends Fragment implements FragmentInterface {
    //TODO conect macro data
    DataMacroData m_interfaceMacroData;

    GridLayout gridLayout;

    Button btn_reset;

    public FragmentMacroData() {
        m_interfaceMacroData = new DataMacroData();
    }

    public FragmentMacroData(DataMacroData interfaceMacroData) {
        m_interfaceMacroData = interfaceMacroData;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_macro_data, container, false);
        gridLayout = view.findViewById(R.id.grid_layout);


        gridLayout.setRowCount(m_interfaceMacroData.m_dataList.size());
        gridLayout.setColumnCount(3);

        TextView textViewName;
        TextView textViewData;
        TextView textViewUnit;

        for (int i = 0; i < m_interfaceMacroData.m_dataList.size(); i++) {

            textViewName = new TextView(getContext());
            textViewData = new TextView(getContext());
            textViewUnit = new TextView(getContext());
            GridLayout.LayoutParams paramName = new GridLayout.LayoutParams(GridLayout.spec(i),GridLayout.spec(0));
            GridLayout.LayoutParams paramData = new GridLayout.LayoutParams(GridLayout.spec(i),GridLayout.spec(1));
            GridLayout.LayoutParams paramUnit = new GridLayout.LayoutParams(GridLayout.spec(i),GridLayout.spec(2));

            textViewName.setWidth(300);
            textViewData.setWidth(250);
            textViewUnit.setWidth(100);

            gridLayout.addView(textViewName, paramName);
            gridLayout.addView(textViewData, paramData);
            gridLayout.addView(textViewUnit, paramUnit);
        }

        btn_reset = view.findViewById(R.id.btn_reset);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_interfaceMacroData.resetRequest = true;
            }
        });


        updateView();

        return view;
    }


    @Override
    public void updateView() {

        for (int i = 0; i < m_interfaceMacroData.m_dataList.size(); i++) {
            View name = gridLayout.getChildAt( i * 3 + 0);
            View data = gridLayout.getChildAt( i * 3 + 1);
            View unit = gridLayout.getChildAt( i * 3 + 2);
            ((TextView)name).setText( (m_interfaceMacroData.m_dataList.get(i).DataName));
            ((TextView)data).setText( Float.toString(m_interfaceMacroData.m_dataList.get(i).DataValue));
            ((TextView)unit).setText( (m_interfaceMacroData.m_dataList.get(i).DataUnit));
        }
    }
}