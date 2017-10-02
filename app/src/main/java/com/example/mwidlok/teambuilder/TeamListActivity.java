package com.example.mwidlok.teambuilder;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mwidlok.teambuilder.Adapters.RvTeamListAdapter;
import com.example.mwidlok.teambuilder.Model.Person;
import com.example.mwidlok.teambuilder.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class TeamListActivity extends AppCompatActivity {

    private FloatingActionButton fabNewTeamMember;
    public final int REQUESTCODE_NEWTEAMMEMBER = 1;
    public ArrayList<RealmResults<Person>> dataSet = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView rvTeamView;
    RvTeamListAdapter teamListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        fabNewTeamMember = (FloatingActionButton) findViewById(R.id.fabnewTeamMember);

        fabNewTeamMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreatePersonActivity.class);
                startActivityForResult(intent, REQUESTCODE_NEWTEAMMEMBER);
            }
        });

        Realm myDb = Realm.getDefaultInstance();
        RealmResults<Person> allPersons= myDb.where(Person.class).findAll();

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

        myDb.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_NEWTEAMMEMBER)
        {
            if (data != null)
            {
                Person newPerson = (Person) data.getSerializableExtra("newPersonResult");
                Log.i("TeamBuilder","Got new Person from CreateTeamActivity named " + newPerson.getFirstName() + " " + newPerson.getLastName());
                dataSet.add(newPerson);
                // now update the adapter for list of team members
                teamListAdapter.notifyDataSetChanged();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
