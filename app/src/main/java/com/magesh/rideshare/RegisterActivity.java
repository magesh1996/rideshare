package com.magesh.rideshare;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView userPhoto;
    static int preqCode = 1;
    static int reqCode = 1;
    Uri pickedImgUri;

    private EditText name,email,pwd,mobile;
    private Button regbutton;
    private ProgressBar loadingprogress;
    private TextView gotologin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.editText);
        email = findViewById(R.id.editText1);
        pwd = findViewById(R.id.editText2);
        mobile = findViewById(R.id.editText3);

        regbutton = findViewById(R.id.button);
        gotologin = findViewById(R.id.textView1);

        regbutton.setOnClickListener(this);
        gotologin.setOnClickListener(this);

        loadingprogress = findViewById(R.id.progressBar);
        loadingprogress.setVisibility(View.INVISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void registerUser(){

        final String uname = name.getText().toString();
        final String uemail = email.getText().toString();
        String upwd = pwd.getText().toString();
        final String umobile = mobile.getText().toString();

        if(uname.isEmpty() || uemail.isEmpty() || upwd.isEmpty() || umobile.isEmpty()) {

            showMessage("Please verify all fields");
            regbutton.setVisibility(View.VISIBLE);
            loadingprogress.setVisibility(View.INVISIBLE);
        }

        else {
            firebaseAuth.createUserWithEmailAndPassword(uemail,upwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    Users users = null;
                    if (task.isSuccessful()) {
                        users = new Users(uname, uemail, umobile);
                        FirebaseDatabase.getInstance().getReference("users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    showMessage("Successfully registered");
                                } else {
                                    showMessage("Registration failed");
                                }
                            }
                        });
                    }
                    else {
                        showMessage("Failed");
                    }
                }
            });
        }


    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        if (v == regbutton){
            registerUser();
        }
        if (v == gotologin){
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}