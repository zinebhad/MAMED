package com.example.pfe.Activities;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.EventLogTags;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pfe.Adapters.CommentAdapter;
import com.example.pfe.Models.Comment;
import com.example.pfe.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
public class BookDetailActivity extends AppCompatActivity {
    ImageView imgBook,imgUserBook,imgCurrentUser;
    TextView txtBookDesc, txtBookDateName, txtBookTitle, txtBookAuthor, txtBookDescription, txtBookSp, txtBookTh;    EditText editTextComment;
    Button btnAddComment;
    String BookKey;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView RvComment;
    CommentAdapter commentAdapter;
    List<Comment> listComment;
    TextView textView;
    static String COMMENT_KEY = "Comment" ;
    static String Rate_KEY="Rating";
    Dialog rate ,login;
    static String r;
    public int n=0;
    RatingBar ratingBar;
    Button submitRateButton, later,Login,register,deleteBook;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        // let's set the statue bar to transparent
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().setTitle("BookDetails");
        ConstraintLayout constb=findViewById(R.id.constBooks);
        // ini Views
        RvComment = findViewById(R.id.rv_comment);
        imgBook = findViewById(R.id.book_detail_img);
        imgUserBook = findViewById(R.id.book_detail_user_img);
        imgCurrentUser = findViewById(R.id.book_detail_currentuser_img);
        txtBookTitle = findViewById(R.id.book_detail_title);
        txtBookDateName = findViewById(R.id.book_detail_date_name);
        txtBookAuthor = findViewById(R.id.auteur);
        txtBookDescription = findViewById(R.id.Desc);
        txtBookSp = findViewById(R.id.Spe);
        txtBookTh = findViewById(R.id.The);
        editTextComment = findViewById(R.id.book_detail_comment);
        btnAddComment = findViewById(R.id.book_detail_add_comment_btn);
        deleteBook = findViewById(R.id.deletebook);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //Rating
        BookKey = getIntent().getExtras().getString("bookKey");
        textView=findViewById(R.id.textView11);
        Login = (Button)findViewById(R.id.log);
        register = findViewById(R.id.register);
        databaseReference = firebaseDatabase.getReference(Rate_KEY);
        //Rating
        if (firebaseUser != null) {
            if (firebaseUser.getUid().equals("91l2XpXX3bS8FDMDe29J4pIHgYM2")) {
                editTextComment.setVisibility(4);
                btnAddComment.setVisibility(4);
                deleteBook.setVisibility(0);
                deleteBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        firebaseDatabase.getReference("Books").child(BookKey).removeValue();
                        startActivity(new Intent(BookDetailActivity.this, Home.class));
                    }
                });
            } else {
                databaseReference.child(BookKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            //Toast.makeText(getApplicationContext(),snap.getKey().toString(), Toast.LENGTH_LONG).show();
                            if (snap.getKey().toString().equals(firebaseUser.getUid())) {
                                n = 1;
                            }
                        }
                        if (n == 1) {
                            r = dataSnapshot.child(firebaseUser.getUid()).getValue(String.class);
                            if (r.equals(null)) {
                                //Toast.makeText(getApplicationContext(), r, Toast.LENGTH_LONG).show();
                                Rate();
                                rate.show();
                            }
                        } else if (n == 0) {
                            Rate();
                            rate.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        }
        // add Comment button click listner
        // add Comment button click listner
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseUser != null) {
                    btnAddComment.setVisibility(View.INVISIBLE);
                    DatabaseReference commentReference = firebaseDatabase.getReference(COMMENT_KEY).child(BookKey).push();
                    String comment_content = editTextComment.getText().toString();
                    String uid = firebaseUser.getUid();
                    String uname = firebaseUser.getDisplayName();
                    String uimg = firebaseUser.getPhotoUrl().toString();
                    Comment comment = new Comment(comment_content, uid, uimg, uname);
                    commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showMessage("comment added");
                            editTextComment.setText("");
                            btnAddComment.setVisibility(View.VISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showMessage("fail to add comment : " + e.getMessage());
                        }
                    });
                } else {

                    Login.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            login.dismiss();
                            //starting login activity
                            startActivity(new Intent(BookDetailActivity.this, LoginActivity.class));
                        }

                    });
                    register.setOnClickListener(new View.OnClickListener() {


                        @Override
                        public void onClick(View view) {
                            login.dismiss();
                            //starting login activity
                            startActivity(new Intent(BookDetailActivity.this, RegisterActivity.class));
                        }
                    });
                }
            }
        });
        if( firebaseAuth.getCurrentUser()!=null) {
            String BookImage = getIntent().getExtras().getString("bookImage");
            Glide.with(this).load(BookImage).into(imgBook);
            String BookTitle = getIntent().getExtras().getString("title");
            txtBookTitle.setText(BookTitle);
            firebaseDatabase.getReference().child("Books").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String author = dataSnapshot.child(BookKey).child("author").getValue(String.class);
                    String Description = dataSnapshot.child(BookKey).child("description").getValue(String.class);
                    String Spetialite = dataSnapshot.child(BookKey).child("spetialite").getValue(String.class);
                    String Theme = dataSnapshot.child(BookKey).child("theme").getValue(String.class);
                    txtBookAuthor.setText(author);
                    txtBookDescription.setText(Description);
                    txtBookSp.setText(Spetialite);
                    txtBookTh.setText(Theme);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            String userBookImage = getIntent().getExtras().getString("userPhoto");
            Glide.with(this).load(userBookImage).into(imgUserBook);
            // setcomment user image
            if (firebaseUser != null) {
                Glide.with(this).load(firebaseUser.getPhotoUrl()).into(imgCurrentUser);
            }
            String date = timestampToString(getIntent().getExtras().getLong("bookDate"));
            txtBookDateName.setText(date);
            // ini Recyclerview Comment
            iniRvComment();


        }
        else {
            constb.setVisibility(4);
            textView.setVisibility(0);
            register.setVisibility(0);
            Login.setVisibility(0);
            Login.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //starting login activity
                    startActivity(new Intent(BookDetailActivity.this, LoginActivity.class));
                }

            });
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //starting login activity
                    startActivity(new Intent(BookDetailActivity.this, RegisterActivity.class));
                }
            });
        }
    }
    private void Rate(){
        rate= new Dialog(this);
        rate.setContentView(R.layout.rate_us);
        rate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        rate.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        rate.getWindow().getAttributes().gravity = Gravity.CENTER;
        ratingBar = (RatingBar) rate.findViewById(R.id.rating_bar_feedback);
        submitRateButton= (Button) rate.findViewById(R.id.submit);
        later=rate.findViewById(R.id.later);
        submitRateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String ratingValue = String.valueOf(ratingBar.getRating());
                DatabaseReference RateReference = firebaseDatabase.getReference().child(Rate_KEY).child(BookKey);
                String uid = firebaseUser.getUid();
                RateReference.child(firebaseUser.getUid()).setValue(ratingValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("RAte successfully");
                        rate.dismiss();
                    }
                });
                Toast.makeText(getApplicationContext(), "Rate: " + ratingValue, Toast.LENGTH_LONG).show();
            }

        });
        later.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {
            rate.dismiss();
            }
        });
    }
    private void iniRvComment() {
        RvComment.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRef = firebaseDatabase.getReference(COMMENT_KEY).child(BookKey);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap:dataSnapshot.getChildren()) {

                    Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment) ;
                }
                commentAdapter = new CommentAdapter(getApplicationContext(),listComment);
                RvComment.setAdapter(commentAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
    private String timestampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy",calendar).toString();
        return date;
    }
}
