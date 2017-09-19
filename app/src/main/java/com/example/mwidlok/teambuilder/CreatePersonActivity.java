package com.example.mwidlok.teambuilder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mwidlok.teambuilder.Model.Person;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static com.example.mwidlok.teambuilder.MainActivity.REQUEST_CODE_EVENT_NAME_SET;

public class CreatePersonActivity extends AppCompatActivity {

    Button btnSaveMember;
    EditText txtFirstName;
    EditText txtName;
    EditText txtAge;
    EditText txtSkillLevel;

    private final int REQUEST_CODE_NEW_MEMBER_SET = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_person);


        btnSaveMember = (Button) findViewById(R.id.btnSaveMember);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtName = (EditText) findViewById(R.id.txtName);
        txtAge = (EditText) findViewById(R.id.txtAge);
        txtSkillLevel = (EditText) findViewById(R.id.txtSkillLevel);

        btnSaveMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // now query this request
                Realm myDb = Realm.getDefaultInstance();

                //getting amount of persons that are already saved in db. so we can get the current id.
                long personAmount = myDb.where(Person.class).count();

                // creating realm transaction
                myDb.beginTransaction();

                Person newPerson = new Person();
                newPerson.setId((int) personAmount);
                Log.i("TeamBuilder","Realm: New data gets id " + newPerson.getId());
                newPerson.setFirstName(txtFirstName.getText().toString());
                newPerson.setLastName(txtName.getText().toString());
                newPerson.setAge(Integer.parseInt(txtAge.getText().toString()));
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
}
