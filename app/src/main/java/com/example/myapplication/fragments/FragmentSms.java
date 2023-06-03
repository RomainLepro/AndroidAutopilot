package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.example.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSms#newInstance} factory method to
 * create an instance of this fragment.
 */

import androidx.fragment.app.Fragment;

import com.example.myapplication.Interfaces.DataSms;

public class FragmentSms extends Fragment implements FragmentInterface{

    public DataSms m_dataSms;

    View view;

    public EditText tv_phone_self_val,tv_phone_target_val;
    public Switch sw_enableSend,sw_enableGps;

    public Button btn_send;
    public FragmentSms(DataSms dataSms) {
        m_dataSms = dataSms;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sms, container, false);

        tv_phone_self_val = view.findViewById(R.id.tv_phone_self_val);
        tv_phone_target_val = view.findViewById(R.id.tv_phone_target_val);

        sw_enableSend = view.findViewById(R.id.sw_enableSend);
        sw_enableGps = view.findViewById(R.id.sw_enableGps);

        btn_send = view.findViewById(R.id.btn_send);

        //TODO refresh rate

        return view;
    }

    @Override
    public void updateView() {
        // TODO needs switch to send data to SMS
        m_dataSms.sendingData = sw_enableSend.isChecked();
        m_dataSms.sendingGps = sw_enableGps.isChecked();
        if(btn_send.isPressed()){ // should be done via a call back function insted of this
            m_dataSms.sendingPing = true;
        }
        m_dataSms.fromNumber = tv_phone_self_val.getText().toString();
        m_dataSms.toNumber = tv_phone_target_val.getText().toString();
    }
}