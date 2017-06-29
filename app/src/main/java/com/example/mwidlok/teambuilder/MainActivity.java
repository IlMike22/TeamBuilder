package com.example.mwidlok.teambuilder;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.mwidlok.teambuilder.Adapters.RvEventsAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> dataSet = new ArrayList<String>();


    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = (FloatingActionButton) findViewById(R.id.fabNewEvent);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CreateEventActivity.class);
                startActivity(intent);
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

    }
}
