package com.example.pfe.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pfe.Fragments.ProfileFragment;
import com.example.pfe.Models.Adherent;
import com.example.pfe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateProfile extends AppCompatActivity {
    ImageView ImgUserPhoto;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;
    private EditText userEmail, userName, cin;
    private ProgressBar loadingProgress;
    private Button regBtn;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Adherent ObjectAdherent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        userEmail = findViewById(R.id.regMail);
        userName = findViewById(R.id.regName);
        cin = findViewById(R.id.cin);
        loadingProgress = findViewById(R.id.regProgressBar);
        regBtn = findViewById(R.id.updating);
        loadingProgress.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Adherents");
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                regBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                final String email = userEmail.getText().toString();
                final String name = userName.getText().toString();

                if (name.isEmpty()) {
                    // something goes wrong : all fields must be filled
                    // we need to display an error message
                    showMessage("Please Verify all fields");
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                } else {
                    // everything is ok and all fields are filled now we can start creating user account
                    // CreateUserAccount method will try to create the user if the email is valid
                    updateUserInfo(name, pickedImgUri, mAuth.getCurrentUser());
                }
            }
        });

        ImgUserPhoto = findViewById(R.id.regUserPhoto);

        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 22) {

                    checkAndRequestForPermission();


                } else {
                    openGallery();
                }

            }
        });

    }


    // update user photo and name
    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {

        // first we need to upload user photo to firebase storage and get url

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // image uploaded succesfully
                // now we can get our image url

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        // uri contain user image url


                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profleUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            // user info updated successfully
                                            showMessage("profile changed");
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
        finish();
    }

    // simple method to show toast message
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    private void checkAndRequestForPermission() {


        if (ContextCompat.checkSelfPermission(UpdateProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(UpdateProfile.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(UpdateProfile.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        } else
            openGallery();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData();
            ImgUserPhoto.setImageURI(pickedImgUri);
        }
    }


}






























































































































































































