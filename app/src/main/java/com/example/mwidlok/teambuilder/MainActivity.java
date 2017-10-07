package com.example.mwidlok.teambuilder;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mwidlok.teambuilder.Adapters.RvEventsAdapter;
import com.example.mwidlok.teambuilder.Model.Person;
import com.example.mwidlok.teambuilder.Model.Team;
import com.scand.realmbrowser.RealmBrowser;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> dataSet = new ArrayList<String>();
    private FloatingActionButton fab;
    static final int REQUEST_CODE_EVENT_NAME_SET = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

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


        Realm myDb = getRealmInstance();
        Log.i("TeamBuilder","Realm: Reading all persons from db..");

        RealmResults<Team> allTeams = myDb.where(Team.class).findAll();

        for (Team currentTeam : allTeams)
        {
            Log.i("TeamBuilder","Realm: Found a team dataset named " + currentTeam.getName());
            dataSet.add(currentTeam.getName());
        }

        //notwendig?
        myDb.close();

        mRecyclerView = (RecyclerView) findViewById(R.id.rvEventsView);
        mRecyclerView.setHasFixedSize(true);

        // setting Layout Manager for Recycler View
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RvEventsAdapter(dataSet);
        mRecyclerView.setAdapter(mAdapter);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.deleteDb:
                Log.i("TeamBuilder","Delete Db Option selected");
                if (deleteDatabase())
                    Toast.makeText(getApplicationContext(),"Realm datanbase was successfully deleted.",Toast.LENGTH_SHORT);
                else
                    Toast.makeText(getApplicationContext(),"Realm database couldn't be deleted.", Toast.LENGTH_SHORT);
            case R.id.showDbBrowser:
                List<Class<? extends RealmObject>> classes = new ArrayList<>();
                classes.add(Person.class);
                classes.add(Team.class);

                Log.i("TeamBuilder","No showing Realm Browser");
                new RealmBrowser.Builder(this)
                        .add(getRealmInstance(), classes)
                        .show();
        }

        return true;
    }

    private Realm getRealmInstance()
    {
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("myRealmDatabase.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        return Realm.getDefaultInstance();
    }

    private boolean deleteDatabase()
    {
        Realm myDb = Realm.getDefaultInstance();
        myDb.close();
        Log.i("TeamBuilder","Trying to delete realm db..");
        return Realm.deleteRealm(myDb.getConfiguration());
    }
}
