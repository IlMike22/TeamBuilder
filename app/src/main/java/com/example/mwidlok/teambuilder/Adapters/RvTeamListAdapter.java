package com.example.mwidlok.teambuilder.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.mwidlok.teambuilder.Model.Person;
import com.example.mwidlok.teambuilder.R;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Mike on 19.09.2017.
 */

public class RvTeamListAdapter extends RecyclerView.Adapter<RvTeamListAdapter.ViewHolder>  {

    private List<Person> mDataSet = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public LinearLayout llMemberCardView;
        public ViewHolder(LinearLayout llCardView)
        {
            super(llCardView);
            llMemberCardView = llCardView;
        }
    }

    public RvTeamListAdapter(List<Person> myDataSet)
    {
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
        String firstName = mDataSet.get(position).getFirstName().toString();
        String lastName = mDataSet.get(position).getLastName().toString();
        int age = mDataSet.get(position).getAge();
        int skillLevel = mDataSet.get(position).getSkillLevel();
        tvName.setText(firstName + " " + lastName);
        tvAge.setText("Alter: " + String.valueOf(age));
        tvSkillLevel.setText("Skill Level: " + String.valueOf(skillLevel));

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.i("TeamBuilder","User clicked Item " + position + " of Member List.");

                // now open new Activity for creating members and put in all data of selected member dataset
                //showMemberDetailActivity(v.getContext());
            }
        });

        //close realm
        Realm currentRealmInstance = Realm.getDefaultInstance();
        currentRealmInstance.close();
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


}
