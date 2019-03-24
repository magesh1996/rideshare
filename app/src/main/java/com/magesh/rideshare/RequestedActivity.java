package com.magesh.rideshare;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestedActivity extends AppCompatActivity {

    Requestedrvadapter requestedrvadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        String oid = b.getString("oid");
        String userid = b.getString("userid");

        List<requested> list = new ArrayList<>();

        DatabaseReference dbref;
        dbref = FirebaseDatabase.getInstance().getReference().child("offers").child(oid).child("reqs");
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String uid = data.child("requestorid").getValue(String.class);
                    String pic = data.child("pickup").getValue(String.class);
                    String dro = data.child("dropoff").getValue(String.class);
                    String sr = data.child("sr").getValue(String.class);

                    list.add(new requested(uid,pic,dro,sr));
                    requestedrvadapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //list.add(new requested("dummy","dummy","dummy","dummy"));
        RecyclerView recyclerView = findViewById(R.id.listofrequestsrv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestedrvadapter = new Requestedrvadapter(this, list, new Requestedrvadapter.ItemClickListener() {
            @Override
            public void onaccept(View v, int i) {
                String ruid = list.get(i).getUid();
                String pic = list.get(i).getPic();
                String dro = list.get(i).getDro();
                String sr = list.get(i).getSr();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("offers").child(oid).child("reqs").child(ruid).child("aord");
                databaseReference.setValue("accepted");

            }
        });

        recyclerView.setAdapter(requestedrvadapter);

    }
}
