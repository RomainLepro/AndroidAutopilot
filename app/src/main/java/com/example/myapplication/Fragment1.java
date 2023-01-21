package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment1 extends Fragment {

    TextView tv_logger,tv_send;
    Button btn_connect,btn_send;







    public Fragment1() {
        // Required empty public constructor
    }


    public static Fragment1 newInstance() {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Fragment1","onCreate");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_1, container, false);

        tv_logger = view.findViewById(R.id.tv_logger);
        tv_logger.setSelected(true);
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

        return view;
    }

    public void updateView(String logger)
    {
        tv_logger.setText(logger);
    }

}