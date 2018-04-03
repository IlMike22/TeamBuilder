package com.example.mwidlok.teambuilder.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mwidlok.teambuilder.EventDetailFragment;
import com.example.mwidlok.teambuilder.MainActivity;
import com.example.mwidlok.teambuilder.Model.Person;
import com.example.mwidlok.teambuilder.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 19.09.2017.
 */

public class RvTeamListAdapter extends RecyclerView.Adapter<RvTeamListAdapter.ViewHolder> {

    private List<Person> mDataSet = new ArrayList<>();
    EventDetailFragment.OnEventClickedForDetailViewListener mCallback;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout llMemberCardView;

        public ViewHolder(LinearLayout llCardView) {
            super(llCardView);
            llMemberCardView = llCardView;
        }
    }

    public RvTeamListAdapter(List<Person> myDataSet) {
        mDataSet = myDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LinearLayout llCardView = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.member_cardview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(llCardView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        LinearLayout llEventList = holder.llMemberCardView;
        TextView tvName = (TextView) llEventList.findViewById(R.id.tvMemberName);
        TextView tvAge = (TextView) llEventList.findViewById(R.id.tvAge);
        TextView tvSkillLevel = (TextView) llEventList.findViewById(R.id.tvSkillLevel);

        final Person currentPerson = mDataSet.get(position);

        tvName.setText(currentPerson.getFirstName() + " " + currentPerson.getLastName());
        tvAge.setText(String.valueOf(currentPerson.getAge() + " Jahre alt"));
        tvSkillLevel.setText(String.valueOf(currentPerson.getSkillLevelDescription()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TeamBuilder", "User clicked Item " + position + " of Member List.");
                Log.i("TeamBuilder", "Clicked user is " + currentPerson.getFirstName() + " " + currentPerson.getLastName());
                // now open new Activity for creating members and put in all data of selected member dataset

                showPersonDetailFragment(v.getContext(), currentPerson, currentPerson.getEventId());

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private void showPersonDetailFragment(Context context, Person currentPerson, int eventId) {
        ((MainActivity) context).openPersonDetailView(currentPerson, eventId);
        // opens person detail view for showing and editing current clicked person in list.
        // todo open new fragment, not new activity. use fragment manager
        //mCallback = (EventDetailFragment.OnEventClickedForDetailViewListener) ((MainActivity) c;


//        Intent i = new Intent(context, CreatePersonActivity.class);
//        i.putExtra("currentPersonId", id);
//        i.putExtra("teamId", teamId);
//        ((TeamListActivity) context).startActivityForResult(i, 100);

    }
}
