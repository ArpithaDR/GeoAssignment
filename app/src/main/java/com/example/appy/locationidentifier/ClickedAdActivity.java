package com.example.appy.locationidentifier;

import android.content.Intent;
import android.net.Uri;
import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
        TextView distance = (TextView) findViewById(R.id.clicked_ad_distance);
        ImageButton directionsBtn = (ImageButton) findViewById(R.id.google_maps);
        directionsBtn.setOnClickListener(onClickListener());
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
        distance.setText("Distance: " + bundle.getString("Distance"));
    }

    public void listViewOfPublicPlaces(View view) {
        Bundle bundle = getIntent().getExtras();
        Double latitude = Double.parseDouble(bundle.getString("Latitude"));
        Double longitude = Double.parseDouble(bundle.getString("Longitude"));
        String s = "http://maps.google.com/maps?saddr="+latitude+","+longitude+"&daddr="+latitude+","+longitude;
        Intent intent = new Intent(this, ListOfPublicPlaces.class);
        intent.putExtra("Latitude", Double.toString(latitude));
        intent.putExtra("Longitude", Double.toString(longitude));
        startActivity(intent);
    }

    private View.OnClickListener onClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                Double sLat = Double.parseDouble(bundle.getString("sLat"));
                Double sLong = Double.parseDouble(bundle.getString("sLong"));
                Double dLat = Double.parseDouble(bundle.getString("Latitude"));
                Double dLong = Double.parseDouble(bundle.getString("Longitude"));
                Intent intent1 = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr="+sLat+","+sLong+"&daddr="+dLat+","+dLong));
                startActivity(intent1);
            }
        };
    }
}
