package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.Interfaces.DataLogger;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentLogger#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLogger extends Fragment implements FragmentInterface {

    TextView tv_logger,tv_send,tv_debug;
    Button btn_connect,btn_send,btn_debug;

    DataLogger m_interfaceLogger;


    public FragmentLogger() {
        // Required empty public constructor
    }


    public static FragmentLogger newInstance() {
        FragmentLogger fragment = new FragmentLogger();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("FragmentLogger","onCreate");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logger, container, false);

        tv_logger = view.findViewById(R.id.tv_logger);
        tv_logger.setSelected(true);
        tv_debug = view.findViewById(R.id.tv_debug);
        //tv__debug.setSelected(true);

        tv_send = view.findViewById(R.id.tv_send);

        btn_connect = view.findViewById(R.id.btn_connect);
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("btn_connect","btn_connect");
                MainActivity activity = (MainActivity)getActivity();
                activity.startUsb();
                tv_logger.setText(activity.getLogger());
            }
        });

        btn_send = view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("btn_send","btn_send");
                String message = tv_send.getText().toString() + '\n';
                MainActivity activity = (MainActivity)getActivity();
                activity.send(message);
                tv_logger.setText(activity.getLogger());
            }
        });

        btn_debug = view.findViewById(R.id.btn_debug);
        btn_debug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("btn_debug","btn_debug");
                String message = tv_send.getText().toString() + '\n';
                MainActivity activity = (MainActivity)getActivity();
            }
        });

        return view;
    }

    public void updateView(String logger)
    {
        tv_logger.setText(logger);
    }
    public void updateView(String logger,String debug)
    {
        updateView(logger);
        tv_debug.setText(debug);
    }
    public void updateView()
    {
        tv_debug.setText(m_interfaceLogger.debug);
        tv_logger.setText(m_interfaceLogger.logger);
    }



}