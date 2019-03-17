package com.magesh.rideshare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

public class Offersavailablervadapter extends RecyclerView.Adapter<Offersavailablervadapter.ViewHolder> {

    private List<availableoffers> list;
    private LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;

    Offersavailablervadapter(Context context, List<availableoffers> list) {
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.listofoffersrv_row,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView rname;
        private TextView start;
        private TextView end;
        private TextView cob;
        private TextView sa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rname = itemView.findViewById(R.id.rname);
            start = itemView.findViewById(R.id.startloc);
            end = itemView.findViewById(R.id.endloc);
            cob = itemView.findViewById(R.id.cob);
            sa = itemView.findViewById(R.id.sa);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void setClickListener(OffersavailableActivity offersavailableActivity) {

    }

    public interface ItemClickListener {
        void onItemClick(View view, int i);

    }
}
