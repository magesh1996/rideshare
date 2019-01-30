package com.magesh.rideshare;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class header extends AppCompatActivity {

    ImageView headerpropic;
    TextView headerproname;

    FirebaseAuth firebaseAuth;
    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header);

        headerpropic = findViewById(R.id.headerpropic);
        headerproname = findViewById(R.id.headerproname);

        firebaseAuth = FirebaseAuth.getInstance();

        dbref = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getCurrentUser().getUid());

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                headerproname.setText(dataSnapshot.child("name").getValue().toString());
                String link = dataSnapshot.child("propic").getValue().toString();
                Picasso.get().load(link).into(headerpropic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
