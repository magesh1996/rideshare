package com.magesh.rideshare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OfferrideActivity extends AppCompatActivity implements View.OnClickListener {

    private int mYear, mMonth, mDay;
    final Calendar myCalender = Calendar.getInstance();

    EditText editText2;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offerride);

        editText2 = findViewById(R.id.editText2);
        button = findViewById(R.id.button);

        editText2.setOnClickListener(this);
        button.setOnClickListener(this);

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
            startActivity(new Intent(this, DrivermapActivity.class));
        }

    }
}