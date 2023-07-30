package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSensor#newInstance} factory method to
 * create an instance of this fragment.
 */

;
import com.example.myapplication.Interfaces.DataGps;
import com.example.myapplication.Interfaces.DataSensors;
import com.example.myapplication.Interfaces.DataRadio;
import com.example.myapplication.R;

public class FragmentSensor extends Fragment implements FragmentInterface{
    DataSensors m_interfaceSensors;
    DataRadio m_interfaceRadio;
    DataGps m_interfaceGps;
    TextView tv_ax,tv_ay,tv_az,tv_gx,tv_gy,tv_gz,tv_magx,tv_magy,tv_magz,tv_magCorse,tv_gpsCorse,tv_corseWaypoint;
    TextView tv_Rx,tv_Ry,tv_Rz,tv_Th,tv_Sa,tv_Sb,tv_He,tv_Te;
    ImageView imv_gpsCourse, imv_relativeWaypointCourse,imv_magCorse;
    View view;

    public FragmentSensor() {
        m_interfaceSensors = new DataSensors();
    }

    public FragmentSensor(DataSensors interfaceSensors, DataRadio interfaceRadio,DataGps interfaceGps) {
        m_interfaceSensors = interfaceSensors;
        m_interfaceRadio =interfaceRadio;
        m_interfaceGps =interfaceGps;
    }

    public static FragmentSensor newInstance(String param1, String param2) {
        FragmentSensor fragment = new FragmentSensor();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("FragmentSensor","onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_sensor, container, false);

        tv_ax = view.findViewById(R.id.tv_ax);
        tv_ay = view.findViewById(R.id.tv_ay);
        tv_az = view.findViewById(R.id.tv_az);

        tv_gx = view.findViewById(R.id.tv_gx);
        tv_gy = view.findViewById(R.id.tv_gy);
        tv_gz = view.findViewById(R.id.tv_gz);

        tv_magx = view.findViewById(R.id.tv_Magx);
        tv_magy = view.findViewById(R.id.tv_Magy);
        tv_magz = view.findViewById(R.id.tv_Magz);

        tv_Rx = view.findViewById(R.id.tv_Rx);
        tv_Ry = view.findViewById(R.id.tv_Ry);
        tv_Rz = view.findViewById(R.id.tv_Rz);

        tv_Th = view.findViewById(R.id.tv_Th);
        tv_Sa = view.findViewById(R.id.tv_Sa);
        tv_Sb = view.findViewById(R.id.tv_Sb);
        tv_He = view.findViewById(R.id.tv_He);
        tv_Te = view.findViewById(R.id.tv_Te);

        tv_corseWaypoint= view.findViewById(R.id.tv_corseWaypoint);
        tv_gpsCorse     = view.findViewById(R.id.tv_gpsCorse);
        tv_magCorse     = view.findViewById(R.id.tv_magCorse);

        imv_gpsCourse = view.findViewById(R.id.imv_gpsCourse);
        imv_relativeWaypointCourse = view.findViewById(R.id.imv_relativeWaypointCourse);
        imv_magCorse = view.findViewById(R.id.imv_magCorse);

        Log.i("CREATE_SENSOR","creating activity sensor");

        return view;
    }

    public void updateView(float[] accelerometerReading,float[] orientationAngles,int[] radioListInputs)
    {
        tv_ax.setText(String.format("%.2f",accelerometerReading[0]));
        tv_ay.setText(String.format("%.2f",accelerometerReading[1]));
        tv_az.setText(String.format("%.2f",accelerometerReading[2]));

        tv_gx.setText(String.format("%.2f",radToangle(orientationAngles[0])));
        tv_gy.setText(String.format("%.2f",radToangle(orientationAngles[1])));
        tv_gz.setText(String.format("%.2f",radToangle(orientationAngles[2])));

        tv_magx.setText(String.format("%.2f",radToangle(m_interfaceSensors.magnetometerReading[0])));
        tv_magy.setText(String.format("%.2f",radToangle(m_interfaceSensors.magnetometerReading[1])));
        tv_magz.setText(String.format("%.2f",radToangle(m_interfaceSensors.magnetometerReading[2])));

        tv_corseWaypoint.setText(String.format("%.2f",(m_interfaceGps.courseToNextWaypoint_deg)));
        tv_gpsCorse.setText     (String.format("%.2f",(m_interfaceGps.currentCourse_deg)));
        tv_magCorse.setText     (String.format("%.2f",radToangle(m_interfaceSensors.orientationAngles_rad[0])));

        tv_Rx.setText(String.valueOf(radioListInputs[0]));
        tv_Ry.setText(String.valueOf(radioListInputs[1]));
        tv_Rz.setText(String.valueOf(radioListInputs[2]));
        tv_Th.setText(String.valueOf(radioListInputs[3]));
        tv_Sa.setText(String.valueOf(radioListInputs[4]));
        tv_Sb.setText(String.valueOf(radioListInputs[5]));
        tv_He.setText(String.valueOf(radioListInputs[6]));
        tv_Te.setText(String.valueOf(radioListInputs[7]));
    }

    //called to update the veiw from main activity
    public void updateView()
    {
        updateView(m_interfaceSensors.accelerometerReading,
                m_interfaceSensors.orientationAngles_rad,
                m_interfaceRadio.L_val_radio_int);
        updateArrows(-m_interfaceGps.currentCourse_deg,
                m_interfaceGps.deltaCourseToNextWaypoint_deg,-radToangle(m_interfaceSensors.orientationAngles_rad[0]));
    }

    public void updateArrows(float gpsCourse_deg,float relativeCourseWaypoint_deg,float magCorse_deg)
    {
        imv_gpsCourse.setRotation(gpsCourse_deg);
        imv_relativeWaypointCourse.setRotation(relativeCourseWaypoint_deg);
        imv_magCorse.setRotation(magCorse_deg);
    }


    public static float radToangle(float rad)
    {
        return rad * 180.f / 3.1415f;
    }
}