package com.example.appy.locationidentifier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.appy.utility.SessionManagement;

public class ClickedAdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_ad);

        /*SessionManagement session = new SessionManagement(getApplicationContext());
        String id = session.getLoggedInUserId();
        Log.v("Shivalik test - " , id);
        */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
