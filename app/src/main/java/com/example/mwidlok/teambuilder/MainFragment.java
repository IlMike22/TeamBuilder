package com.example.mwidlok.teambuilder;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mwidlok.teambuilder.Adapters.RvEventsAdapter;
import com.example.mwidlok.teambuilder.Model.Team;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private boolean isFirstRun = false;
    final private Activity activity = getActivity();
    final private List<String> dataSet = new ArrayList<>();
    private static final int REQUEST_CODE_EVENT_NAME_SET = 1;
    static final String newEventCode = Integer.toString(REQUEST_CODE_EVENT_NAME_SET);

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabNewEvent);
        TextView tvNoEventAvailable = (TextView) view.findViewById(R.id.tvNoEventsMsg);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment createEventFragment = new CreateEventFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.llyt_container, createEventFragment, "com.example.mwidlok.teambuilder.CreateEventFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.rvEventsView);
        mRecyclerView.setHasFixedSize(true);

        // setting Layout Manager for Recycler View
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new RvEventsAdapter(dataSet);
        mRecyclerView.setAdapter(mAdapter);

        if (!isFirstRun)
        {
            isFirstRun = true;
            Realm myDb = getRealmInstance();
            String TAG = "TeamBuilder";
            Log.i(TAG, "Realm: Reading all events from db..");
            RealmResults<Team> allEvents = myDb.where(Team.class).findAll();
            for (Team currentTeam : allEvents) {
                Log.i(TAG, "Realm: Found a team dataset named " + currentTeam.getName());
                dataSet.add(currentTeam.getName());
            }
        }

        // checking if there is already at least one event created. if not, show message
        if (dataSet.size() == 0)
            tvNoEventAvailable.setVisibility(View.VISIBLE);
        else
            tvNoEventAvailable.setVisibility(View.GONE);
    }

    private Realm getRealmInstance() {
        Realm.init(getContext());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("myRealmDatabase.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        return Realm.getInstance(realmConfig);
    }

}
