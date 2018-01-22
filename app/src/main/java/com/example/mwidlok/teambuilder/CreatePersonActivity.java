package com.example.mwidlok.teambuilder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mwidlok.teambuilder.Model.Person;
import com.example.mwidlok.teambuilder.Model.Team;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.example.mwidlok.teambuilder.MainActivity.REQUEST_CODE_EVENT_NAME_SET;

public class CreatePersonActivity extends AppCompatActivity{

    Button btnSaveMember;
    EditText txtFirstName;
    EditText txtName;
    EditText txtAge;
    Spinner spSkillLevel;

    private final int REQUEST_CODE_NEW_MEMBER_SET = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_person);

        btnSaveMember = (Button) findViewById(R.id.btnSaveMember);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtName = (EditText) findViewById(R.id.txtName);
        txtAge = (EditText) findViewById(R.id.txtAge);
        spSkillLevel = (Spinner) findViewById(R.id.spSkillLevel);

        spSkillLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TeamBuilder","Item " + parent.getItemAtPosition(position).toString() + " selected");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("TeamBuilder","Warning. No item selected.");
            }
        });

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.spSkillLevelEntries, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spSkillLevel.setAdapter(arrayAdapter);

        getPersonInformationForEdit(getIntent().getIntExtra("currentPersonId",-1));

        final int teamId = getIntent().getIntExtra("teamId", -1);
        if (teamId < 0) {
            Log.e("TeamBuilder", "Error. Current team Id not found. Couldn't read out current team.");
            return;
        }





        btnSaveMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Person newPerson = validateAndCreatePerson(teamId);

                // now query this request
                Realm myDb = RealmHelper.getRealmInstance();

                //getting amount of persons that are already saved in db. so we can get the current id.
                //getting also the amount of current team members. Therefore we catch all persons with current team id.
                int counter = 0;
                ArrayList<Person> personList = new ArrayList<Person>(myDb.where(Person.class).findAll());
                for (Person p : personList)
                {
                    if (p.getTeamId() == teamId)
                        counter++;
                }

                long personAmount = myDb.where(Person.class).count();
                Log.i("TeamBuilder information","There are " + personAmount + " persons in db at the moment.");
                Log.i("TeamBuilder information", counter + " persons belong to current team with id " + teamId);

                // creating realm transaction
                myDb.beginTransaction();
                newPerson.setId((int) personAmount);
                Log.i("TeamBuilder","Realm: New data gets id " + newPerson.getId());

                myDb.copyToRealm(newPerson);
                myDb.commitTransaction();
                Log.i("TeamBuilder","Realm: New data successfully saved.");

                Intent returnIntent = new Intent();
                returnIntent.putExtra("newPersonResult", newPerson);
                setResult(REQUEST_CODE_NEW_MEMBER_SET, returnIntent);

                try
                {
                    finish();
                }
                catch (Exception exc)
                {
                    Log.e("TeamBuilder","Fehler. Details: " + exc.getMessage());
                }
            }
        });
    }

    private Person validateAndCreatePerson(int teamId)
    {
        String firstName = txtFirstName.getText().toString();
        String lastName = txtName.getText().toString();
        int age = Integer.parseInt(txtAge.getText().toString());

        if (firstName == "") return null;
        if (lastName == "") return null;
        if (age <= 0) return null;

        Person newPerson = new Person();

        String skillLevel = spSkillLevel.getSelectedItem().toString();

        switch (skillLevel)
        {
            case "Amateur":
                newPerson.setSkillLevel(0);
                break;
            case "Average":
                newPerson.setSkillLevel(1);
                break;
            case "Profi":
                newPerson.setSkillLevel(2);
                break;
            default:
                Log.e("TeamBuilder","No valid skill level selected");
                return null;
        }
        newPerson.setFirstName(firstName);
        newPerson.setLastName(lastName);
        newPerson.setAge(age);

        Realm myDb = RealmHelper.getRealmInstance();

        Team currentTeam = myDb.where(Team.class).equalTo("id", teamId).findFirst();
        newPerson.setTeamId(currentTeam.getId());

        return newPerson;
    }

    private boolean getPersonInformationForEdit(int currentPersonId)
    {
        if (currentPersonId == -1)
            return false;

            Person currentPerson = null;

            try
            {
                currentPerson = RealmHelper.getRealmInstance().where(Person.class).equalTo("id",currentPersonId).findFirst();
            }
            catch(Exception exc)
            {
                Log.e("TeamBuilder","Unfortunately reading out the desired person failed. Details: " + exc.getMessage());
            }

            txtFirstName.setText(currentPerson.getFirstName());
            txtName.setText(currentPerson.getLastName());
            txtAge.setText(String.valueOf(currentPerson.getAge()));
            spSkillLevel.setSelection(currentPerson.getSkillLevel());

            return true;
    }
}
