package com.magesh.rideshare;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OffersavailableActivity extends AppCompatActivity {

    Offersavailablervadapter offersavailablervadapter;
    DatabaseReference databaseReference, dbref, dbref1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offersavailable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();

        String pic = b.getString("pic");
        String dro = b.getString("dro");
        String dor = b.getString("dor");
        String sr = b.getString("sr");
        int srint = Integer.parseInt(sr);
        Double piclat = b.getDouble("piclat");
        Double piclng = b.getDouble("piclng");
        Double drolat = b.getDouble("drolat");
        Double drolng = b.getDouble("drolng");


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String usr = user.getUid();

        List<availableoffers> list = new ArrayList<>();
        List<availableoffers> getlist = new ArrayList<>();

        //list.add(new availableoffers("jon snow", "dummy", "dummy", "jetta", "2" ));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("offers");
        databaseReference.orderByChild("des").equalTo(dro).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    String ori = data.child("ori").getValue(String.class);
                    String des = data.child("des").getValue(String.class);
                    String offerdor = data.child("dor").getValue(String.class);
                    String cob = data.child("cob").getValue(String.class);
                    String sa = data.child("sa").getValue(String.class);
                    int saint = Integer.parseInt(sa);
                    String ruid = data.child("uid").getValue(String.class);
                    String oid = data.getKey().toString();

                    if (ori.equals(pic) && des.equals(dro) && offerdor.equals(dor) && saint >= srint) {
                        getlist.add(new availableoffers(oid, ruid, ori, des, cob, sa));
                    }
                    list.clear();
                    list.addAll(getlist);
                    offersavailablervadapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),"search completed",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        RecyclerView recyclerView = findViewById(R.id.listofoffersrv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        offersavailablervadapter = new Offersavailablervadapter(this, list, new Offersavailablervadapter.ItemClickListener() {
            @Override
            public void onreq(View view, int i) {
                String offerid = list.get(i).getOid();
                String proid = list.get(i).getRname();
                String requestorid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                dbref1 = FirebaseDatabase.getInstance().getReference().child("offers").child(offerid).child("reqs").child(usr);
                //dbref = dbref1.child(pkey);
                Map<String, Object> requpdates = new HashMap<>();
                dbref1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {



                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Ask ask = null;
                            ask = new Ask(uid, pic, dro, dor, sr, piclat, piclng, drolat, drolng);

                            DatabaseReference databaseReference;
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("requests");

                            String pkey = databaseReference.push().getKey();

                            FirebaseDatabase.getInstance().getReference().child("requests").child(pkey)
                                    .setValue(ask).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        DatabaseReference databaseReference1;
                                        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("requests").child(pkey);
                                        GeoFire geoFire = new GeoFire(databaseReference1);
                                        geoFire.setLocation("ploc",new GeoLocation(piclat,piclng));
                                        geoFire.setLocation("dloc", new GeoLocation(drolat,drolng));
                                        Toast.makeText(getApplicationContext(),"requesting",Toast.LENGTH_SHORT).show();

                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"can't request for ride",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });




                            requpdates.put("aord", "waiting");
                            requpdates.put("requestid", pkey);
                            requpdates.put("requestorid", usr);
                            requpdates.put("pickup", pic);
                            requpdates.put("dropoff", dro);
                            requpdates.put("sr", sr);
                            dbref1.updateChildren(requpdates, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    Toast.makeText(getApplicationContext(),"requested "+proid,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"already requested",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                /*dbref1 = FirebaseDatabase.getInstance().getReference().child("waitingreq").child(offerid);
                dbref = FirebaseDatabase.getInstance().getReference().child("waitingreq").child(offerid).push();
                Map<String, Object> requpdates = new HashMap<>();
                dbref1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(getApplicationContext(),"already requested",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            requpdates.put("requestid", pkey);
                            requpdates.put("aord", "waiting");
                            dbref.updateChildren(requpdates, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    Toast.makeText(getApplicationContext(),"requested "+proid,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/

            }
        });
        //offersavailablervadapter.setClickListener(this);
        recyclerView.setAdapter(offersavailablervadapter);

    }

}