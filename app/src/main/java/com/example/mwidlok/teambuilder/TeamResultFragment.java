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

    private TextView tvResult1;
    private TextView tvResult2;

    interface TeamResultListener {
        void onResultViewFinished();
    }

    private TeamResultListener mCallback;


    public TeamResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCallback = (MainActivity) getActivity();

        return inflater.inflate(R.layout.fragment_team_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvResult1 = (TextView) view.findViewById(R.id.tvResult1);
        tvResult2 = (TextView) view.findViewById(R.id.tvResult2);
        Button btnComplete = (Button) view.findViewById(R.id.btnComplete);

        Bundle bundle = this.getArguments();
        int eventId;
        if (bundle != null) {
            eventId = bundle.getInt("eventId", -1);
        } else {
            Log.e(getString(R.string.app_title), getString(R.string.teamresult_error_bundle_null));
            return;
        }

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onResultViewFinished();
            }
        });

        ArrayList<Person> teamMembers = new ArrayList<>();

        Realm myDb = RealmHelper.getRealmInstance();
        RealmResults<Person> allPersons = myDb.where(Person.class).equalTo("teamId", eventId).findAll();
        RealmList<Person> realmList = new RealmList<>();
        realmList.addAll(allPersons.subList(0, allPersons.size()));
        List<Person> personList = myDb.copyFromRealm(allPersons);

        for (Person p : personList) {
            teamMembers.add(p);
            Log.i(getString(R.string.app_title), "Here is Person " + p.getFirstName() + " " + p.getLastName());
        }

        printResult(generateTeams(teamMembers));
    }

    private ArrayList<ArrayList<Person>> generateTeams(ArrayList<Person> personList) {

        BusinessLogic bl = new BusinessLogic();
        ArrayList<ArrayList<Person>> result = bl.createTeams(personList);
        Log.i(getString(R.string.app_title), "Teams were successfully generated.");
        return result;
    }

    private void printResult(ArrayList<ArrayList<Person>> result) {

        String result1Output;
        String result2Output;
        String profiOutput = "";
        String averageOutput = "";
        String amateurOutput = "";

        ArrayList<Person> team1 = result.get(0);
        ArrayList<Person> team2 = result.get(1);

        for (Person p : team1) {
            switch (p.getSkillLevel()) {
                case 0:
                    amateurOutput += " - " + p.getFirstName() + " " + p.getLastName() + "\n";
                    break;
                case 1:
                    averageOutput += " - " + p.getFirstName() + " " + p.getLastName() + "\n";
                    break;
                case 2:
                    profiOutput += " - " + p.getFirstName() + " " + p.getLastName() + "\n";
                    break;
            }
        }

        result1Output = "Team 1 has the following members\n" + profiOutput + "\n" + averageOutput + "\n" + amateurOutput + "\n";

        profiOutput = "";
        averageOutput = "";
        amateurOutput = "";

        for (Person p2 : team2) {
            switch (p2.getSkillLevel()) {
                case 0:
                    amateurOutput += " - " + p2.getFirstName() + " " + p2.getLastName() + "\n";
                    break;
                case 1:
                    averageOutput += " - " + p2.getFirstName() + " " + p2.getLastName() + "\n";
                    break;
                case 2:
                     profiOutput += " - " + p2.getFirstName() + " " + p2.getLastName() + "\n";
                    break;
            }
        }

        result2Output = "Team 2 has the following members\n" + profiOutput + "\n" + averageOutput + "\n" + amateurOutput + "\n";


        if (tvResult1 != null)
            tvResult1.setText(result1Output);
        if (tvResult2 != null)
            tvResult2.setText(result2Output);
    }
}
