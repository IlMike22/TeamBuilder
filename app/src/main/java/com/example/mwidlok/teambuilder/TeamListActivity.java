package com.example.mwidlok.teambuilder;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mwidlok.teambuilder.Model.Person;
import com.example.mwidlok.teambuilder.R;

public class TeamListActivity extends AppCompatActivity {

    private FloatingActionButton fabNewTeamMember;
    public final int REQUESTCODE_NEWTEAMMEMBER = 1;

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_NEWTEAMMEMBER)
        {
            if (data != null)
            {
                Person newPerson = (Person) data.getSerializableExtra("newPersonResult");
                Log.i("TeamBuilder","Got new Person from CreateTeamActivity named " + newPerson.getFirstName() + " " + newPerson.getLastName());

                // now update the adapter for list of team members
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
