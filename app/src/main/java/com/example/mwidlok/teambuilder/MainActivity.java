package com.example.mwidlok.teambuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mwidlok.teambuilder.Model.Person;

public class MainActivity extends AppCompatActivity implements CreateEventFragment.OnEventCreatedListener,
        EventDetailFragment.OnEventClickedForDetailViewListener,
        CreatePersonFragment.CreateNewPersonListener,
        TeamResultFragment.TeamResultListener

{
    private final int REQUESTCODE_MEMBER_CREATED = 100;
    private final int REQUESTCODE_MEMBER_EDITED = 101;
    private final int REQUESTCODE_MEMBER_DELETED = 102;


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

    @Override
    public void updateListAfterEventCreated(String eventName) {
        // a new event was created. open main fragment and show new item in list..

        Log.i("TeamBuilder", "New event called " + eventName);

        MainFragment mainFragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(MainFragment.newEventCode, eventName);
        mainFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.llyt_container, mainFragment).addToBackStack(null).commit();
    }

    @Override
    public void openEventDetailView(int eventId) {
        // open detail view for event with given event id..
        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putInt("eventId", eventId);
        eventDetailFragment.setArguments(args);

        createTransactionAndReplaceFragment(eventDetailFragment);
    }

    @Override
    public void openNewPersonView(int eventId) {
        CreatePersonFragment createPersonFragment = new CreatePersonFragment();
        Bundle args = new Bundle();
        args.putInt("eventId", eventId);
        createPersonFragment.setArguments(args);

        createTransactionAndReplaceFragment(createPersonFragment);

    }

    @Override
    public void openPersonDetailView(Person person, int eventId) {
        CreatePersonFragment personDetailViewFragment = new CreatePersonFragment();
        Bundle args = new Bundle();
        args.putSerializable("person", person);
        args.putInt("eventId", eventId);
        personDetailViewFragment.setArguments(args);

        createTransactionAndReplaceFragment(personDetailViewFragment);
    }

    @Override
    public void openTeamResultView(int eventId) {

        TeamResultFragment resultFragment = new TeamResultFragment();
        Bundle args = new Bundle();
        args.putInt("eventId", eventId);
        resultFragment.setArguments(args);

        createTransactionAndReplaceFragment(resultFragment);

    }

    @Override
    public void onNewPersonCreated(Person newPerson, int eventId) {
        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("newPerson", newPerson);
        args.putInt("eventId", eventId);
        args.putInt(getString(R.string.statuscode), REQUESTCODE_MEMBER_CREATED);
        eventDetailFragment.setArguments(args);

        createTransactionAndReplaceFragment(eventDetailFragment);
    }

    @Override
    public void onPersonEdited(int eventId) {
        // called after a person was edited an user saved changes.
        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putInt("eventId", eventId);
        args.putInt(getString(R.string.statuscode), REQUESTCODE_MEMBER_EDITED);
        eventDetailFragment.setArguments(args);

        createTransactionAndReplaceFragment(eventDetailFragment);

    }

    @Override
    public void onPersonDeleted(int eventId) {
        // called after person was successfully deleted in db.
        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putInt("eventId", eventId);
        args.putInt(getString(R.string.statuscode), REQUESTCODE_MEMBER_DELETED);
        eventDetailFragment.setArguments(args);

        createTransactionAndReplaceFragment(eventDetailFragment);

    }

    @Override
    public void onResultViewFinished() {
        // called after user wants to close team result view. simply opens main fragment view.
        MainFragment mainFragment = new MainFragment();

        createTransactionAndReplaceFragment(mainFragment);
    }

    private void createTransactionAndReplaceFragment(Fragment currentFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.llyt_container, currentFragment).addToBackStack(null).commit();
    }
}



