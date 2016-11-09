package com.example.appy.locationidentifier;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appy.utility.AsyncResponse;
import com.example.appy.utility.HttpConnection;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostMyAdForm extends AppCompatActivity {

    public static final String TAG = PostMyAdForm
            .class.getSimpleName();
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_my_ad_form);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void submitAddress(View arg0) {
        EditText title_et = (EditText) findViewById(R.id.post_ad_title);
        title_et.setText("my Title");
        String title = title_et.getText().toString();

        EditText address_et = (EditText) findViewById(R.id.post_ad_address);
        address_et.setText("2140 Oak Street");
        String address = address_et.getText().toString();

        EditText aptNum_et = (EditText) findViewById(R.id.post_ad_aptNumber);
//        aptNum_et.setText("2");
        String aptNum = aptNum_et.getText().toString(); // integer check, null check handled in python

        EditText city_et = (EditText) findViewById(R.id.post_ad_city);
        city_et.setText("Los Angeles");
        String city = city_et.getText().toString();

        EditText state_et = (EditText) findViewById(R.id.post_ad_state);
        state_et.setText("California");
        String state = state_et.getText().toString();

        EditText zipCd_et = (EditText) findViewById(R.id.post_ad_zip);
        zipCd_et.setText("90007");
        String zipcd = zipCd_et.getText().toString(); // int check

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

        Address geoCodedAddress;
        double lat = 0.0, lng = 0.0;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List addressList = null;
        try {
            addressList = geocoder.getFromLocationName(address+city+state, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addressList != null && addressList.size() > 0) {
            geoCodedAddress = (Address) addressList.get(0);
            lat = geoCodedAddress.getLatitude();
            lng = geoCodedAddress.getLongitude();

            Log.i(TAG,"Title: " + title +
                    ", Address: " + address +
                    ", Apt Num: " + aptNum +
                    ", City: " + city +
                    ", State: " + state +
                    ", Price: " + price +
                    ", vacancy: " + vacancy +
                    ", desc: " + description +
                    ", Phone: " + phone
            );

            Toast.makeText(getBaseContext(), "Submitted!" , Toast.LENGTH_SHORT ).show();
            String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/addAddress?title=" + title +
                    "&address=" + address + "&aptNum=" + aptNum + "&city=" + city + "&state=" + state + "&zipcd=" + zipcd + "&price=" + price +
                    "&vacancies=" + vacancy + "&start_date="+ startdate + "&end_date=" + enddate +
                    "&description=" + description + "&phone_number=" + phone + "&lat=" + lat + "&lng=" + lng;

            HttpConnection httpConnection = new HttpConnection(this, new AsyncResponse() {
                @Override
                public void processFinish(Object output) {
                    String s = (String) output;
                    System.out.println("In PostMyAdForm finish process with result:" + s);
                }
            });
            httpConnection.execute(s);
        } else {
            Toast.makeText(getBaseContext(), "Incorrect address, please correct and hit Submit again." , Toast.LENGTH_SHORT ).show();
        }

    }

    public void test(String s) {
        Log.i(TAG, s);
    }
}
