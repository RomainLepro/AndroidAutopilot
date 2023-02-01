package com.example.myapplication.listValue;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PidValueAdapter extends ArrayAdapter<PidValue> {

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
        TextView tv_pid = (TextView) convertView.findViewById(R.id.tv_pid);
        TextView tv_p_result = (TextView) convertView.findViewById(R.id.tv_p_result);
        TextView tv_i_result = (TextView) convertView.findViewById(R.id.tv_i_result);
        TextView tv_d_result = (TextView) convertView.findViewById(R.id.tv_d_result);
        TextView tv_pid_result = (TextView) convertView.findViewById(R.id.tv_pid_result);




        // Populate the data into the template view using the data object
        tv_pid.setText(pidValue.name);
        tv_p_result.setText(Float.toString(pidValue.Presult));
        tv_i_result.setText(Float.toString(pidValue.Iresult));
        tv_d_result.setText(Float.toString(pidValue.Dresult));
        tv_pid_result.setText(Float.toString(pidValue.PIDresult));
        // Return the completed view to render on screen

        return convertView;
    }

}
