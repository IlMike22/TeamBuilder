package com.example.mwidlok.teambuilder;

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

public class CreatePersonActivity extends AppCompatActivity {

    Button btnSaveMember;
    EditText txtFirstName;
    EditText txtName;
    EditText txtAge;
    EditText txtSkillLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_person);

        // creating realm database
        final Realm myRealm = Realm.getInstance(new RealmConfiguration.Builder(getApplicationContext()).name("myRealmDatabase").build());

        btnSaveMember = (Button) findViewById(R.id.btnSaveMember);

        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtName = (EditText) findViewById(R.id.txtName);
        txtAge = (EditText) findViewById(R.id.txtAge);
        txtSkillLevel = (EditText) findViewById(R.id.txtSkillLevel);



        btnSaveMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // creating realm transaction
                myRealm.beginTransaction();

                Person newPerson = myRealm.createObject(Person.class);
                newPerson.setId(0);
                newPerson.setFirstName(txtFirstName.getText().toString());
                newPerson.setLastName(txtName.getText().toString());

                try
                {
                    newPerson.setAge(Integer.parseInt(txtAge.getText().toString()));
                }
                catch(Exception exc)
                {
                    Log.e("TeamBuilder","Error while parsing age (integer).");
                }

                myRealm.commitTransaction();

                // now query this request
                RealmResults<Person> results = myRealm.where(Person.class).findAll();
                Toast t;

                if (results.size() > 0)
                {
                    t = Toast.makeText(getApplicationContext(), "Found a result named " + results.get(0).getFirstName() + " " + results.get(0).getLastName(),Toast.LENGTH_SHORT);
                }
                else
                    t = Toast.makeText(getApplicationContext(), "No data found.", Toast.LENGTH_SHORT);

                t.show();
            }
        });
    }
}
