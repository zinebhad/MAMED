package com.example.pfe.Activities;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pfe.Fragments.HomeFragment;

import com.example.pfe.R;


public class HomeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("Mamed");

        getSupportFragmentManager().beginTransaction().replace(R.id.container1,new HomeFragment()).commit();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.Sign_in) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));

            return true;
        }
        if (id == R.id.Sign_up) {
            startActivity(new Intent(HomeActivity.this, RegisterActivity.class));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}