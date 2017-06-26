package com.example.mwidlok.teambuilder.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.mwidlok.teambuilder.R;

/**
 * Created by MWidlok on 26.06.2017.
 */

public class RvEventsAdapter extends RecyclerView.Adapter<RvEventsAdapter.ViewHolder>
{
    private String[] mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tvEventName;

        public ViewHolder(TextView tv)
        {
            super(tv);
            tvEventName = tv;
        }
    }

    // suitable constructor for data transmission

    public RvEventsAdapter(String[] myDataSet)
    {
        mDataSet = myDataSet;
    }

    @Override
    public RvEventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        TextView currentTv = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.id.tvEventName, parent, false);

        ViewHolder viewHolder = new ViewHolder(currentTv);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RvEventsAdapter.ViewHolder holder, int position) {

        holder.tvEventName.setText(mDataSet[position]);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}
