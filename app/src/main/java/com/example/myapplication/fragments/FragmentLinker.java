package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;

import com.example.myapplication.R;
import com.example.myapplication.palne.LinkerInterface;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentLinker#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLinker extends Fragment {


    LinkerInterface m_linker;
    GridLayout gridLayout;


    public FragmentLinker() {
        // Required empty public constructor
        m_linker = new LinkerInterface();
    }

    public FragmentLinker(LinkerInterface linker) {
        // if a linker is provided, do not initialize the matrix
        m_linker = linker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void updateValues()
    {
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
            if(viewText instanceof EditText) {
                ((EditText) viewText).setText(String.format("%4.1f", m_linker.outputLinker[i - 2]));
                ((EditText) viewText).setWidth(160);
            }
        }
        i=0;j=0;
        for (j = 2; j < m_linker.numCols+2; j++) {
            int index = i * (m_linker.numCols+2) + j;
            View viewText = gridLayout.getChildAt(index);
            if(viewText instanceof EditText)
            {
                ((EditText) viewText).setText(String.format("%4.1f",m_linker.inputLinker[j-2]));
                ((EditText) viewText).setWidth(160);
            }
        }

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

        gridLayout.setRowCount(m_linker.numRows+2);
        gridLayout.setColumnCount(m_linker.numCols+2);

        for (int i = 0; i < m_linker.numRows+2; i++) {
            for (int j = 0; j < m_linker.numCols+2; j++) {
                EditText editText = new EditText(getContext());
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED );

                if(i==1 && j!=0){
                    editText.setText("I:"+Integer.toString(j-1));
                }
                else if(j==1 && i!=0){
                    editText.setText("O:"+Integer.toString(i-1));
                }
                else if(i>=2 && j>=2) {
                    editText.setText(Float.toString(m_linker.matrixLinker[i-2][j-2]));
                }
                else{
                    editText.setText("");
                }
                if(i==1 && j==1) {
                    editText.setText("XX");
                }


                GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(i),GridLayout.spec(j));
                gridLayout.addView(editText, params);
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