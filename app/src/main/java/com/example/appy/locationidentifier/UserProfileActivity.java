package com.example.appy.locationidentifier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        TextView fname = (TextView) findViewById(R.id.first_name);
        TextView lname = (TextView) findViewById(R.id.last_name);
        Bundle bundle = getIntent().getExtras();
        fname.setText(bundle.getString("fname"));
        lname.setText(bundle.getString("lname"));
//        fname.setText("hello");
//        lname.setText("yoyo");
    }
}
