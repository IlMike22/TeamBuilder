package com.example.mwidlok.teambuilder;

import android.support.design.widget.BaseTransientBottomBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                    Log.i("myMessage","event was set. now close activity");
                    finish();
                }
            }
        });
    }






}
