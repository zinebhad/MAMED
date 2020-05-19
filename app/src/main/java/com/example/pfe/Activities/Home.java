package com.example.pfe.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pfe.Fragments.HomeFragment;
import com.example.pfe.Fragments.ProfileFragment;
import com.example.pfe.Fragments.SettingsFragment;
import com.example.pfe.Models.Book;

import com.example.pfe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int PReqCode = 2 ;
    private static final int REQUESCODE = 2 ;
    FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Dialog popAddBook;
    ImageView popupUserImage,popupBookImage,popupAddBtn;
    TextView popupTitle,popupDescription,Sp,Th,Author;
    ProgressBar popupClickProgress;
    private Uri pickedImgUri = null;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        // ini

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Rating");
        if(currentUser!=null){
        if (currentUser.getUid().equals("91l2XpXX3bS8FDMDe29J4pIHgYM2")) {
            fab.setVisibility(0);
            // ini popup
            iniPopup();
            setupPopupImageClick();



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddBook.show();
            }
        });}}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

if (currentUser!=null) {
    updateNavHeader();

}
        // set the home fragment as the default one

        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();



    }

    private void setupPopupImageClick() {


        popupBookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                checkAndRequestForPermission();


            }
        });



    }


    private void checkAndRequestForPermission() {


        if (ContextCompat.checkSelfPermission(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(Home.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();

            }

            else
            {
                ActivityCompat.requestPermissions(Home.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }
        else
            // everything goes well : we have permission to access user gallery
            openGallery();

    }





    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }



    // when user picked an image ...
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            popupBookImage.setImageURI(pickedImgUri);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser()==null){
            Intent homeactivity = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(homeactivity);
            finish();
        }
    }

    private void iniPopup() {
        popAddBook = new Dialog(this);
        popAddBook.setContentView(R.layout.popup_add_book);
        popAddBook.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popAddBook.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddBook.getWindow().getAttributes().gravity = Gravity.CENTER;
        // ini popup widgets
        popupUserImage = popAddBook.findViewById(R.id.popup_user_image);
        popupBookImage = popAddBook.findViewById(R.id.popup_img);
        popupTitle = popAddBook.findViewById(R.id.popup_title);
        popupDescription = popAddBook.findViewById(R.id.popup_description);
        popupAddBtn = popAddBook.findViewById(R.id.popup_add);
        Author=popAddBook.findViewById(R.id.author);
        Sp=popAddBook.findViewById(R.id.Sp);
        Th=popAddBook.findViewById(R.id.Th);
        popupClickProgress = popAddBook.findViewById(R.id.popup_progressBar);

        // load Current user profile photo

        Glide.with(Home.this).load(currentUser.getPhotoUrl()).into(popupUserImage);
        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);
                if (!popupTitle.getText().toString().isEmpty()
                    && !popupDescription.getText().toString().isEmpty()
                        && !Sp.getText().toString().isEmpty()
                        && !Th.getText().toString().isEmpty()
                    && pickedImgUri != null
                    ) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                    final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownlaodLink = uri.toString();
                                    Book book = new Book(popupTitle.getText().toString(),
                                            Author.getText().toString(),
                                            popupDescription.getText().toString(),
                                            imageDownlaodLink,
                                            currentUser.getUid(),
                                            Sp.getText().toString(),
                                            Th.getText().toString()
                                          );
                                    addBook(book);



                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // something goes wrong uploading picture
                                    showMessage(e.getMessage());
                                    popupClickProgress.setVisibility(View.INVISIBLE);
                                    popupAddBtn.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                }
                else {
                    showMessage("Please verify all input fields and choose Book Image") ;
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popupClickProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    private void addBook(Book book) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Books").push();
        String key = myRef.getKey();
        book.setBookKey(key);
        myRef.setValue(book).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Book Added successfully");
                popupClickProgress.setVisibility(View.INVISIBLE);
                popupAddBtn.setVisibility(View.VISIBLE);
                popAddBook.dismiss();
            }
        });





    }
    private void showMessage(String message) {
        Toast.makeText(Home.this,message,Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            getSupportActionBar().setTitle("Home");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();

        } else if (id == R.id.nav_profile) {

            getSupportActionBar().setTitle("Profile");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new ProfileFragment()).commit();

        } else if (id == R.id.nav_settings) {

            getSupportActionBar().setTitle("Settings");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new SettingsFragment()).commit();


        }
        else if (id == R.id.nav_signout) {
            FirebaseAuth.getInstance().signOut();
            Intent homeactivity = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(homeactivity);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void updateNavHeader() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navUserMail = headerView.findViewById(R.id.nav_user_mail);
        ImageView navUserPhot = headerView.findViewById(R.id.nav_user_photo);
        navUserMail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());
        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhot);
    }
}
