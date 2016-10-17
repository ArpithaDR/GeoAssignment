package com.example.appy.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.appy.locationidentifier.PostMyAdForm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by arorai on 10/16/16.
 */

public class HttpConnection extends AsyncTask<String, Void, Void> {


    private final Context context;
    private ProgressDialog progress;

    public HttpConnection(Context c) {
        this.context = c;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(this.context);
        progress.setMessage("Loading.....!");
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
            progress.dismiss();

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