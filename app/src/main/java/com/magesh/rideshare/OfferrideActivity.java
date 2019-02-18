package com.magesh.rideshare;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class OfferrideActivity extends AppCompatActivity implements View.OnClickListener{

    int AUTOCOMPLETE_REQUEST_CODE_ORIGIN = 1;
    int AUTOCOMPLETE_REQUEST_CODE_DESTINATION = 2;

    private int mYear, mMonth, mDay;
    final Calendar myCalender = Calendar.getInstance();

    EditText editText, editText1, editText2, editText3, editText4, editText5;
    Button button;

    String origin, orilatlng;
    String destination, deslatlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offerride);

        editText = findViewById(R.id.editText);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
        button = findViewById(R.id.button);

        editText.setOnClickListener(this);
        editText1.setOnClickListener(this);
        editText2.setOnClickListener(this);
        editText3.setOnClickListener(this);
        editText4.setOnClickListener(this);
        button.setOnClickListener(this);

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

        if (v == button){
            offerRide();
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
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_ORIGIN);


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
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_DESTINATION);


        }

        if (v == editText3){

            Calendar timeDialog = Calendar.getInstance();
            int hour = timeDialog.get(Calendar.HOUR_OF_DAY);
            int minute = timeDialog.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    editText3.setText(hourOfDay + ":" + minute);
                }
            },hour,minute,true);

            timePickerDialog.show();
        }

        if (v == editText4){

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
                    editText4.setText(seats);
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


    }

    private void offerRide() {
        String ori = editText.getText().toString();
        String des = editText1.getText().toString();
        String dor = editText2.getText().toString();
        String dep = editText3.getText().toString();
        String sa = editText4.getText().toString();
        String car = editText5.getText().toString();

        if(ori.isEmpty() || des.isEmpty() || dor.isEmpty() || dep.isEmpty() || sa.isEmpty() || car.isEmpty()){
            showMessage("Please verify all fields");
        }

        else {
            Offer offer = null;
            offer = new Offer(ori, des, dor, dep, sa, car, orilatlng, deslatlng);

            FirebaseDatabase.getInstance().getReference().child("offers").push()
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(offer).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        showMessage("You offered your ride");

                    } else {
                        showMessage("Can't offer your ride");
                    }
                }
            });
            startActivity(new Intent(this, Mapfordriver.class));
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_ORIGIN) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                origin = place.getName().toString();
                editText.setText(origin);
                orilatlng = place.getLatLng().toString();
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                status.getStatusMessage();
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_DESTINATION) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                destination = place.getName().toString();
                editText1.setText(destination);
                deslatlng = place.getLatLng().toString();

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
