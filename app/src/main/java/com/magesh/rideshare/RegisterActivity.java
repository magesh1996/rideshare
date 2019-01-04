package com.magesh.rideshare;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

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
        loadingprogress = findViewById(R.id.progressBar);
        loadingprogress.setVisibility(View.INVISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();

        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regbutton.setVisibility(View.INVISIBLE);
                loadingprogress.setVisibility(View.VISIBLE);
                final String uname = name.getText().toString();
                final String uemail = email.getText().toString();
                final String upwd = pwd.getText().toString();
                final String umobile = mobile.getText().toString();

                if(uname.isEmpty() || uemail.isEmpty() || upwd.isEmpty() || umobile.isEmpty()){
                    showMessage("Please verify all fields");
                    regbutton.setVisibility(View.VISIBLE);
                    loadingprogress.setVisibility(View.INVISIBLE);
                }
                else {
                    CreateUserAccount(uname,uemail,upwd);
                }

            }
        });

        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            }
        });


        userPhoto = findViewById(R.id.imageView);
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermission();
                }
                else{
                    openGallery();
                }
            }
        });

    }

    private void CreateUserAccount(final String uname, String uemail, String upwd) {
        firebaseAuth.createUserWithEmailAndPassword(uemail,upwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    showMessage("Account created");
                    updateUserInfo(uname,pickedImgUri,firebaseAuth.getCurrentUser());
                }
                else {
                    showMessage("Account creation failed"+task.getException().getMessage());
                    regbutton.setVisibility(View.VISIBLE);
                    loadingprogress.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void updateUserInfo(final String uname, Uri pickedImgUri, final FirebaseUser currentUser) {
        StorageReference refStorage = FirebaseStorage.getInstance().getReference().child("userPhoto");
        final StorageReference imgFilePath = refStorage.child(pickedImgUri.getLastPathSegment());
        imgFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(uname)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            showMessage("Registration Completed");
                                            updateUI();
                                        }
                                    }
                                });
                    }
                });
            }
        });

    }

    private void updateUI() {
        Intent loginActivity = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(loginActivity);
        finish();

    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    private void checkAndRequestForPermission() {
        if(ContextCompat.checkSelfPermission(RegisterActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(RegisterActivity.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(RegisterActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},preqCode);
            }
        }
        else {
            openGallery();
        }

    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,reqCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == reqCode && data != null){
            pickedImgUri = data.getData();
            userPhoto.setImageURI(pickedImgUri);
        }
    }
}
