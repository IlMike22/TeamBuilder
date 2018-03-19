package com.example.mwidlok.teambuilder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.example.mwidlok.teambuilder.Adapters.RvEventsAdapter;
import com.example.mwidlok.teambuilder.Model.Team;
import java.util.ArrayList;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends BaseActivity {


    private List<String> dataSet = new ArrayList<String>();
    private FloatingActionButton fab;
    static final int REQUEST_CODE_EVENT_NAME_SET = 1;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.onCreateDrawer();

        fab = (FloatingActionButton) findViewById(R.id.fabNewEvent);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createEventIntent = new Intent(v.getContext(), CreateEventActivity.class);
                startActivityForResult(createEventIntent, REQUEST_CODE_EVENT_NAME_SET);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.rvEventsView);
        mRecyclerView.setHasFixedSize(true);

        // setting Layout Manager for Recycler View
        mLayoutManager = new LinearLayoutManager(this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EVENT_NAME_SET) {
            String result;
            if (data != null) {
                result = data.getStringExtra("result");
                dataSet.add(result);
                mAdapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Realm getRealmInstance() {
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("myRealmDatabase.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        return Realm.getInstance(realmConfig);
    }


}
