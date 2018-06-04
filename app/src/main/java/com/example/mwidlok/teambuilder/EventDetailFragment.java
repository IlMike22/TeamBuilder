package com.example.mwidlok.teambuilder;


import android.app.Dialog;
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
import android.widget.TextView;
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

    final private String TAG = "TeamBuilder";
    private OnEventClickedForDetailViewListener mCallback;

    public interface OnEventClickedForDetailViewListener {
        void openEventDetailView(int eventId);

        void openNewPersonView(int eventId);

        void openPersonDetailView(Person person, int eventId);

        void openTeamResultView(int eventId);
    }

    final private ArrayList<Person> dataSet = new ArrayList<>();
    private RvTeamListAdapter teamListAdapter;

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
        mCallback = (MainActivity) getActivity();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final int eventId;
        Person newPerson = null;
        int statusCode = 0;


        FloatingActionButton fabNewTeamMember = (FloatingActionButton) view.findViewById(R.id.fabnewTeamMember);
        Button btnGenerateTeams = (Button) view.findViewById(R.id.btnGenerateTeams);
        TextView tvNoEventMembersInfo = (TextView) view.findViewById(R.id.tvNoEventMemberInfo);

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
            Log.e(TAG, "Event Id not found.");
            return;
        }

        Realm myDb = RealmHelper.getRealmInstance();
        RealmResults<Person> allPersons = myDb.where(Person.class).equalTo("teamId", eventId).findAll();

        if (allPersons.size() == 0)
        {
            // at the moment we dont have any event members
            if (tvNoEventMembersInfo != null)
            {
                tvNoEventMembersInfo.setVisibility(View.VISIBLE);
                tvNoEventMembersInfo.setText(R.string.event_detail_msg_no_members);
            }
            else
                Log.e(TAG, "Element tvNoEventMembersInfo is null.");

        }
        else
        {
            if (tvNoEventMembersInfo != null)
                tvNoEventMembersInfo.setVisibility(View.GONE);
            else
                Log.e(TAG, "Element tvNoEventMembersInfo is null.");
        }


        dataSet.addAll(allPersons);

        RecyclerView rvTeamView = (RecyclerView) view.findViewById(R.id.rvTeam);
        rvTeamView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvTeamView.setLayoutManager(mLayoutManager);
        teamListAdapter = new RvTeamListAdapter(dataSet);
        rvTeamView.setAdapter(teamListAdapter);

        if (statusCode == REQUESTCODE_MEMBER_CREATED && newPerson != null) {
            showStatusInToast(REQUESTCODE_MEMBER_CREATED);
            updateMemberListAdapter();
        }

        if (statusCode == REQUESTCODE_MEMBER_EDITED) {
            showStatusInToast(REQUESTCODE_MEMBER_EDITED);
            updateMemberListAdapter();

        } else if (statusCode == REQUESTCODE_DELETE_MEMBER) {
            showStatusInToast(REQUESTCODE_DELETE_MEMBER);
            updateMemberListAdapter();
        }

        fabNewTeamMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null)
                    mCallback.openNewPersonView(eventId);
                else
                    Log.e(TAG, "mCallback is null. Cannot open newPersonView.");
            }
        });

        btnGenerateTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataSet.size() < 4) {
                        // not enough persons created to generate a result.
                        DialogHelper.showStandardDialog(getString(R.string.msg_not_enough_persons_title), getString(R.string.msg_not_enough_persons_detail), false, getActivity(), 0);

                } else {
                    if (mCallback != null)
                        mCallback.openTeamResultView(eventId);
                    else
                        Log.e(TAG, "mCallback is null. Cannot open TeamResultView()");
                }
            }
        });
    }

    private void showStatusInToast(int statusCode) {
        String message = "";

        switch(statusCode)
        {
            case REQUESTCODE_DELETE_MEMBER:
                    message = getString(R.string.eventDetail_person_deleted_msg);
                break;
            case REQUESTCODE_MEMBER_CREATED:
                message = getString(R.string.eventDetail_person_created_msg);
                break;
            case REQUESTCODE_MEMBER_EDITED:
                message = getString(R.string.eventDetail_person_edited_msg);
                break;
        }

        Toast.makeText(getActivity(), message,
                Toast.LENGTH_LONG).show();
    }

    private void updateMemberListAdapter() {
        if (teamListAdapter != null)
            teamListAdapter.notifyDataSetChanged();
        else
            Log.e(TAG, "Cannot update member list adapter. Adapter is null.");
    }
}
