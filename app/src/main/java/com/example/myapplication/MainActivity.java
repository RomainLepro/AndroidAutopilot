package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    Fragment fragment1,fragment2,fragment3;
    FragmentTransaction ft;



    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        invalidateOptionsMenu();

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Main Page");
        toolbar.setSubtitle("Test Subtitle");
        toolbar.inflateMenu(R.menu.option_menu);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayout,fragment1);
        ft.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item1:
                Toast.makeText(getApplicationContext(),"ITEM 1",Toast.LENGTH_SHORT).show();

                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayout,fragment1);
                ft.commit();
                break;
            case R.id.item2:
                Toast.makeText(getApplicationContext(),"ITEM 2",Toast.LENGTH_SHORT).show();

                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayout,fragment2);
                ft.commit();
                break;
            case R.id.item3:
                Toast.makeText(getApplicationContext(),"ITEM 3",Toast.LENGTH_SHORT).show();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayout,fragment3);
                ft.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}