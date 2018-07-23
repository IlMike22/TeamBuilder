package com.example.mwidlok.teambuilder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mwidlok.teambuilder.Model.Team;

import io.realm.Realm;


public class CreateEventFragment extends Fragment {

    private OnEventCreatedListener mCallback;
    private final String TAG = "TeamBuilder";

    public interface OnEventCreatedListener
    {
        void updateListAfterEventCreated(String eventName);
    }

    private Button btnSaveEvent;
    private EditText txtEventName;

    public CreateEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_event, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSaveEvent = (Button) view.findViewById(R.id.btnSave);
        txtEventName = (EditText) view.findViewById(R.id.txtEventName);

        btnSaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = txtEventName.getText().toString();

                if (eventName.isEmpty())
                    Toast.makeText(getContext(), "Please define a name for event", Toast.LENGTH_SHORT).show();

                else
                {
                    // now take user input and go back to main site with current adapter and list.
                    // Save event in db
                    Realm myDb = RealmHelper.getRealmInstance();

                    long teamAmount = myDb.where(Team.class).count();
                    myDb.beginTransaction();
                    Team newTeam = new Team();
                    newTeam.setId((int) teamAmount);
                    newTeam.setName(eventName);
                    myDb.copyToRealm(newTeam);
                    myDb.commitTransaction();
                    myDb.close();

                    Log.i(TAG,"Realm: New team dataset named " + newTeam.getName() + " was saved in db.");
                    Log.i(TAG,"Now close NewTeamActivity and go back to overview.");
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",eventName);
                    mCallback = (OnEventCreatedListener) getActivity();
                    mCallback.updateListAfterEventCreated(eventName);
                }
            }
        });
    }
}
