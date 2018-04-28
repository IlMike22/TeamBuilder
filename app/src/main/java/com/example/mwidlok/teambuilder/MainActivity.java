package com.example.mwidlok.teambuilder;

import android.nfc.Tag;
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
    String TAG = "TeamBuilder";
    private final int REQUESTCODE_MEMBER_CREATED = 100;
    private final int REQUESTCODE_MEMBER_EDITED = 101;
    private final int REQUESTCODE_MEMBER_DELETED = 102;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment mainFragment = new MainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.llyt_container, mainFragment, "com.example.mwidlok.teambuilder.MainFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

//    @Override
//    public void onBackPressed() {
//
//
//        //todo maybe this is not needed. addtobackstack should save the last fragment and should open it when user clicks back button
//        // todo for create new person the back button click already works but other views dont react by pressing the back button :(
//        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.llyt_container);
//        if (currentFrag instanceof MainFragment)
//        {
//            //main fragment is our current fragment. disable back button
//            return;
//        }
//        else if (currentFrag instanceof CreatePersonFragment)
//        {
//            Log.i(TAG,"Event Detail Fragment now open");
//            //createTransactionAndReplaceFragment(eventDetailFragment, getString(R.string.frg_tag_event_detail))
//            super.onBackPressed();
//        }
//
//        else if (currentFrag instanceof EventDetailFragment)
//        {
//            Log.i(TAG,"open Event List Fragment now");
//        }
//    }

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
    public void onNewPersonCreated(Person newPerson, int eventId) {
        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("newPerson", newPerson);
        args.putInt("eventId", eventId);
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



