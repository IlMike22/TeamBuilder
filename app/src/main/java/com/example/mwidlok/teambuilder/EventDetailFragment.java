package com.example.mwidlok.teambuilder;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mwidlok.teambuilder.Adapters.RvTeamListAdapter;
import com.example.mwidlok.teambuilder.Model.Person;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailFragment extends Fragment {

    OnEventClickedForDetailViewListener mCallback;

    public interface OnEventClickedForDetailViewListener {
        void openEventDetailView(int eventId);
        void openNewPersonView(int eventId);
        void openPersonDetailView(Person person, int eventId);
        void openTeamResultView(int eventId);
    }

    private Activity activity = getActivity();
    private FloatingActionButton fabNewTeamMember;
    public ArrayList<Person> dataSet = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView rvTeamView;
    RvTeamListAdapter teamListAdapter;
    Button btnGenerateTeams;

    // Request Codes
    private final int REQUESTCODE_MEMBER_CREATED = 100;
    private final int REQUESTCODE_MEMBER_EDITED = 101;
    private final int REQUESTCODE_DELETE_MEMBER = 102;


    public EventDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_list_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final int eventId;
        Person newPerson = null;
        int statusCode = 0;


        fabNewTeamMember = (FloatingActionButton) view.findViewById(R.id.fabnewTeamMember);
        btnGenerateTeams = (Button) view.findViewById(R.id.btnGenerateTeams);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            // we have two situation to handle. first: event detail view was called from main fragment (event id != null)
            // scnd: we have created a new person and go back to event detail view (newPerson != null)
            eventId = bundle.getInt("eventId", -1);
            newPerson = (Person) bundle.getSerializable("newPerson");
            statusCode = bundle.getInt("statusCode");
        } else
            eventId = -1;

        if (eventId < 0) {
            Log.e("Error", "Team Id not found.");
            return;
        }

        Realm myDb = RealmHelper.getRealmInstance();
        RealmResults<Person> allPersons = myDb.where(Person.class).equalTo("teamId", eventId).findAll();

        for (Person p : allPersons) {
            dataSet.add(p);
        }

        rvTeamView = (RecyclerView) view.findViewById(R.id.rvTeam);
        rvTeamView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        rvTeamView.setLayoutManager(mLayoutManager);
        teamListAdapter = new RvTeamListAdapter(dataSet);
        rvTeamView.setAdapter(teamListAdapter);

        if (statusCode == REQUESTCODE_MEMBER_CREATED && newPerson != null) {
            showStatusInToast(REQUESTCODE_MEMBER_CREATED);
            updateMemberListAdapter();
        }

        if (statusCode == REQUESTCODE_MEMBER_EDITED)
        {
            showStatusInToast(REQUESTCODE_MEMBER_EDITED);
            updateMemberListAdapter();

        }
        else if (statusCode == REQUESTCODE_DELETE_MEMBER)
        {
            showStatusInToast(REQUESTCODE_DELETE_MEMBER);
            updateMemberListAdapter();
        }

        fabNewTeamMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback = (MainActivity) getActivity();
                mCallback.openNewPersonView(eventId);
            }
        });

        btnGenerateTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataSet.size() == 0) {
                    Toast.makeText(activity.getApplicationContext(), "No team members available to generate a team with.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void showStatusInToast(int statusCode) {
        String message = "";
        // show a toast when person was edited oder deleted.
        if (statusCode == REQUESTCODE_DELETE_MEMBER)
            message = "Person successfully deleted.";
        else if (statusCode == REQUESTCODE_MEMBER_EDITED)
            message = "Person successfully edited.";
        else if (statusCode == REQUESTCODE_MEMBER_CREATED)
            message = "Person successfully created.";
        Toast.makeText(getActivity(), message,
                Toast.LENGTH_LONG).show();
    }

    private void updateMemberListAdapter()
    {
        if (teamListAdapter != null)
            teamListAdapter.notifyDataSetChanged();
        else
            Log.e("TeamBuilder","Cannot update member list adapter. Adapter is null.");
    }
}
