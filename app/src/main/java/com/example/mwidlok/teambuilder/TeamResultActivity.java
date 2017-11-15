package com.example.mwidlok.teambuilder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mwidlok.teambuilder.Model.Person;
import com.example.mwidlok.teambuilder.Model.Team;

import java.util.List;

public class TeamResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_result);

        int teamId = getIntent().getIntExtra("teamId",0);

        //todo we have to get team information (all members) so we can generate teams.
        //todo write method which generates team (team builder algorithm)
    }

    private void generateTeams(List<Person> personList, Team currentTeam)
    {

    }
}
