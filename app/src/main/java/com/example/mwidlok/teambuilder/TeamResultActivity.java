package com.example.mwidlok.teambuilder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.mwidlok.teambuilder.Model.Person;
import com.example.mwidlok.teambuilder.Model.Team;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class TeamResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_result);

        ArrayList<Person> teamMembers = new ArrayList<>();
        int teamId = getIntent().getIntExtra("currentTeamId",-1);
        Realm myDb = RealmHelper.getRealmInstance();
        RealmResults<Person> allPersons= myDb.where(Person.class).equalTo("teamId",teamId).findAll();

        for (Person p : allPersons)
        {
            teamMembers.add(p);
            Log.i("TeamBuilder","Here is Person " + p.getFirstName() + " " + p.getLastName());
        }
    }

    private void generateTeams(List<Person> personList, Team currentTeam)
    {
    }
}
