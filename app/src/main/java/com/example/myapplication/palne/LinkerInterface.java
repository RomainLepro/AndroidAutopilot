package com.example.myapplication.palne;




public class LinkerInterface {
    //WARNNING not using getter or setters
    public int numRows = 12;
    public int numCols = 8;
    public float[][] matrixLinker;

    public LinkerInterface() {
        matrixLinker = new float[numRows][numCols];
        initialiseMatrix();
    }

    public LinkerInterface(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        matrixLinker = new float[numRows][numCols];
        initialiseMatrix();
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public float[][] getMatrixLinker() {
        return matrixLinker;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public void setNumCols(int numCols) {
        this.numCols = numCols;
    }

    public void setMatrixLinker(float[][] matrixLinker) {
        this.matrixLinker = matrixLinker;
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
}
