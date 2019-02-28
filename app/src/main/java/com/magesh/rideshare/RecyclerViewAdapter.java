package com.magesh.rideshare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context context;
    List<upcoming> upcomingList;

    public RecyclerViewAdapter(Context context, List<upcoming> upcomingList) {
        this.context = context;
        this.upcomingList = upcomingList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.itemupcoming,viewGroup,false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.tvstart.setText(upcomingList.get(i).getOri());
        myViewHolder.tvend.setText(upcomingList.get(i).getDes());
        myViewHolder.tvdor.setText(upcomingList.get(i).getDor());
        myViewHolder.tvnos.setText(upcomingList.get(i).getSa());
        myViewHolder.tvofforreq.setText("offered");


    }

    @Override
    public int getItemCount() {
        return upcomingList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvstart;
        private TextView tvend;
        private TextView tvdor;
        private TextView tvnos;
        private TextView tvofforreq;

        public MyViewHolder(View itemView){
            super(itemView);

            tvstart = itemView.findViewById(R.id.startloc);
            tvend = itemView.findViewById(R.id.endloc);
            tvdor = itemView.findViewById(R.id.dor);
            tvnos = itemView.findViewById(R.id.nos);
            tvofforreq = itemView.findViewById(R.id.offorreq);


        }

    }
}
