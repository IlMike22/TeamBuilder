package com.example.mwidlok.teambuilder;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mwidlok.teambuilder.Model.Person;
import com.example.mwidlok.teambuilder.Model.Team;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class CreatePersonActivity extends AppCompatActivity{

    Button btnSaveMember;
    Button btnDeleteMember;
    EditText txtFirstName;
    EditText txtName;
    EditText txtAge;
    Spinner spSkillLevel;
    TextView tvPersonInfo;

    private final int REQUEST_CODE_NEW_MEMBER_SET = 1;
    private final int REQUESTCODE_EDITTEAMMEMBER= 100;
    final Date currentDate = Calendar.getInstance().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_person);

        final Activity currentActivity = this;

        btnSaveMember = (Button) findViewById(R.id.btnSaveMember);
        btnDeleteMember = (Button) findViewById(R.id.btnDeleteMember);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtName = (EditText) findViewById(R.id.txtName);
        txtAge = (EditText) findViewById(R.id.txtAge);
        spSkillLevel = (Spinner) findViewById(R.id.spSkillLevel);
        tvPersonInfo = (TextView) findViewById(R.id.tvPersonInfo);

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

        final int id = getIntent().getIntExtra("currentPersonId",-1);
        final int teamId = getIntent().getIntExtra("teamId", -1);

        if (teamId < 0)
        {
            Log.e("TeamBuilder", "Error. Current team Id not found. Couldn't read out current team.");
            return;
        }

        if (id > -1)
        {
            // edit mode
            btnDeleteMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.i("TeamBuilder","Deleting person with id " + id );
                    // todo show dialog with yes and no
                    DialogHelper.showStandardDialog("Delete person","Do you really want to delete this person?",true, currentActivity, 0);
                    Realm realmDb = RealmHelper.getRealmInstance();
                    realmDb.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            RealmResults<Person> result = realm.where(Person.class).equalTo("id", id).findAll();
                            if (result.deleteAllFromRealm())
                                Log.i("TeamBuilder","Row was successfully deleted.");
                            else
                                Log.e("TeamBuilder","Cannot delete row.");
                        }
                    });
                }
            });
            getPersonInformationForEdit(id, teamId);
        }
        else
        {
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
                    myDb.close();
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
        newPerson.setCreateDate(currentDate);

        Realm myDb = RealmHelper.getRealmInstance();

        Team currentTeam = myDb.where(Team.class).equalTo("id", teamId).findFirst();
        newPerson.setTeamId(currentTeam.getId());

        return newPerson;
    }

    private boolean getPersonInformationForEdit(final int id, final int teamId)
    {
        final Activity currentActivity = this;
            try
            {
                final Person person = RealmHelper.getRealmInstance().where(Person.class).equalTo("id",id).findFirst();
                txtFirstName.setText(person.getFirstName());
                txtName.setText(person.getLastName());
                txtAge.setText(String.valueOf(person.getAge()));
                spSkillLevel.setSelection(person.getSkillLevel());

                String personInfo = "";
                tvPersonInfo.setVisibility(View.VISIBLE);
                if (person.getCreateDate() != null)
                    personInfo = "Person created on " + person.getCreateDate().toString();
                if (person.getUpdateDate() != null)
                    personInfo += "\nPerson updated on " + person.getUpdateDate().toString();

                tvPersonInfo.setText(personInfo);

                // show delete button to delete current person
                btnDeleteMember.setVisibility(View.VISIBLE);

                // edit button text from create to update
                btnSaveMember.setText("aktualisieren");

                btnSaveMember.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (updatePersonData(id, teamId, person.getCreateDate()))
                        {
                            Log.i("TeamBuilder","Person updated successfully.");
                            DialogHelper.showStandardDialog("Success", "The person was updated successfully.",false, currentActivity , REQUESTCODE_EDITTEAMMEMBER);
                        }
                    }
                });

                return true;

            }
            catch(Exception exc)
            {
                Log.e("TeamBuilder","Unfortunately reading out the desired person failed. Details: " + exc.getMessage());
                return false;
            }
    }

    private boolean updatePersonData(int id, int teamId, Date createdDate)
    {
        try
        {
            Realm myDb = RealmHelper.getRealmInstance();
            myDb.beginTransaction();
            Person person = new Person();
            person.setId(id);

            person.setUpdateDate(currentDate);
            person.setCreateDate(createdDate);

            person.setFirstName(txtFirstName.getText().toString());
            person.setLastName(txtName.getText().toString());
            person.setAge(Integer.parseInt(txtAge.getText().toString()));
            person.setSkillLevel(spSkillLevel.getSelectedItemPosition());
            person.setSkillLevelDescription(spSkillLevel.getSelectedItem().toString());
            person.setTeamId(teamId);

            myDb.copyToRealmOrUpdate(person);
            myDb.commitTransaction();
            myDb.close();

            return true;
        }

        catch(Exception exc)
        {
            Log.e("TeamBuilder","An error occured during updating person data. Details: " + exc.getMessage());
            return false;
        }
    }

}
