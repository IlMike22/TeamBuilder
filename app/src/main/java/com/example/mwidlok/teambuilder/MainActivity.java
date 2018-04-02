package com.example.mwidlok.teambuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements  CreateEventFragment.OnEventCreatedListener,
                                                                TeamListOverviewFragment.OnEventClickedForDetailViewListener

{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment mainFragment = new MainFragment();
        //todo open new fragment, not new activity. use fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.llyt_container, mainFragment, "com.example.mwidlok.teambuilder.MainFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // todo die ganzen transaction Codeschnipsel kann man zusammenfassen


    @Override
    public void updateListAfterEventCreated(String eventName) {
        // a new event was created. open main fragment and show new item in list..

        Log.i("TeamBuilder", "New event called " + eventName);

        MainFragment mainFragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(MainFragment.newEventCode, eventName);
        mainFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.llyt_container, mainFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void openEventDetailView(int eventId) {
        // open detail view for event with given event id..
        TeamListOverviewFragment eventDetailFragment = new TeamListOverviewFragment();
        Bundle args = new Bundle();
        args.putInt("eventId",eventId);
        eventDetailFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.llyt_container,eventDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void openNewPersonView(int eventId) {
        CreatePersonFragment createPersonFragment = new CreatePersonFragment();
        Bundle args = new Bundle();
        args.putInt("eventId", eventId);
        createPersonFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.llyt_container, createPersonFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void openTeamResultView(int eventId) {

        TeamResultFragment resultFragment = new TeamResultFragment();
        Bundle args = new Bundle();
        args.putInt("eventId",eventId);
        resultFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.llyt_container, resultFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}



