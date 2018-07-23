package com.example.mwidlok.teambuilder;


import android.app.AlertDialog;
import android.content.DialogInterface;
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

    private Button btnSaveMember;
    private Button btnDeleteMember;
    private EditText txtFirstName;
    private EditText txtName;
    private EditText txtAge;
    private Spinner spSkillLevel;
    private TextView tvPersonInfo;

    final Date currentDate = Calendar.getInstance().getTime();

    private CreateNewPersonListener mCallback;

    interface CreateNewPersonListener {
        void onNewPersonCreated(Person newPerson, int eventId);

        void onPersonEdited(int eventId);

        void onPersonDeleted(int eventId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_person, container, false);
    }


    
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCallback = (MainActivity) getActivity();

        final int eventId;
        final Person currentPerson;
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
                Log.i(getString(R.string.app_title), "Item " + parent.getItemAtPosition(position).toString() + " selected");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(getString(R.string.app_title), "Warning. No item selected.");
            }
        });

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.spSkillLevelEntries, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spSkillLevel.setAdapter(arrayAdapter);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            eventId = bundle.getInt("eventId", -1);
            currentPerson = (Person) bundle.getSerializable("person");
        } else {
            Log.e(getString(R.string.app_title), "The bundle is null. Cannot read event id for creating new person.");
            return;
        }

        if (currentPerson != null) {
            // edit mode
            btnDeleteMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.i(getString(R.string.app_title), "Deleting person with id " + currentPerson.getId());
                    showDeleteConfirmDialog(currentPerson, eventId);
                }
            });

            getPersonInformationForEdit(currentPerson, eventId);
        } else {
            btnSaveMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Person newPerson = validateAndCreatePerson(eventId);

                    if (newPerson == null)
                    {
                        showStandardDialog(getString(R.string.app_title),getString(R.string.create_person_data_needed_msg));
                        return;
                    }

                    // now query this request
                    Realm myDb = RealmHelper.getRealmInstance();

                    //getting amount of persons that are already saved in db. so we can get the current id.
                    //getting also the amount of current team members. Therefore we catch all persons with current team id.
                    int counter = 0;
                    ArrayList<Person> personList = new ArrayList<>(myDb.where(Person.class).findAll());
                    for (Person p : personList) {
                        if (p.getEventId() == eventId)
                            counter++;
                    }

                    long personAmount = myDb.where(Person.class).count();
                    Log.i(getString(R.string.app_title), "There are " + personAmount + " persons in db at the moment.");
                    Log.i(getString(R.string.app_title), counter + " persons belong to current team with id " + eventId);

                    // creating realm transaction
                    myDb.beginTransaction();
                    newPerson.setId((int) personAmount);
                    Log.i(getString(R.string.app_title), "Realm: New data gets id " + newPerson.getId());

                    try {
                        myDb.copyToRealmOrUpdate(newPerson);
                        myDb.commitTransaction();
                        myDb.close();
                        Log.i(getString(R.string.app_title), "Realm: New data successfully saved.");
                    } catch (Exception exc) {
                        Log.e(getString(R.string.app_title), "Failed to write in db. Details: " + exc.getMessage());
                    }

                    if (mCallback != null)
                        mCallback.onNewPersonCreated(newPerson, eventId);
                    else
                        Log.e(getString(R.string.app_title), "mCallback is null, cannot call activity method onNewPersonCreated()");
                }
            });
        }
    }

    private Person validateAndCreatePerson(int teamId) {
        String firstName = txtFirstName.getText().toString();
        String lastName = txtName.getText().toString();
        String ageString = txtAge.getText().toString();


        if (firstName.isEmpty()) return null;
        if (lastName.isEmpty()) return null;
        if (ageString.isEmpty()) return null;

        int age = Integer.parseInt(txtAge.getText().toString());

        Person newPerson = new Person();

        newPerson.setFirstName(firstName);
        newPerson.setLastName(lastName);
        newPerson.setAge(age);
        newPerson.setCreateDate(currentDate);

        String skillLevel = spSkillLevel.getSelectedItem().toString();

        switch (skillLevel) {
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
                Log.e("TeamBuilder", "No valid skill level selected");
                return null;
        }

        newPerson.setSkillLevelDescription(skillLevel);

        Realm myDb = RealmHelper.getRealmInstance();

        Team currentTeam = myDb.where(Team.class).equalTo("id", teamId).findFirst();
        newPerson.setTeamId(currentTeam.getId());

        return newPerson;
    }

    private void getPersonInformationForEdit(final Person currentPerson, final int eventId) {

        try {
            //final Person person = RealmHelper.getRealmInstance().where(Person.class).equalTo("id",personId).findFirst();
            txtFirstName.setText(currentPerson.getFirstName());
            txtName.setText(currentPerson.getLastName());
            txtAge.setText(String.valueOf(currentPerson.getAge()));
            spSkillLevel.setSelection(currentPerson.getSkillLevel());

            String personInfo = "";
            String formattedDate;
            tvPersonInfo.setVisibility(View.VISIBLE);
            if (currentPerson.getCreateDate() != null) {
                formattedDate = (String) DateFormat.format("EEEE, dd.MM.yyyy, HH:mm:ss", currentPerson.getCreateDate());
                personInfo = "Person created on " + formattedDate;
            }

            if (currentPerson.getUpdateDate() != null) {
                formattedDate = (String) DateFormat.format("EEEE, dd.MM.yyyy, HH:mm:ss", currentPerson.getUpdateDate());
                personInfo += "\nPerson updated on " + formattedDate;
            }

            tvPersonInfo.setText(personInfo);

            // show delete button to delete current person
            btnDeleteMember.setVisibility(View.VISIBLE);

            // edit button text from create to update
            btnSaveMember.setText(R.string.create_person_update_btn);

            btnSaveMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (updatePersonData(currentPerson.getId(), eventId, currentPerson.getCreateDate())) {
                        Log.i("TeamBuilder", "Person updated successfully.");
                        if (mCallback != null)
                            mCallback.onPersonEdited(eventId);
                        else
                            Log.e("TeamBuilder", "mCallback is null. Cannot call activity method onPersonEdited()");
                    }
                }
            });

        } catch (Exception exc) {
            Log.e("TeamBuilder", "Unfortunately reading out the desired person failed. Details: " + exc.getMessage());
        }
    }

    private boolean updatePersonData(int personId, int eventId, Date createdDate) {
        try {
            Realm myDb = RealmHelper.getRealmInstance();
            myDb.beginTransaction();
            Person person = new Person();
            person.setId(personId);

            person.setUpdateDate(currentDate);
            person.setCreateDate(createdDate);

            person.setFirstName(txtFirstName.getText().toString());
            person.setLastName(txtName.getText().toString());
            person.setAge(Integer.parseInt(txtAge.getText().toString()));
            person.setSkillLevel(spSkillLevel.getSelectedItemPosition());
            person.setSkillLevelDescription(spSkillLevel.getSelectedItem().toString());
            person.setTeamId(eventId);

            myDb.copyToRealmOrUpdate(person);
            myDb.commitTransaction();
            myDb.close();

            return true;
        } catch (Exception exc) {
            Log.e(getString(R.string.app_title), "An error occured during updating person data. Details: " + exc.getMessage());
            return false;
        }
    }

    private void deletePerson(final int id) {
        try {
            Realm realmDb = RealmHelper.getRealmInstance();
            realmDb.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    if (realm == null)
                    {
                        Log.e("TeamBuilder","Cannot delete data. Realm reference is null");
                        return;
                    }


                    RealmResults<Person> result = realm.where(Person.class).equalTo("id", id).findAll();
                    if (result.deleteAllFromRealm())
                        Log.i("TeamBuilder", "Row was successfully deleted.");
                    else
                        Log.e("TeamBuilder", "Cannot delete row.");
                }
            });
        } catch (Exception exc) {
            Log.e(getString(R.string.app_title), "Cannot delete row. Details: " + exc.getMessage());
        }
    }

    // shows a standard dialog with "ok" button
    private void showStandardDialog(String title, String message)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        else
            builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void showDeleteConfirmDialog(final Person currentPerson, final int eventId) {
        // we have to put our code in the onclick listener..
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        else
            builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Delete person")
                .setMessage("You really want to delete this person?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("TeamBuilder", "Ok clicked. Now turn back to overview.");
                        deletePerson(currentPerson.getId());

                        if (mCallback != null)
                            mCallback.onPersonDeleted(eventId);
                        else
                            Log.e("TeamBuilder", "mCallback is null. Cannot call activity method onPersonDeleted()");

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
