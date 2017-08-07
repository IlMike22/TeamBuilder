package com.example.mwidlok.teambuilder;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.mwidlok.teambuilder.Adapters.RvEventsAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> dataSet = new ArrayList<String>();
    private FloatingActionButton fab;
    static final int REQUEST_CODE_EVENT_NAME_SET = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = (FloatingActionButton) findViewById(R.id.fabNewEvent);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createEventIntent = new Intent(v.getContext(), CreateEventActivity.class);
                startActivityForResult(createEventIntent, REQUEST_CODE_EVENT_NAME_SET);
            }
        });

        dataSet.add("Eintrag 1");
        dataSet.add("Eintrag 2");
        dataSet.add("Eintrag 3");

        mRecyclerView = (RecyclerView) findViewById(R.id.rvEventsView);
        mRecyclerView.setHasFixedSize(true);

        // setting Layout Manager for Recycler View
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RvEventsAdapter(dataSet);
        mRecyclerView.setAdapter(mAdapter);

        // Creating Realm database..

        Realm myRealm = Realm.getInstance(new RealmConfiguration.Builder(getApplicationContext()).name("myRealmDatabase").build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EVENT_NAME_SET)
        {
            String result;
            if (data != null)
            {
                result = data.getStringExtra("result");
                dataSet.add(result);
                mAdapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
