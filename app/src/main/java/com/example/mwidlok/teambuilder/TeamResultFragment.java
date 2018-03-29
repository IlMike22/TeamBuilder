package com.example.mwidlok.teambuilder;


import android.app.Activity;
import android.content.Intent;
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
    Activity activity = getActivity();



    public TeamResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvResult1 = (TextView) activity.findViewById(R.id.tvResult1);
        tvResult2 = (TextView) activity.findViewById(R.id.tvResult2);
        btnComplete = (Button) activity.findViewById(R.id.btnComplete);

        ArrayList<Person> teamMembers = new ArrayList<>();
        int teamId = activity.getIntent().getIntExtra("currentTeamId",-1);
        Realm myDb = RealmHelper.getRealmInstance();
        RealmResults<Person> allPersons= myDb.where(Person.class).equalTo("teamId",teamId).findAll();
        RealmList<Person> realmList = new RealmList<Person>();
        realmList.addAll(allPersons.subList(0,allPersons.size()));
        List<Person> personList = myDb.copyFromRealm(allPersons);

        for (Person p : personList)
        {
            teamMembers.add(p);
            Log.i("TeamBuilder","Here is Person " + p.getFirstName() + " " + p.getLastName());
        }

        ArrayList<ArrayList<Person>> result = generateTeams(teamMembers, teamId);

        if (personList.size() < 4) {
            DialogHelper.showStandardDialog("Not enough persons", "You must have at least 4 persons created to generate two teams.", false, activity, 0);
            return;
        }

        String result1Output = "Folgende Personen sind in Team 1:\n";
        String result2Output = "Folgende Personen sind in Team 2:\n";

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        try
        {
            for (Person person : result.get(0))
            {
                result1Output += person.getFirstName() + " " + person.getLastName();
                if (result.get(0).indexOf(person) != result.get(0).size()-1)
                {
                    result1Output += ", ";
                }
            }
        }
        catch(Exception exc)
        {
            Log.e("TeamBuilder","An error accured while putting out information about the team members.");
            Log.e("TeamBuilder","Details: " + exc.getMessage());
        }

        try
        {
            for (Person person : result.get(1))
            {
                result2Output += person.getFirstName() + " " + person.getLastName();
                if (result.get(1).indexOf(person) != result.get(1).size()-1)
                {
                    result2Output += ", ";
                }
            }
        }
        catch (Exception exc)
        {
            Log.e("TeamBuilder","An error accured while putting out the info about the team memabers.");
            Log.e("TeamBuilder","Details: " + exc.getMessage());
        }

        if (tvResult1 != null)
            tvResult1.setText(result1Output);
        if (tvResult2 != null)
            tvResult2.setText(result2Output);
    }

    private ArrayList<ArrayList<Person>> generateTeams(ArrayList<Person> personList, int teamId)
    {

        // first of all we must have enough (>= 4 member) to build two teams
        if (personList.size() < 4)
        {
            Toast.makeText(activity.getApplicationContext(), "There must be at least 4 members to generate two teams.",Toast.LENGTH_SHORT).show();
            return null;
        }

        BusinessLogic bl = new BusinessLogic();
        ArrayList<ArrayList<Person>> result = bl.createTeams(personList);
        Log.i("TeamBuilder","Teams were successfully generated.");
        //todo Logik überarbeiten und Validieren
        return result;
    }
}
