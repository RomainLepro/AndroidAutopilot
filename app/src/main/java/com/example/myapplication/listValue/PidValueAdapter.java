package com.example.myapplication.listValue;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PidValueAdapter extends ArrayAdapter<PidValue> {

    View view;
    ViewGroup viewGroup;

    public PidValueAdapter(Context context, ArrayList<PidValue> pidValues) {
        super(context, 0, pidValues);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PidValue pidValue = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.content, parent, false);
        }
        // Lookup view for data population
        pidValue.tv_pid = (TextView) convertView.findViewById(R.id.tv_pid);
        pidValue.tv_p_result = (TextView) convertView.findViewById(R.id.tv_p_result);
        pidValue.tv_i_result = (TextView) convertView.findViewById(R.id.tv_i_result);
        pidValue.tv_d_result = (TextView) convertView.findViewById(R.id.tv_d_result);
        pidValue.tv_pid_result = (TextView) convertView.findViewById(R.id.tv_pid_result);
        pidValue.tv_p = (EditText) convertView.findViewById(R.id.tv_p_value);
        //pidValue.tv_i = (TextView) convertView.findViewById(R.id.tv_i);
        //pidValue.tv_d = (TextView) convertView.findViewById(R.id.tv_d);



        // Populate the data into the template view using the data object
        pidValue.tv_pid.setText(pidValue.name);
        pidValue.tv_p_result.setText(Float.toString(pidValue.Presult));
        pidValue.tv_i_result.setText(Float.toString(pidValue.Iresult));
        pidValue.tv_d_result.setText(Float.toString(pidValue.Dresult));
        pidValue.tv_pid_result.setText(Float.toString(pidValue.PIDresult));
        pidValue.tv_p.setText(Float.toString(pidValue.P));
        // Return the completed view to render on screen

        Log.i("CREATE","CREATING VIEW ");


        return convertView;
    }
    

    public void updateViews()
    {
        for (int i =0;i<getCount();i++) {
            PidValue pidValue = getItem(i);
            // Populate the data into the template view using the data object
            pidValue.tv_pid.setText(pidValue.name);
            pidValue.tv_p_result.setText(Float.toString(pidValue.Presult));
            pidValue.tv_i_result.setText(Float.toString(pidValue.Iresult));
            pidValue.tv_d_result.setText(Float.toString(pidValue.Dresult));
            pidValue.tv_pid_result.setText(Float.toString(pidValue.PIDresult));
        }
    }


}