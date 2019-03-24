package com.magesh.rideshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class fragmentupcoming extends Fragment {

    View v;

    RecyclerViewAdapter recyclerViewAdapter;

    private RecyclerView recyclerView;

    List<upcoming> getListupcoming;
    List<upcoming> listupcoming;

    DatabaseReference databaseReference1, databaseReference2, dbref;

    public fragmentupcoming(){

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final String cu = user.getUid();

        listupcoming = new ArrayList<>();
        getListupcoming = new ArrayList<>();

        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("offers");
        databaseReference1.orderByChild("uid").equalTo(cu).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String ori = data.child("ori").getValue(String.class);
                    String des = data.child("des").getValue(String.class);
                    String dor = data.child("dor").getValue(String.class);
                    String sa = data.child("sa").getValue(String.class);
                    String offorreq = "offered";
                    String offorreqid = data.getKey().toString();
                    getListupcoming.add(new upcoming(ori, des, dor, sa, offorreq, offorreqid));
                    getListupcoming.sort(Comparator.comparing(upcoming::getDor));
                }
                listupcoming.clear();
                listupcoming.addAll(getListupcoming);
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("requests");
        databaseReference2.orderByChild("uid").equalTo(cu).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    /*listupcoming.add(new upcoming(data.child("pic").getValue(String.class),data.child("dro").getValue(String.class),
                            data.child("dor").getValue(String.class),data.child("sr").getValue(String.class),"requested"));*/
                    String pic = data.child("pic").getValue(String.class);
                    String dro = data.child("dro").getValue(String.class);
                    String dor = data.child("dor").getValue(String.class);
                    String sr = data.child("sr").getValue(String.class);
                    String offorreq = "requested";
                    String offorreqid = data.getKey().toString();
                    getListupcoming.add(new upcoming(pic, dro, dor, sr, offorreq, offorreqid));
                    getListupcoming.sort(Comparator.comparing(upcoming::getDor));
                }
                listupcoming.clear();
                listupcoming.addAll(getListupcoming);
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*listupcoming.add(new upcoming("dummy","dummy","22-12-2019","6","requested"));
        listupcoming.add(new upcoming("dummy","dummy","20-12-2019","6","offered"));
        listupcoming.add(new upcoming("dummy","dummy","21-12-2019","6","requested"));
        listupcoming.add(new upcoming("dummy","dummy","23-12-2019","6","offered"));*/

        //listupcoming.sort(Comparator.comparing(upcoming::getDor));

        v = inflater.inflate(R.layout.upcoming,container,false);
        recyclerView = v.findViewById(R.id.upcomingrcview);
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), listupcoming, new RecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onViewRide(View v, int i) {
                String oorr = listupcoming.get(i).getOfforreq();
                String oorrid = listupcoming.get(i).getOfforreqid();
                if (oorr.equals("offered")){

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("offers").child(oorrid);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            double orilat = dataSnapshot.child("orilat").getValue(Double.class);
                            double orilng = dataSnapshot.child("orilng").getValue(Double.class);
                            double deslat = dataSnapshot.child("deslat").getValue(Double.class);
                            double deslng = dataSnapshot.child("deslng").getValue(Double.class);

                            Bundle b = new Bundle();
                            b.putString("oid",oorrid);
                            b.putDouble("orilat",orilat);
                            b.putDouble("orilng",orilng);
                            b.putDouble("deslat",deslat);
                            b.putDouble("deslng",deslng);
                            startActivity(new Intent(getContext(),Mapfordriver.class).putExtras(b));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("requests").child(oorrid);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            double orilat = dataSnapshot.child("piclat").getValue(Double.class);
                            double orilng = dataSnapshot.child("piclng").getValue(Double.class);
                            double deslat = dataSnapshot.child("drolat").getValue(Double.class);
                            double deslng = dataSnapshot.child("drolng").getValue(Double.class);

                            Bundle b1 = new Bundle();
                            b1.putString("reqid",oorrid);
                            b1.putDouble("orilat",orilat);
                            b1.putDouble("orilng",orilng);
                            b1.putDouble("deslat",deslat);
                            b1.putDouble("deslng",deslng);
                            startActivity(new Intent(getContext(),Mapforrequestor.class).putExtras(b1));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
