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
public class TeamListOverviewFragment extends Fragment {

    OnEventClickedForDetailViewListener mCallback;

    public interface OnEventClickedForDetailViewListener {
        void openEventDetailView(int eventId);
        void openNewPersonView(int eventId);
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
    private final int REQUESTCODE_NEWTEAMMEMBER = 100;
    private final int REQUESTCODE_EDITTEAMMEMBER = 101;
    private final int REQUESTCODE_DELETE_MEMBER = 102;


    public TeamListOverviewFragment() {
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
        fabNewTeamMember = (FloatingActionButton) view.findViewById(R.id.fabnewTeamMember);
        btnGenerateTeams = (Button) view.findViewById(R.id.btnGenerateTeams);

        final int teamId;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            teamId = bundle.getInt("teamId", -1);
            if (teamId < 0) {
                Log.e("Error", "Team Id not found.");
                return;
            }
        }
        else
            teamId = -1;


        fabNewTeamMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo open new fragment, not new activity. use fragment manager
                mCallback = (MainActivity) getActivity();
                mCallback.openNewPersonView(teamId);
//                Intent intent = new Intent(activity.getApplicationContext(), CreatePersonActivity.class);
//                intent.putExtra("teamId",teamId);
//                startActivityForResult(intent, REQUESTCODE_NEWTEAMMEMBER);
            }
        });

        btnGenerateTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataSet.size() == 0) {
                    Toast.makeText(activity.getApplicationContext(), "No team members available to generate a team with.", Toast.LENGTH_SHORT).show();
                    return;
                }
//                  // todo open new fragment, not new activity. use fragment manager
//                Intent intent = new Intent(activity.getApplicationContext(), TeamResultActivity.class);
//                intent.putExtra("currentTeamId", teamId);
//                startActivity(intent);
            }
        });

        Realm myDb = RealmHelper.getRealmInstance();
        RealmResults<Person> allPersons = myDb.where(Person.class).equalTo("teamId", teamId).findAll();

        for (Person p : allPersons) {
            dataSet.add(p);
        }

        rvTeamView = (RecyclerView) activity.findViewById(R.id.rvTeam);
        rvTeamView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(activity);
        rvTeamView.setLayoutManager(mLayoutManager);
        teamListAdapter = new RvTeamListAdapter(dataSet);
        rvTeamView.setAdapter(teamListAdapter);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUESTCODE_NEWTEAMMEMBER)
//        {
//            if (resultCode == REQUESTCODE_NEWTEAMMEMBER)
//            {
//                if (data != null)
//                {
//                    Person newPerson = (Person) data.getSerializableExtra("newPersonResult");
//                    Log.i("TeamBuilder","Got new Person from CreateTeamActivity named " + newPerson.getFirstName() + " " + newPerson.getLastName());
//                    dataSet.add(newPerson);
//                }
//            }
//            else if (resultCode == REQUESTCODE_DELETE_MEMBER)
//            {
//                // remove deleted person from dataset
//                int id = (int) data.getExtras().getInt("deletePerson");
//                Log.i("TeamBuilder","Id of deleted person is " + id + ". Remove person from dataset..");
//
//                for (Person p : dataSet)
//                {
//                    try
//                    {
//                        if (p.getId() == id)
//                        {
//                            dataSet.remove(p);
//                            break;
//                        }
//                    }
//                    catch(Exception exc)
//                    {
//                        Log.e("TeamBuilder","Failed deleting person from dataset. Details: " + exc.getMessage());
//                    }
//                }
//            }
//
//            teamListAdapter.notifyDataSetChanged();
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    protected void onDestroy() {
//
//        //close realm
//        RealmHelper.getRealmInstance().close();
//        super.onDestroy();
//    }
}
