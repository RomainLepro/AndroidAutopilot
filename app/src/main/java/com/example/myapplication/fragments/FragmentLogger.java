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

    TextView tv_logger_in,tv_logger_out,tv_send,tv_debug;
    Button btn_connect,btn_send,btn_debug;

    public DataLogger m_interfaceLogger;

    public FragmentLogger(DataLogger interfaceLogger) {
        // Required empty public constructor
        m_interfaceLogger = interfaceLogger;
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

        tv_logger_in = view.findViewById(R.id.tv_logger_in);
        tv_logger_in.setSelected(true);
        tv_logger_out = view.findViewById(R.id.tv_logger_out);
        tv_debug = view.findViewById(R.id.tv_debug);
        //tv__debug.setSelected(true);

        tv_send = view.findViewById(R.id.tv_send);

        btn_connect = view.findViewById(R.id.btn_connect);
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("btn_connect","btn_connect");
                m_interfaceLogger.requestConnection = true;
                tv_logger_in.setText(m_interfaceLogger.logger_in);
            }
        });

        btn_send = view.findViewById(R.id.btn_send);
        /*
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("btn_send","btn_send");
                String message = tv_send.getText().toString() + '\n';
                MainActivity activity = (MainActivity)getActivity();
                activity.send(message);
                tv_logger_in.setText(activity.getLogger_in());
            }
        });
        */
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

    public void updateView()
    {
        tv_debug.setText(m_interfaceLogger.debug);
        tv_logger_in.setText(m_interfaceLogger.logger_in);
        tv_logger_out.setText(m_interfaceLogger.logger_out);
    }



}