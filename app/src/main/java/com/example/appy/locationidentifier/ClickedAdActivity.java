package com.example.appy.locationidentifier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

        price.setText(bundle.getString("Price"));
        address.setText(bundle.getString("Address"));
        startDate.setText(bundle.getString("StartDate"));
        endDate.setText(bundle.getString("EndDate"));
        desc.setText(bundle.getString("Desc"));
        phone.setText(bundle.getString("Phone"));
        spots.setText(bundle.getString("Spots"));
        subject.setText(bundle.getString("Subject"));
        email.setText(bundle.getString("Email"));
    }
}
