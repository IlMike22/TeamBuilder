package com.example.mwidlok.teambuilder.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mwidlok.teambuilder.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 19.09.2017.
 */

public class RvTeamListAdapter extends RecyclerView.Adapter<RvTeamListAdapter.ViewHolder> {

    private List<String> mDataSet = new ArrayList<String>();

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public LinearLayout llMemberCardView;
        public ViewHolder(LinearLayout llCardView)
        {
            super(llCardView);
            llMemberCardView = llCardView;
        }
    }

    public RvTeamListAdapter(List<String> myDataSet)
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
        TextView tvEventItem = (TextView) llEventList.findViewById(R.id.tvMemberName);
        tvEventItem.setText(mDataSet.get(position));

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
    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
