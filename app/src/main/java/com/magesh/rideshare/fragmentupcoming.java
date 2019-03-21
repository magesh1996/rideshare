package com.magesh.rideshare;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    List<upcoming> listupcoming;

    DatabaseReference databaseReference1, databaseReference2;

    public fragmentupcoming(){

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final String cu = user.getUid();

        listupcoming = new ArrayList<>();
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
                    listupcoming.add(new upcoming(ori, des, dor, sa, offorreq));
                    listupcoming.sort(Comparator.comparing(upcoming::getDor));
                }
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
                    listupcoming.add(new upcoming(pic, dro, dor, sr, offorreq));
                    listupcoming.sort(Comparator.comparing(upcoming::getDor));
                }
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
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(),listupcoming);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
