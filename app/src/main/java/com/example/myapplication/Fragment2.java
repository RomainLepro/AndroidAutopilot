package com.example.myapplication;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment2 extends Fragment {

    TextView tv_ax,tv_ay,tv_az,tv_gx,tv_gy,tv_gz;
    TextView tv_Rx,tv_Ry,tv_Rz,tv_Th,tv_Sa,tv_Sb,tv_He,tv_Te;

    Button btn_update;

    View view;

    public Fragment2() {
        // Required empty public constructor
    }

    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Fragment2","onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_2, container, false);

        tv_ax = view.findViewById(R.id.tv_ax);
        tv_ay = view.findViewById(R.id.tv_ay);
        tv_az = view.findViewById(R.id.tv_az);

        tv_gx = view.findViewById(R.id.tv_gx);
        tv_gy = view.findViewById(R.id.tv_gy);
        tv_gz = view.findViewById(R.id.tv_gz);

        tv_Rx = view.findViewById(R.id.tv_Rx);
        tv_Ry = view.findViewById(R.id.tv_Ry);
        tv_Rz = view.findViewById(R.id.tv_Rz);

        tv_Th = view.findViewById(R.id.tv_Th);
        tv_Sa = view.findViewById(R.id.tv_Sa);
        tv_Sb = view.findViewById(R.id.tv_Sb);
        tv_He = view.findViewById(R.id.tv_He);
        tv_Te = view.findViewById(R.id.tv_Te);

        btn_update = view.findViewById(R.id.btn_update);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Update","update");
            }
        });

        Log.i("CREATE_SENSOR","creating activity sensor");

        return view;
    }

    //called to update the veiw from main activity
    public void updateView(float[] accelerometerReading,float[] orientationAngles)
    {
        tv_ax.setText(String.valueOf(accelerometerReading[0]));
        tv_ay.setText(String.valueOf(accelerometerReading[1]));
        tv_az.setText(String.valueOf(accelerometerReading[2]));

        tv_gx.setText(String.valueOf(orientationAngles[0]));
        tv_gy.setText(String.valueOf(orientationAngles[1]));
        tv_gz.setText(String.valueOf(orientationAngles[2]));
    }

    public void updateView(float[] accelerometerReading,float[] orientationAngles,int[] radioListInputs)
    {
        updateView(accelerometerReading,orientationAngles);

        tv_Rx.setText(String.valueOf(radioListInputs[0]));
        tv_Ry.setText(String.valueOf(radioListInputs[1]));
        tv_Rz.setText(String.valueOf(radioListInputs[2]));
        tv_Th.setText(String.valueOf(radioListInputs[3]));
        tv_Sa.setText(String.valueOf(radioListInputs[4]));
        tv_Sb.setText(String.valueOf(radioListInputs[5]));
        tv_He.setText(String.valueOf(radioListInputs[6]));
        tv_Te.setText(String.valueOf(radioListInputs[7]));
    }
}