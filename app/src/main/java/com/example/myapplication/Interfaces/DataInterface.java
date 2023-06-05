package com.example.myapplication.Interfaces;

public interface DataInterface {

    void saveData();

    void reset();
    void loadData();
    void loadData(String dataName);

    //TODO may be complication to get sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

}
