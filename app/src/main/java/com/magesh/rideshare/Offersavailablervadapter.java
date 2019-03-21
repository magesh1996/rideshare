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

public class Offersavailablervadapter extends RecyclerView.Adapter<Offersavailablervadapter.ViewHolder> {

    private List<availableoffers> list;
    private LayoutInflater layoutInflater;
    ItemClickListener mListener;

    Offersavailablervadapter(Context context, List<availableoffers> list, ItemClickListener itemClickListener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
        mListener = itemClickListener;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.listofoffersrv_row,viewGroup,false);
        return new ViewHolder(view, mListener );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.oid.setText(list.get(i).getOid());
        viewHolder.rname.setText(list.get(i).getRname());
        viewHolder.start.setText(list.get(i).getStart());
        viewHolder.end.setText(list.get(i).getEnd());
        viewHolder.cob.setText(list.get(i).getCob());
        viewHolder.sa.setText(list.get(i).getSa());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //view holder

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemClickListener mListener;

        private TextView oid;
        private TextView rname;
        private TextView start;
        private TextView end;
        private TextView cob;
        private TextView sa;
        private Button req;
        private Button can;

        public ViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            this.mListener = itemClickListener;

            oid = itemView.findViewById(R.id.oid);
            rname = itemView.findViewById(R.id.rname);
            start = itemView.findViewById(R.id.startloc);
            end = itemView.findViewById(R.id.endloc);
            cob = itemView.findViewById(R.id.cob);
            sa = itemView.findViewById(R.id.sa);

            can = itemView.findViewById(R.id.can);
            req = itemView.findViewById(R.id.req);
            req.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onreq(v, getAdapterPosition());
                    req.setText("REQUESTED");
                    req.setEnabled(false);
                    can.setEnabled(true);
                }
            });

        }
    }

    public interface ItemClickListener {
        void onreq(View view, int i);
    }
}
