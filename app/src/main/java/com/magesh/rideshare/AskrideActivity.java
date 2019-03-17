package com.magesh.rideshare;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AskrideActivity extends AppCompatActivity implements View.OnClickListener {

    int AUTOCOMPLETE_REQUEST_CODE_PICKUP = 1;
    int AUTOCOMPLETE_REQUEST_CODE_DROPOFF = 2;

    private int mYear, mMonth, mDay;
    final Calendar myCalender = Calendar.getInstance();

    EditText editText, editText1, editText2, editText3;
    Button button;

    String pickup, dropoff;
    LatLng piclatlng, drolatlng;
    double piclat, piclng, drolat, drolng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_askride);

        editText = findViewById(R.id.editText);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        button = findViewById(R.id.button);

        editText.setOnClickListener(this);
        editText1.setOnClickListener(this);
        editText2.setOnClickListener(this);
        editText3.setOnClickListener(this);
        button.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Places.initialize(getApplicationContext(), "AIzaSyBNOGGHYlXOJ44JTyGYAMXCKXTnheWtouk");

    }

    @Override
    public void onClick(View v) {

        if (v == editText2){

            mYear = myCalender.get(Calendar.YEAR);
            mMonth = myCalender.get(Calendar.MONTH);
            mDay = myCalender.get(Calendar.DAY_OF_MONTH);

            editText2 = findViewById(R.id.editText2);
            button = findViewById(R.id.button);

            final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    editText2.setText(dayOfMonth + "-" + (month+1) + "-" + year);
                }

            }, mYear, mMonth, mDay);

            datePickerDialog.show();

        }

        if (v == editText){
            // Set the fields to specify which types of place data to
            // return after the user has made a selection.
            List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, fields)
                    .setCountry("IN")
                    .build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_PICKUP);


        }

        if (v == editText1){
            // Set the fields to specify which types of place data to
            // return after the user has made a selection.
            List<Place.Field> fields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, fields)
                    .setCountry("IN")
                    .build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_DROPOFF);


        }

        if (v == editText3){

            final AlertDialog.Builder d = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog, null);
            d.setView(dialogView);
            final NumberPicker numberPicker = dialogView.findViewById(R.id.numberpicker);
            numberPicker.setMinValue(1);
            numberPicker.setMaxValue(10);
            numberPicker.setWrapSelectorWheel(false);
            d.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    final String seats = String.valueOf(numberPicker.getValue());
                    editText3.setText(seats);
                }
            });
            d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alertDialog = d.create();
            alertDialog.show();
        }

        if (v == button){
            askRide();
        }

    }

    private void askRide() {
        String pic = editText.getText().toString();
        String dro = editText1.getText().toString();
        String dor = editText2.getText().toString();
        String sr = editText3.getText().toString();

        if(pic.isEmpty() || dro.isEmpty() || dor.isEmpty() || sr.isEmpty()){
            showMessage("Please verify all fields");
        }

        else {
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
                        showMessage("Request has made successfully");

                    } else {
                        showMessage("Can't request for ride");
                    }
                }
            });
            startActivity(new Intent(this, OffersavailableActivity.class).putExtra("pic",pic).putExtra("dro",dro).putExtra("dor",dor).putExtra("sr",sr));
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_PICKUP) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                pickup = place.getName().toString();
                editText.setText(pickup);
                piclatlng = place.getLatLng();
                piclat = piclatlng.latitude;
                piclng = piclatlng.longitude;
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                status.getStatusMessage();
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_DROPOFF) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                dropoff = place.getName().toString();
                editText1.setText(dropoff);
                drolatlng = place.getLatLng();
                drolat = drolatlng.latitude;
                drolng = drolatlng.longitude;
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                status.getStatusMessage();
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}