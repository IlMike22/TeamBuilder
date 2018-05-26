package com.example.mwidlok.teambuilder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.mwidlok.teambuilder.Model.Person;

public class MainActivity extends AppCompatActivity implements CreateEventFragment.OnEventCreatedListener,
        EventDetailFragment.OnEventClickedForDetailViewListener,
        CreatePersonFragment.CreateNewPersonListener,
        TeamResultFragment.TeamResultListener,
        ImpressumFragment.OnImpressumClickedListener

{
    final private String TAG = "TeamBuilder";
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        NavigationView navView = (NavigationView) findViewById(R.id.navView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // setting toolbar instead of standard actionbar for drawer
        setSupportActionBar(toolbar);

        ActionBar actionBar =  getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                mDrawerLayout.closeDrawers();

                switch(item.getItemId())
                {
                    case R.id.nav_about:
                        openImpressumView();
                        break;
                }
                return false;
            }
        });


        Fragment mainFragment = new MainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.llyt_container, mainFragment, "com.example.mwidlok.teambuilder.MainFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateListAfterEventCreated(String eventName) {
        // a new event was created. open main fragment and show new item in list..

        Log.i(TAG, "New event called " + eventName);

        MainFragment mainFragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(MainFragment.newEventCode, eventName);
        mainFragment.setArguments(args);

        createTransactionAndReplaceFragment(mainFragment, getString(R.string.frg_tag_main));
    }

    @Override
    public void openEventDetailView(int eventId) {
        // open detail view for event with given event id..
        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putInt("eventId", eventId);
        eventDetailFragment.setArguments(args);

        createTransactionAndReplaceFragment(eventDetailFragment, getString(R.string.frg_tag_event_detail));
    }

    @Override
    public void openNewPersonView(int eventId) {
        CreatePersonFragment createPersonFragment = new CreatePersonFragment();
        Bundle args = new Bundle();
        args.putInt("eventId", eventId);
        createPersonFragment.setArguments(args);

        createTransactionAndReplaceFragment(createPersonFragment, getString(R.string.frg_tag_create_person));

    }

    @Override
    public void openPersonDetailView(Person person, int eventId) {
        CreatePersonFragment personDetailViewFragment = new CreatePersonFragment();
        Bundle args = new Bundle();
        args.putSerializable("person", person);
        args.putInt("eventId", eventId);
        personDetailViewFragment.setArguments(args);

        createTransactionAndReplaceFragment(personDetailViewFragment, getString(R.string.frg_tag_person_detail));
    }

    @Override
    public void openTeamResultView(int eventId) {

        TeamResultFragment resultFragment = new TeamResultFragment();
        Bundle args = new Bundle();
        args.putInt("eventId", eventId);
        resultFragment.setArguments(args);

        createTransactionAndReplaceFragment(resultFragment, getString(R.string.frg_tag_team_result));
    }

    @Override
    public void openImpressumView() {
        ImpressumFragment impFragment = new ImpressumFragment();
        createTransactionAndReplaceFragment(impFragment, "IMPRESSUM_FRAGMENT");

    }

    @Override
    public void onNewPersonCreated(Person newPerson, int eventId) {
        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("newPerson", newPerson);
        args.putInt("eventId", eventId);
        int REQUESTCODE_MEMBER_CREATED = 100;
        args.putInt(getString(R.string.statuscode), REQUESTCODE_MEMBER_CREATED);
        eventDetailFragment.setArguments(args);

        createTransactionAndReplaceFragment(eventDetailFragment,  getString(R.string.frg_tag_event_detail));
    }

    @Override
    public void onPersonEdited(int eventId) {
        // called after a person was edited an user saved changes.
        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putInt("eventId", eventId);
        int REQUESTCODE_MEMBER_EDITED = 101;
        args.putInt(getString(R.string.statuscode), REQUESTCODE_MEMBER_EDITED);
        eventDetailFragment.setArguments(args);

        createTransactionAndReplaceFragment(eventDetailFragment,  getString(R.string.frg_tag_event_detail));

    }

    @Override
    public void onPersonDeleted(int eventId) {
        // called after person was successfully deleted in db.
        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putInt("eventId", eventId);
        int REQUESTCODE_MEMBER_DELETED = 102;
        args.putInt(getString(R.string.statuscode), REQUESTCODE_MEMBER_DELETED);
        eventDetailFragment.setArguments(args);

        createTransactionAndReplaceFragment(eventDetailFragment, getString(R.string.frg_tag_event_detail));

    }

    @Override
    public void onResultViewFinished() {
        // called after user wants to close team result view. simply opens main fragment view.
        MainFragment mainFragment = new MainFragment();

        createTransactionAndReplaceFragment(mainFragment,  getString(R.string.frg_tag_main));
    }

    private void createTransactionAndReplaceFragment(Fragment currentFragment, String fragmentTAG) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.llyt_container, currentFragment,fragmentTAG).addToBackStack(null).commit();
    }
}



