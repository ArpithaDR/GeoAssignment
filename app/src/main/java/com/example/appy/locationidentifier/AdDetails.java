package com.example.appy.locationidentifier;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appy.utility.AsyncResponse;
import com.example.appy.utility.HttpConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class AdDetails extends AppCompatActivity {

    public String strBase64 = "";
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

        System.out.println("HouseId - " + bundle.getInt("HouseId"));

        String house_id = String.valueOf(bundle.getInt("HouseId"));

        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/getImage?house_id=" + house_id;

        HttpConnection httpConnection = new HttpConnection(AdDetails.this, new AsyncResponse() {
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

    }
}
