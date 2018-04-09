package com.example.mwidlok.teambuilder;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mwidlok.teambuilder.Model.Person;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */


public class TeamResultFragment extends Fragment {

    TextView tvResult1;
    TextView tvResult2;
    Button btnComplete;
    int eventId = -1;

    TeamResultListener mCallback;

    interface TeamResultListener {
        void onResultViewFinished();
    }


    public TeamResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCallback = (MainActivity) getActivity();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvResult1 = (TextView) view.findViewById(R.id.tvResult1);
        tvResult2 = (TextView) view.findViewById(R.id.tvResult2);
        btnComplete = (Button) view.findViewById(R.id.btnComplete);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            eventId = bundle.getInt("eventId", -1);
        } else {
            Log.e(getString(R.string.app_title), getString(R.string.teamresult_error_bundle_null));
            return;
        }

        ArrayList<Person> teamMembers = new ArrayList<>();

        Realm myDb = RealmHelper.getRealmInstance();
        RealmResults<Person> allPersons = myDb.where(Person.class).equalTo("teamId", eventId).findAll();
        RealmList<Person> realmList = new RealmList<Person>();
        realmList.addAll(allPersons.subList(0, allPersons.size()));
        List<Person> personList = myDb.copyFromRealm(allPersons);

        for (Person p : personList) {
            teamMembers.add(p);
            Log.i(getString(R.string.app_title), "Here is Person " + p.getFirstName() + " " + p.getLastName());
        }

        ArrayList<ArrayList<Person>> result = generateTeams(teamMembers, eventId);

        String result1Output = getString(R.string.team_result_persons_in_team1);
        String result2Output = getString(R.string.team_result_persons_in_team2);

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onResultViewFinished();
            }
        });

        try {
            for (Person person : result.get(0)) {
                result1Output += person.getFirstName() + " " + person.getLastName();
                if (result.get(0).indexOf(person) != result.get(0).size() - 1) {
                    result1Output += ", ";
                }
            }
        } catch (Exception exc) {
            Log.e(getString(R.string.app_title), "An error accured while putting out information about the team members.");
            Log.e(getString(R.string.app_title), "Details: " + exc.getMessage());
        }

        try {
            for (Person person : result.get(1)) {
                result2Output += person.getFirstName() + " " + person.getLastName();
                if (result.get(1).indexOf(person) != result.get(1).size() - 1) {
                    result2Output += ", ";
                }
            }
        } catch (Exception exc) {
            Log.e(getString(R.string.app_title), "An error accured while putting out the info about the team memabers.");
            Log.e(getString(R.string.app_title), "Details: " + exc.getMessage());
        }

        if (tvResult1 != null)
            tvResult1.setText(result1Output);
        if (tvResult2 != null)
            tvResult2.setText(result2Output);
    }

    private ArrayList<ArrayList<Person>> generateTeams(ArrayList<Person> personList, int teamId) {

        // first of all we must have enough (>= 4 member) to build two teams
        if (personList.size() < 4) {
            Toast.makeText(getActivity().getApplicationContext(), R.string.teamresult_error_not_enough_members, Toast.LENGTH_SHORT).show();
            return null;
        }

        BusinessLogic bl = new BusinessLogic();
        ArrayList<ArrayList<Person>> result = bl.createTeams(personList);
        Log.i(getString(R.string.app_title), "Teams were successfully generated.");
        //todo Logik überarbeiten und Validieren
        return result;
    }
}
