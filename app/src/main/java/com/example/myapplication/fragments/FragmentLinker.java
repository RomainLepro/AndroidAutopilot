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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentLinker#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLinker extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_matrixLinker = "matrixLinker";

    int numRows = 12;
    int numCols = 8;

    float[][] matrixLinker;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    GridLayout gridLayout;


    public FragmentLinker() {
        // Required empty public constructor
    }

    public static FragmentLinker newInstance(String param1, String param2) {
        FragmentLinker fragment = new FragmentLinker();
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

        matrixLinker = new float[numRows][numCols];
        initialiseMatrix();
    }

    void initialiseMatrix()
    {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if(i==j)
                {
                    matrixLinker[i][j] = 1.f;
                }
                else
                {
                    matrixLinker[i][j] = 0.f;
                }
            }
        }
    }

    void updateValues()
    {
        for (int i = 1; i < numRows+1; i++) {
            for (int j = 1; j < numCols+1; j++) {
                //int rowCount = gridLayout.getRowCount();
                //int columnCount = gridLayout.getColumnCount();
                int index = i * (numCols+1) + j;
                View viewText = gridLayout.getChildAt(index);
                if(viewText instanceof EditText)
                {
                    String str = ((EditText) viewText).getText().toString();
                    matrixLinker[i-1][j-1] = Float.parseFloat(str);
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
        return matrixLinker;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_linker, container, false);

        gridLayout = view.findViewById(R.id.grid_layout);

        gridLayout.setRowCount(numRows+1);
        gridLayout.setColumnCount(numCols+1);

        for (int i = 0; i < numRows+1; i++) {
            for (int j = 0; j < numCols+1; j++) {
                EditText editText = new EditText(getContext());
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                if(i==0 && j!=0){
                    editText.setText("In:"+Integer.toString(j));
                }
                else if(j==0 && i!=0){
                    editText.setText("Out:"+Integer.toString(i));
                }
                else if(i!=0 && j!=0) {
                    editText.setText(Float.toString(matrixLinker[i-1][j-1]));
                }


                GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(i),GridLayout.spec(j));
                gridLayout.addView(editText, params);
            }
        }
        return view;
    }
}