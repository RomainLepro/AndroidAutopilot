package com.example.myapplication.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.myapplication.Interfaces.InterfaceGps;
import com.example.myapplication.Interfaces.InterfaceLinkerSelector;
import com.example.myapplication.R;
import com.example.myapplication.Interfaces.InterfaceLinker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentLinker#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLinker extends Fragment {

    InterfaceLinkerSelector m_interfaceLinkerSelector;

    InterfaceLinker m_linker;

    GridLayout gridLayout;

    SeekBar m_seekBar;


    public FragmentLinker() {
        // Required empty public constructor
        m_interfaceLinkerSelector = new InterfaceLinkerSelector();
        m_linker = m_interfaceLinkerSelector.m_linker;
    }

    public FragmentLinker(InterfaceLinkerSelector linkerSelector) {
        // if a linker is provided, do not initialize the matrix
        m_interfaceLinkerSelector = linkerSelector;
        m_linker = m_interfaceLinkerSelector.m_linker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void updateValues()
    {
        updateLinkerInterface();
        int i=0,j=0;
        for (i = 2; i < m_linker.numRows+2; i++) {
            for (j = 2; j < m_linker.numCols+2; j++) {
                //int rowCount = gridLayout.getRowCount();
                //int columnCount = gridLayout.getColumnCount();
                int index = i * (m_linker.numCols+2) + j;
                View viewText = gridLayout.getChildAt(index);
                if(viewText instanceof EditText)
                {
                    String str = ((EditText) viewText).getText().toString();
                    m_linker.matrixLinker[i-2][j-2] = toFloat(str);
                }
            }
        }
        i=0;j=0;
        for (i = 2; i < m_linker.numRows+2; i++) {
            int index = i * (m_linker.numCols+2) + j;
            View viewText = gridLayout.getChildAt(index);
            if(viewText instanceof TextView) {
                ((TextView) viewText).setText(String.format("%4.1f", m_linker.outputLinker[i - 2]));
            }
        }
        i=0;j=0;
        for (j = 2; j < m_linker.numCols+2; j++) {
            int index = i * (m_linker.numCols+2) + j;
            View viewText = gridLayout.getChildAt(index);
            if(viewText instanceof TextView)
            {
                ((TextView) viewText).setText(String.format("%4.1f",m_linker.inputLinker[j-2]));
            }
        }

    }

    private void updateLinkerInterface() {

        m_linker = m_interfaceLinkerSelector.m_linker;
    }

    public float[][] getValues()
    {
        updateValues();
        return m_linker.matrixLinker;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_linker, container, false);

        gridLayout = view.findViewById(R.id.grid_layout);

        m_seekBar = (SeekBar) view.findViewById(R.id.seekBar); // initiate the Seek bar

        gridLayout.setRowCount(m_linker.numRows+2);
        gridLayout.setColumnCount(m_linker.numCols+2);

        EditText editText;
        TextView textView;

        for (int i = 0; i < m_linker.numRows+2; i++) {
            for (int j = 0; j < m_linker.numCols+2; j++) {


                editText = new EditText(getContext());
                textView = new TextView(getContext());
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED );

                GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(i),GridLayout.spec(j));

                if(i==1 && j!=0){
                    textView.setText("I:"+Integer.toString(j-1));
                }
                else if(j==1 && i!=0){
                    textView.setText("O:"+Integer.toString(i-1));
                }
                else if(i>=2 && j>=2) {
                    editText.setText(Float.toString(m_linker.matrixLinker[i-2][j-2]));
                    if(i==j)editText.setBackgroundColor(Color.parseColor("#A00000")); // RRGGBB
                }
                else{
                    textView.setText("");
                }

                if(i==1 && j==1) {
                    textView.setText("XX");
                }

                if(i>=2 && j>=2){
                    editText.setWidth(160);
                    gridLayout.addView(editText, params);
                }
                else {
                    textView.setBackgroundColor(Color.parseColor("#00A000"));
                    textView.setWidth(160);
                    gridLayout.addView(textView, params);
                }

            }
        }
        return view;
    }

    float toFloat(String val)
    {
        if(val==null || val=="" ||val.isEmpty())  return 0.f;
        return  Float.parseFloat(val);
    }
}