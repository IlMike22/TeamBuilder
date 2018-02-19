package com.example.mwidlok.teambuilder;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
    private NavigationView navView;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
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

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        fab = (FloatingActionButton) findViewById(R.id.fabNewEvent);


        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawerOpen, R.string.drawerClose){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle("TeamBuilder");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("TeamBuilder");
            }
        };

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createEventIntent = new Intent(v.getContext(), CreateEventActivity.class);
                startActivityForResult(createEventIntent, REQUEST_CODE_EVENT_NAME_SET);
            }
        });

        navView = (NavigationView) findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.i("TeamBuilder","Item selected");
                return false;
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

        mRecyclerView = (RecyclerView) findViewById(R.id.rvEventsView);
        mRecyclerView.setHasFixedSize(true);

        // setting Layout Manager for Recycler View
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RvEventsAdapter(dataSet);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
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

        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        switch (item.getItemId())
        {
            case R.id.deleteDb:
                Log.i("TeamBuilder","Delete Db Option selected");
                if (deleteDatabase())
                    Toast.makeText(getApplicationContext(),"Realm database was successfully deleted.",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),"Realm database couldn't be deleted.", Toast.LENGTH_SHORT).show();
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

        return Realm.getInstance(realmConfig);
    }

    private boolean deleteDatabase()
    {
        Realm myDb = RealmHelper.getRealmInstance();
        Log.i("TeamBuilder","Trying to delete realm db..");
        myDb.close();

        return Realm.deleteRealm(myDb.getConfiguration());
    }
}
