package com.example.mwidlok.teambuilder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment mainFragment = new MainFragment();
        //todo open new fragment, not new activity. use fragment manager
        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.llyt_container,mainFragment,"com.example.mwidlok.teambuilder.MainFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
