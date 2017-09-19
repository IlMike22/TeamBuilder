package com.example.mwidlok.teambuilder;

import android.content.Intent;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mwidlok.teambuilder.Model.Team;

import io.realm.Realm;

import static com.example.mwidlok.teambuilder.MainActivity.REQUEST_CODE_EVENT_NAME_SET;

public class CreateEventActivity extends AppCompatActivity {

    Button btnSaveEvent;
    EditText txtEventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        btnSaveEvent = (Button) findViewById(R.id.btnSave);
        txtEventName = (EditText) findViewById(R.id.txtEventName);

        btnSaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = txtEventName.getText().toString();

                if (eventName.isEmpty())
                {
                    Toast errorToast = Toast.makeText(getApplicationContext(), "Please define a name for event", Toast.LENGTH_SHORT);
                    errorToast.show();
                }
                else
                {
                    // now take user input and go back to main site with current adapter and list.
                    // Save event in db
                    Realm myDb = Realm.getDefaultInstance();
                    long teamAmount = myDb.where(Team.class).count();

                    myDb.beginTransaction();
                    Team newTeam = new Team();
                    newTeam.setId((int) teamAmount);
                    newTeam.setName(eventName);
                    myDb.copyToRealm(newTeam);
                    myDb.commitTransaction();
                    myDb.close();

                    Log.i("TeamBuilder","Realm: New team dataset named " + newTeam.getName() + " was saved in db.");
                    Log.i("TeamBuilder","Now close NewTeamActivity and go back to overview.");
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",eventName);
                    setResult(REQUEST_CODE_EVENT_NAME_SET, returnIntent);
                    finish();
                }
            }
        });
    }






}
