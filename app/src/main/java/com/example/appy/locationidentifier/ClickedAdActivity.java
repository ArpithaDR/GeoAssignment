package com.example.appy.locationidentifier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ClickedAdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_ad);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
