package com.example.mwidlok.teambuilder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CreateEventActivity extends AppCompatActivity {

    Button btnSaveEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        btnSaveEvent = (Button) findViewById(R.id.btnSave);

        btnSaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = new Toast(getApplicationContext());
                t.setText("Button create event clicked.");
                t.show();

            }
        });
    }






}
