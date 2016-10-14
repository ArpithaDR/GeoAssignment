package com.example.appy.locationidentifier;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostMyAdForm extends AppCompatActivity {

    public static final String TAG = PostMyAdForm
            .class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_my_ad_form);
    }

    public void submitAddress(View arg0) {
        EditText subject_et = (EditText) findViewById(R.id.post_ad_subject);
        String subject = subject_et.getText().toString();

        EditText address_et = (EditText) findViewById(R.id.post_ad_address);
        String address = address_et.getText().toString();

        EditText price_et = (EditText) findViewById(R.id.post_ad_price);
        String price = price_et.getText().toString();

        EditText vacancy_et = (EditText) findViewById(R.id.post_ad_spots);
        String vacancy = vacancy_et.getText().toString();

        EditText startdate_et = (EditText) findViewById(R.id.post_ad_start_date);
        EditText enddate_et = (EditText) findViewById(R.id.post_ad_end_date);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startdate, enddate;
        try {
            startdate = df.parse(startdate_et.getText().toString());
            enddate = df.parse(enddate_et.getText().toString());
            String myText = startdate.getDate() + "-" + (startdate.getMonth() + 1) + "-" + (1900 + startdate.getYear());
            Log.i(TAG, "Start date: " + startdate + ", end date: " + enddate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        EditText desc_et = (EditText) findViewById(R.id.post_ad_desc);
        String description = desc_et.getText().toString();

        EditText phone_et = (EditText) findViewById(R.id.post_ad_phone);
        String phone = phone_et.getText().toString();



        Log.i(TAG,"Subject: " + subject +
                ", Address: " + address +
                ", Price: " + price +
                ", vacancy: " + vacancy +
                ", desc: " + description +
                ", Phone: " + phone
                );

        Toast.makeText(getBaseContext(), "Submitted!" , Toast.LENGTH_SHORT ).show();

    }
}
