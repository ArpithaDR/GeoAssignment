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
        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/addAddress?subject=" + subject +
                "&address=" + address + "&price=" + price +
                "&vacancies=" + vacancy + "&start_date="+ startdate + "&end_date=" + enddate +
                "&description=" + description + " &phone_number=" + phone;
        new GetRequestDemo(this).execute(s);
    }

    private class GetRequestDemo extends AsyncTask<String, Void, Void> {
        private final Context context;

        public GetRequestDemo(Context c) {
            this.context = c;
        }

        protected void onPreExecute() {
            progress = new ProgressDialog(this.context);
            progress.setMessage("Loading");
            progress.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                // doesn't accept # in url-params
                System.out.println("url:" + params[0]);
//                String encodedUrl = URLEncoder.encode(params[0], "UTF-8");
                URL url = new URL(params[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'GET' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Response: ");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                System.out.println(responseOutput.toString());
                output.append("Response msg: " + responseOutput.toString());

                PostMyAdForm.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), output , Toast.LENGTH_SHORT ).show();
                        progress.dismiss();
                    }
                });

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

    }
}
