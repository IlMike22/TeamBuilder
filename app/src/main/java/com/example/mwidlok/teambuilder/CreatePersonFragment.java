package com.example.mwidlok.teambuilder;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePersonFragment extends Fragment {


    public CreatePersonFragment() {
        // Required empty public constructor
    }

    Button btnSaveMember;
    Button btnDeleteMember;
    EditText txtFirstName;
    EditText txtName;
    EditText txtAge;
    Spinner spSkillLevel;
    TextView tvPersonInfo;

    private final int REQUEST_CODE_NEW_MEMBER_SET = 100;
    private final int REQUESTCODE_EDIT_MEMBER= 101;
    private final int REQUESTCODE_DELETE_MEMBER = 102;
    final Date currentDate = Calendar.getInstance().getTime();
    final Activity activity = getActivity();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_person, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        btnSaveMember = (Button) view.findViewById(R.id.btnSaveMember);
        btnDeleteMember = (Button) view.findViewById(R.id.btnDeleteMember);
        txtFirstName = (EditText) view.findViewById(R.id.txtFirstName);
        txtName = (EditText) view.findViewById(R.id.txtName);
        txtAge = (EditText) view.findViewById(R.id.txtAge);
        spSkillLevel = (Spinner) view.findViewById(R.id.spSkillLevel);
        tvPersonInfo = (TextView) view.findViewById(R.id.tvPersonInfo);

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

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(activity, R.array.spSkillLevelEntries, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spSkillLevel.setAdapter(arrayAdapter);

        final int id = getActivity().getIntent().getIntExtra("currentPersonId",-1);
        final int teamId = getActivity().getIntent().getIntExtra("teamId", -1);

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
                    showDeleteConfirmDialog(id);
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

                    try
                    {
                        myDb.copyToRealmOrUpdate(newPerson);
                        myDb.commitTransaction();
                        myDb.close();
                        Log.i("TeamBuilder","Realm: New data successfully saved.");
                    }
                    catch(Exception exc)
                    {
                        Log.e("TeamBuilder","Failed to write in db. Details: " + exc.getMessage());
                    }


                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("newPersonResult", newPerson);
                    activity.setResult(REQUEST_CODE_NEW_MEMBER_SET, returnIntent);

                    try
                    {
                        activity.finish();
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

        newPerson.setFirstName(firstName);
        newPerson.setLastName(lastName);
        newPerson.setAge(age);
        newPerson.setCreateDate(currentDate);

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

        newPerson.setSkillLevelDescription(skillLevel);

        Realm myDb = RealmHelper.getRealmInstance();

        Team currentTeam = myDb.where(Team.class).equalTo("id", teamId).findFirst();
        newPerson.setTeamId(currentTeam.getId());

        return newPerson;
    }

    private boolean getPersonInformationForEdit(final int id, final int teamId)
    {

        try
        {
            final Person person = RealmHelper.getRealmInstance().where(Person.class).equalTo("id",id).findFirst();
            txtFirstName.setText(person.getFirstName());
            txtName.setText(person.getLastName());
            txtAge.setText(String.valueOf(person.getAge()));
            spSkillLevel.setSelection(person.getSkillLevel());

            String personInfo = "";
            String formattedDate = "";
            tvPersonInfo.setVisibility(View.VISIBLE);
            if (person.getCreateDate() != null)
            {
                formattedDate = (String) DateFormat.format("EEEE, dd.MM.yyyy, HH:mm:ss",person.getCreateDate());
                personInfo = "Person created on " + formattedDate;
            }

            if (person.getUpdateDate() != null)
            {
                formattedDate = (String) DateFormat.format("EEEE, dd.MM.yyyy, HH:mm:ss",person.getUpdateDate());
                personInfo += "\nPerson updated on " + formattedDate;
            }

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
                        DialogHelper.showStandardDialog("Success", "The person was updated successfully.",false, activity , REQUESTCODE_EDIT_MEMBER);
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

    public void deletePerson(final int id)
    {
        try
        {
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
        catch(Exception exc)
        {
            Log.e("TeamBuilder","Cannot delete row. Details: " + exc.getMessage());
        }
    }

    private void showDeleteConfirmDialog(final int id)
    {
        // we have to put our code in the onclick listener..
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder = new AlertDialog.Builder(activity,android.R.style.Theme_Material_Dialog_Alert);
        else
            builder = new AlertDialog.Builder(activity);

        builder.setTitle("Delete person")
                .setMessage("You really want to delete this person?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("TeamBuilder","Ok clicked. Now turn back to overview.");
                        deletePerson(id);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("deletePerson", id);
                        activity.setResult(REQUESTCODE_DELETE_MEMBER, returnIntent);
                        activity.finish();
                    }
                });

        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}
