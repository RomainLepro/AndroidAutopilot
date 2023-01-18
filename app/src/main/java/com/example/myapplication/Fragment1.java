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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tv_logger,tv_send;
    Button btn_connect,btn_send;

    Handler handler;


    final Runnable task = new Runnable() {
        public void run() {
            AndroidCommunication activity = (AndroidCommunication)getActivity();
            if(activity!=null)
            {
                tv_logger.setText(activity.getLogger());
                Log.i("Loop","looping");
                handler.postDelayed(this, 100);
                /*
                if(!activity.isConnected())
                {
                    activity.startUsb();
                }
                */
            }
        }
    };


    public Fragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment1 newInstance(String param1, String param2) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.i("Fragment1","onCreate");
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(task,1000);
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

}