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

import io.realm.Realm;
import io.realm.RealmConfiguration;
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

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.spSkillLevel, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spSkillLevel.setAdapter(arrayAdapter);



        btnSaveMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Person newPerson = validateAndCreatePerson();

                // now query this request
                Realm myDb = Realm.getDefaultInstance();

                //getting amount of persons that are already saved in db. so we can get the current id.
                long personAmount = myDb.where(Person.class).count();

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
                finish();
            }
        });
    }

    private Person validateAndCreatePerson()
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

        return newPerson;
    }
}
