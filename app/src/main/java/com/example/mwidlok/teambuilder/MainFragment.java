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

    Activity activity = getActivity();
    private List<String> dataSet = new ArrayList<String>();
    private FloatingActionButton fab;
    static final int REQUEST_CODE_EVENT_NAME_SET = 1;
    static final String newEventCode = Integer.toString(REQUEST_CODE_EVENT_NAME_SET);

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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



        fab = (FloatingActionButton) view.findViewById(R.id.fabNewEvent);

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

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvEventsView);
        mRecyclerView.setHasFixedSize(true);

        // setting Layout Manager for Recycler View
        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RvEventsAdapter(dataSet);
        mRecyclerView.setAdapter(mAdapter);

        Realm myDb = getRealmInstance();
        Log.i("TeamBuilder", "Realm: Reading all persons from db..");

        RealmResults<Team> allTeams = myDb.where(Team.class).findAll();

        for (Team currentTeam : allTeams) {
            Log.i("TeamBuilder", "Realm: Found a team dataset named " + currentTeam.getName());
            dataSet.add(currentTeam.getName());
        }

        // retrieve data

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String eventName = bundle.getString(newEventCode, null);
            if (eventName != null) {
                //todo: a new event was created. add this event to list and make it clickable.
                Log.i("TeamBuilder", "New event retrieved named " + eventName);
                dataSet.add(eventName);
                mAdapter.notifyDataSetChanged();
            }

        }

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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE_EVENT_NAME_SET) {
//            String result;
//            if (data != null) {
//                result = data.getStringExtra("result");
//                dataSet.add(result);
//                mAdapter.notifyDataSetChanged();
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

}
