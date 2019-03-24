package com.magesh.rideshare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;

import org.w3c.dom.Text;

import java.util.List;

public class Requestedrvadapter extends RecyclerView.Adapter<Requestedrvadapter.ViewHolder> {

    private List<requested> list;
    private LayoutInflater layoutInflater;
    ItemClickListener mListener;

    public Requestedrvadapter(Context context, List<requested> list, ItemClickListener mListener) {
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.listofrequestsrv_row, viewGroup, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.rname.setText(list.get(i).getUid());
        viewHolder.start.setText(list.get(i).getPic());
        viewHolder.end.setText(list.get(i).getDro());
        viewHolder.sr.setText(list.get(i).getSr());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemClickListener mListener;

        private TextView rname;
        private TextView start;
        private TextView end;
        private TextView sr;
        private Button acc;
        private Button dec;

        public ViewHolder(@NonNull View itemView, ItemClickListener mListener) {
            super(itemView);
            this.mListener = mListener;

            rname = itemView.findViewById(R.id.rname);
            start = itemView.findViewById(R.id.startloc);
            end = itemView.findViewById(R.id.endloc);
            sr = itemView.findViewById(R.id.sr);

            acc = itemView.findViewById(R.id.acc);
            dec = itemView.findViewById(R.id.dec);

            acc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onaccept(v, getAdapterPosition());
                    acc.setText("ACCEPTED");
                    acc.setEnabled(false);
                    dec.setEnabled(true);
                }
            });

        }
    }


    public interface ItemClickListener{
        void onaccept(View v, int i);
    }

}
