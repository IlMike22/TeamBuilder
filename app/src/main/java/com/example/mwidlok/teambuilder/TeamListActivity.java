package com.example.mwidlok.teambuilder;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mwidlok.teambuilder.Adapters.RvTeamListAdapter;
import com.example.mwidlok.teambuilder.Model.Person;
import com.example.mwidlok.teambuilder.Model.Team;
import com.example.mwidlok.teambuilder.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class TeamListActivity extends AppCompatActivity {

    private FloatingActionButton fabNewTeamMember;

    public ArrayList<Person> dataSet = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView rvTeamView;
    RvTeamListAdapter teamListAdapter;
    Button btnGenerateTeams;

    // Request Codes
    private final int REQUESTCODE_NEWTEAMMEMBER = 100;
    private final int REQUESTCODE_EDITTEAMMEMBER = 101;
    private final int REQUESTCODE_DELETE_MEMBER = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        fabNewTeamMember = (FloatingActionButton) findViewById(R.id.fabnewTeamMember);
        btnGenerateTeams = (Button) findViewById(R.id.btnGenerateTeams);

        final int teamId = getIntent().getIntExtra("teamId",-1);

        if (teamId < 0)
        {
            Log.e("Error","Team Id not found.");
            return;
        }

        fabNewTeamMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreatePersonActivity.class);
                intent.putExtra("teamId",teamId);
                startActivityForResult(intent, REQUESTCODE_NEWTEAMMEMBER);
            }
        });

        btnGenerateTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataSet.size() == 0)
                {
                    Toast.makeText(getApplicationContext(),"No team members available to generate a team with.",Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), TeamResultActivity.class);
                intent.putExtra("currentTeamId", teamId);
                startActivity(intent);
            }
        });

        Realm myDb = RealmHelper.getRealmInstance();
        RealmResults<Person> allPersons= myDb.where(Person.class).equalTo("teamId",teamId).findAll();

        for (Person p : allPersons)
        {
            dataSet.add(p);
        }

        rvTeamView = (RecyclerView) findViewById(R.id.rvTeam);
        rvTeamView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        rvTeamView.setLayoutManager(mLayoutManager);
        teamListAdapter = new RvTeamListAdapter(dataSet);
        rvTeamView.setAdapter(teamListAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_NEWTEAMMEMBER)
        {
            if (resultCode == REQUESTCODE_NEWTEAMMEMBER)
            {
                if (data != null)
                {
                    Person newPerson = (Person) data.getSerializableExtra("newPersonResult");
                    Log.i("TeamBuilder","Got new Person from CreateTeamActivity named " + newPerson.getFirstName() + " " + newPerson.getLastName());
                    dataSet.add(newPerson);
                }
            }
            else if (resultCode == REQUESTCODE_EDITTEAMMEMBER)
            {
                // do sth if person was deleted. update dataSet!
                Log.i("TeamBilder","We are now in REQUESTCODE_DELETE_MEMBER");
            }
            else if (resultCode == REQUESTCODE_DELETE_MEMBER)
            {
                // do sth if person was edited.
                Log.i("TeamBuilder","We are now in REQUESTCODE_EDIT_MEMBER");
                int id = (int) data.getExtras().getInt("deletePerson");

                // todo error! fix it
                // todo now getting position of deleted person and delete entry from dataSet!

            }

            teamListAdapter.notifyDataSetChanged();
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {

        //close realm
        RealmHelper.getRealmInstance().close();
        super.onDestroy();
    }
}
