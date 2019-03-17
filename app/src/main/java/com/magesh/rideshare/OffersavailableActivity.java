package com.magesh.rideshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.geofire.GeoFire;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OffersavailableActivity extends AppCompatActivity implements Offersavailablervadapter.ItemClickListener {

    Offersavailablervadapter offersavailablervadapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offersavailable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();

        String pic = i.getStringExtra("pic");
        String dro = i.getStringExtra("dro");
        String dor = i.getStringExtra("dor");
        String sr = i.getStringExtra("sr");


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String usr = user.getUid();

        List<availableoffers> list = new ArrayList<>();

        //list.add(new availableoffers("jon snow", "dummy", "dummy", "jetta", "2" ));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("offers");
        databaseReference.orderByChild("des").equalTo(dro).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    String ori = data.child("ori").getValue(String.class);
                    String des = data.child("des").getValue(String.class);
                    String offerdor = data.child("dor").getValue(String.class);
                    String cob = data.child("cob").getValue(String.class);
                    String sa = data.child("sa").getValue(String.class);
                    String ruid = data.child("uid").getValue(String.class);

                    if (ori.equals(pic) && des.equals(dro) && offerdor.equals(dor) && sr.equals(sr)) {
                        list.add(new availableoffers(ruid, ori, des, cob, sa));
                    }
                    offersavailablervadapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        RecyclerView recyclerView = findViewById(R.id.listofoffersrv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        offersavailablervadapter = new Offersavailablervadapter(this,list);
        offersavailablervadapter.setClickListener(this);
        recyclerView.setAdapter(offersavailablervadapter);

    }

    @Override
    public void onItemClick(View view, int i) {

    }
}
