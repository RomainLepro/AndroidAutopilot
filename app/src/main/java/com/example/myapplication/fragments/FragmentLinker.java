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
        for (int i = 1; i < m_linker.numRows+1; i++) {
            for (int j = 1; j < m_linker.numCols+1; j++) {
                //int rowCount = gridLayout.getRowCount();
                //int columnCount = gridLayout.getColumnCount();
                int index = i * (m_linker.numCols+1) + j;
                View viewText = gridLayout.getChildAt(index);
                if(viewText instanceof EditText)
                {
                    String str = ((EditText) viewText).getText().toString();
                    m_linker.matrixLinker[i-1][j-1] = Float.parseFloat(str);
                }
            }
        }
    }

    void updateInput()
    {

    }

    void updateOutput()
    {

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

        gridLayout.setRowCount(m_linker.numRows+1);
        gridLayout.setColumnCount(m_linker.numCols+1);

        for (int i = 0; i < m_linker.numRows+1; i++) {
            for (int j = 0; j < m_linker.numCols+1; j++) {
                EditText editText = new EditText(getContext());
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                if(i==0 && j!=0){
                    editText.setText("In:"+Integer.toString(j));
                }
                else if(j==0 && i!=0){
                    editText.setText("Out:"+Integer.toString(i));
                }
                else if(i!=0 && j!=0) {
                    editText.setText(Float.toString(m_linker.matrixLinker[i-1][j-1]));
                }


                GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(i),GridLayout.spec(j));
                gridLayout.addView(editText, params);
            }
        }
        return view;
    }
}