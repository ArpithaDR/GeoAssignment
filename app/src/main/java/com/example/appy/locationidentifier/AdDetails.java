package com.example.appy.locationidentifier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AdDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView price = (TextView) findViewById(R.id.clicked_ad_price);
        TextView address = (TextView) findViewById(R.id.clicked_ad_address);
        TextView startDate = (TextView) findViewById(R.id.clicked_ad_start_date);
        TextView endDate = (TextView) findViewById(R.id.clicked_ad_end_date);
        TextView desc = (TextView) findViewById(R.id.clicked_ad_desc);
        TextView phone = (TextView) findViewById(R.id.clicked_ad_phone);
        TextView spots = (TextView) findViewById(R.id.clicked_ad_spots);
        TextView subject = (TextView) findViewById(R.id.clicked_ad_subject);
        TextView email = (TextView) findViewById(R.id.clicked_ad_user_email);
        Bundle bundle = getIntent().getExtras();

        price.setText("Price: " + bundle.getString("Price"));
        address.setText("Address: " + bundle.getString("Address"));
        startDate.setText("Available From: " + bundle.getString("StartDate"));
        endDate.setText("Available To: " + bundle.getString("EndDate"));
        desc.setText("Description: " + bundle.getString("Desc"));
        phone.setText("Phone Number: " + bundle.getString("Phone"));
        spots.setText("Spots Available: " + bundle.getString("Spots"));
        subject.setText("Title: " + bundle.getString("Subject"));
        email.setText("Email: " + bundle.getString("Email"));
    }
}
