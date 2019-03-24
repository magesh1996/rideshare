package com.magesh.rideshare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context context;
    List<upcoming> upcomingList;
    ItemClickListener mListener;

    public RecyclerViewAdapter(Context context, List<upcoming> upcomingList, ItemClickListener itemClickListener) {
        this.context = context;
        this.upcomingList = upcomingList;
        this.mListener = itemClickListener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.itemupcoming,viewGroup,false);
        return new MyViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.tvstart.setText(upcomingList.get(i).getOri());
        myViewHolder.tvend.setText(upcomingList.get(i).getDes());
        myViewHolder.tvdor.setText(upcomingList.get(i).getDor());
        myViewHolder.tvnos.setText(upcomingList.get(i).getSa());
        myViewHolder.tvofforreq.setText(upcomingList.get(i).getOfforreq());
        myViewHolder.tvofforreqid.setText(upcomingList.get(i).getOfforreqid());

    }

    @Override
    public int getItemCount() {
        return upcomingList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private ItemClickListener mListener;

        private TextView tvstart;
        private TextView tvend;
        private TextView tvdor;
        private TextView tvnos;
        private TextView tvofforreq;
        private TextView tvofforreqid;
        private Button viewride;
        private Button cancel;

        public MyViewHolder(View itemView, ItemClickListener itemClickListener){
            super(itemView);
            this.mListener = itemClickListener;


            tvstart = itemView.findViewById(R.id.startloc);
            tvend = itemView.findViewById(R.id.endloc);
            tvdor = itemView.findViewById(R.id.dor);
            tvnos = itemView.findViewById(R.id.nos);
            tvofforreq = itemView.findViewById(R.id.offorreq);
            tvofforreqid = itemView.findViewById(R.id.offorreqid);
            viewride = itemView.findViewById(R.id.viewride);
            cancel = itemView.findViewById(R.id.cancel);

            viewride.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onViewRide(v, getAdapterPosition());

                }
            });


        }

    }

    public interface ItemClickListener{
        void onViewRide(View v, int i);
    }



}
