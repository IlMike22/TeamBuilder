package com.example.mwidlok.teambuilder;


<<<<<<< HEAD
import android.os.Bundle;
import android.support.v4.app.Fragment;
=======
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
>>>>>>> 4fa86b2b9cd7385042bc2bcc0f84e44ccddbd869
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

<<<<<<< HEAD
=======
import com.example.mwidlok.teambuilder.Adapters.RvEventsAdapter;
import com.example.mwidlok.teambuilder.Model.Team;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

>>>>>>> 4fa86b2b9cd7385042bc2bcc0f84e44ccddbd869

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

<<<<<<< HEAD
    Activity activity = getActivity();
    private List<String> dataSet = new ArrayList<String>();
=======
<<<<<<< HEAD
=======
    Activity activity = getActivity();private List<String> dataSet = new ArrayList<String>();
>>>>>>> d90ff2c7fb3bfd72f94282cb2de09b5c00e2d2dd
    private FloatingActionButton fab;
    static final int REQUEST_CODE_EVENT_NAME_SET = 1;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

>>>>>>> 4fa86b2b9cd7385042bc2bcc0f84e44ccddbd869

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

<<<<<<< HEAD
=======
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab = (FloatingActionButton) view.findViewById(R.id.fabNewEvent);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo open new fragment, not new activity. use fragment manager
//                Intent createEventIntent = new Intent(v.getContext(), CreateEventActivity.class);
//                startActivityForResult(createEventIntent, REQUEST_CODE_EVENT_NAME_SET);
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

>>>>>>> 4fa86b2b9cd7385042bc2bcc0f84e44ccddbd869
}
