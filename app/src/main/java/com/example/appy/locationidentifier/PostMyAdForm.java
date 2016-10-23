package com.example.appy.locationidentifier;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appy.utility.AsyncResponse;
import com.example.appy.utility.HttpConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostMyAdForm extends AppCompatActivity {

    public static final String TAG = PostMyAdForm
            .class.getSimpleName();
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_my_ad_form);
    }

    public void submitAddress(View arg0) {
        EditText subject_et = (EditText) findViewById(R.id.post_ad_subject);
        subject_et.setText("my Subject");
        String subject = subject_et.getText().toString();

        EditText address_et = (EditText) findViewById(R.id.post_ad_address);
        address_et.setText("my address");
        String address = address_et.getText().toString();

        EditText price_et = (EditText) findViewById(R.id.post_ad_price);
        price_et.setText("450");
        String price = price_et.getText().toString();

        EditText vacancy_et = (EditText) findViewById(R.id.post_ad_spots);
        vacancy_et.setText("2");
        String vacancy = vacancy_et.getText().toString();

        EditText startdate_et = (EditText) findViewById(R.id.post_ad_start_date);
        startdate_et.setText("2017-1-3");
        EditText enddate_et = (EditText) findViewById(R.id.post_ad_end_date);
        enddate_et.setText("2017-5-3");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startdate = new Date(), enddate = new Date();
        try {
            startdate = df.parse(startdate_et.getText().toString());
            enddate = df.parse(enddate_et.getText().toString());
            String myText = startdate.getDate() + "-" + (startdate.getMonth() + 1) + "-" + (1900 + startdate.getYear());
            Log.i(TAG, "Start date: " + startdate + ", end date: " + enddate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        EditText desc_et = (EditText) findViewById(R.id.post_ad_desc);
        desc_et.setText("my desc");
        String description = desc_et.getText().toString();

        EditText phone_et = (EditText) findViewById(R.id.post_ad_phone);
        phone_et.setText("87681");
        String phone = phone_et.getText().toString();

        Log.i(TAG,"Subject: " + subject +
                ", Address: " + address +
                ", Price: " + price +
                ", vacancy: " + vacancy +
                ", desc: " + description +
                ", Phone: " + phone
                );

        Toast.makeText(getBaseContext(), "Submitted!" , Toast.LENGTH_SHORT ).show();
        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/addAddress?subject=" + subject +
                "&address=" + address + "&price=" + price +
                "&vacancies=" + vacancy + "&start_date="+ startdate + "&end_date=" + enddate +
                "&description=" + description + " &phone_number=" + phone;

        HttpConnection httpConnection = new HttpConnection(this, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String s = (String) output;
                System.out.println("In PostMyAdForm finish process with result:" + s);
            }
        });
        httpConnection.execute(s);
    }

    public void test(String s) {
        Log.i(TAG, s);
    }
}
