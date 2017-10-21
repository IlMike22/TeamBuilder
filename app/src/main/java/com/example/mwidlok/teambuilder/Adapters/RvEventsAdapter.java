package com.example.mwidlok.teambuilder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mwidlok.teambuilder.RealmHelper;
import com.example.mwidlok.teambuilder.Model.Team;
import com.example.mwidlok.teambuilder.R;
import com.example.mwidlok.teambuilder.TeamListActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * Created by MWidlok on 26.06.2017.
 */

public class RvEventsAdapter extends RecyclerView.Adapter<RvEventsAdapter.ViewHolder>
{
    private List<String> mDataSet = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public LinearLayout llEventCardView;
        public ViewHolder(LinearLayout llCardView)
        {
            super(llCardView);
            llEventCardView = llCardView;
        }
    }

    // suitable constructor for data transmission

    public RvEventsAdapter(List<String> myDataSet)
    {
        mDataSet = myDataSet;
    }

    @Override
    public RvEventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LinearLayout llCardView  = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.events_cardview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(llCardView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RvEventsAdapter.ViewHolder holder, final int position) {

        LinearLayout llEventList = holder.llEventCardView;
        TextView tvEventItem = (TextView) llEventList.findViewById(R.id.tvEventName);
        tvEventItem.setText(mDataSet.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // now open new Activity
                showEventDetailActivity(v.getContext(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private void showEventDetailActivity(Context context, int position)
    {
        Realm myRealm = RealmHelper.getRealmInstance();
        RealmQuery<Team> currentTeam = myRealm.where(Team.class).like("name",mDataSet.get(position));
        // to be continued.
        Intent i = new Intent(context, TeamListActivity.class);
        context.startActivity(i);
    }
}
