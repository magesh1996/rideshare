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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class fragmentupcoming extends Fragment {

    View v;

    final String cu = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private RecyclerView recyclerView;

    private List<upcoming> listupcoming;

    DatabaseReference databaseReference;

    public fragmentupcoming(){

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.upcoming,container,false);
        recyclerView = v.findViewById(R.id.upcomingrcview);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getContext(),listupcoming);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listupcoming = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("offers");
        databaseReference.orderByChild("uid").equalTo(cu).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String ori = data.child("ori").getValue(String.class);
                    String des = data.child("des").getValue(String.class);
                    String dor = data.child("dor").getValue(String.class);
                    String sa = data.child("sa").getValue(String.class);
                    listupcoming.add(new upcoming(ori, des, dor, sa));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //listupcoming.add(new upcoming("dummy","dummy","20-12-2019","6"));

    }

}
