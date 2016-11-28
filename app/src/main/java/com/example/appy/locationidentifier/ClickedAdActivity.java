package com.example.appy.locationidentifier;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.renderscript.Double2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import android.widget.ImageView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.TextView;

import com.example.appy.utility.AsyncResponse;
import com.example.appy.utility.HttpConnection;
import com.example.appy.utility.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

public class ClickedAdActivity extends AppCompatActivity {

    public String strBase64 = "";
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
        Button directionsBtn = (Button) findViewById(R.id.google_maps);
        directionsBtn.setOnClickListener(onClickListener());
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

        System.out.println("HouseId - " + bundle.getInt("HouseId"));

        String house_id = String.valueOf(bundle.getInt("HouseId"));

        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/getImage?house_id=" + house_id;

        HttpConnection httpConnection = new HttpConnection(ClickedAdActivity.this, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String result = (String) output;

                JSONObject encodedImage = null;
                try {
                    encodedImage = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.v("image: ", encodedImage.toString());
                try {
                    encodedImage = encodedImage.getJSONObject("image");
                    strBase64 = encodedImage.getString("image_data").replace("\n","");
                    System.out.println(strBase64);

                    byte[] decodedString = Base64.decode(strBase64, 0);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ImageView testEncoded = (ImageView) findViewById(R.id.imageView2);
                    Bitmap scaled = Bitmap.createScaledBitmap(decodedByte, 720, 720, true);
                    testEncoded.setImageBitmap(scaled);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        httpConnection.execute(s);

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
