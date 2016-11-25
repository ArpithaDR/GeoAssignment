package com.example.appy.locationidentifier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.appy.utility.AsyncResponse;
import com.example.appy.utility.HttpConnection;
import com.example.appy.utility.SessionManagement;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewMyAds extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdsListAdapter adapter;
    private List<House> myAdsList;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_ads);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        myAdsList = new ArrayList<>();
        adapter = new MyAdsListAdapter(this, myAdsList);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(adapter);

        prepareListOfHouses();
        System.out.println("List items: " + myAdsList);
    }

    //This is static and this information must be fetched from database later
    //Also this list should be generated based on user logged in and get only ads posted by user
    private void prepareListOfHouses() {
        SessionManagement session = new SessionManagement(getApplicationContext());
        String id = session.getLoggedInUserId();
        String userId = "1349781218406667";
        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/fetchuserhouses?userId=" +
                userId;
        HttpConnection httpConnection = new HttpConnection(this, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String result = (String) output;
                JSONObject houses = null;
                try {
                    houses = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                extractHousesFromResult(houses);
            }
        });
        httpConnection.execute(s);
        adapter.notifyDataSetChanged();
    }

    void extractHousesFromResult(JSONObject obj) {
        ArrayList<MarkerOptions> results = new ArrayList<>();
        // Read json object
        /* Example Object:
              "AptNo": 5,
              "Available": true,
              "City": "Los Angeles",
              "Description": "Nearest to Campus",
              "EndDate": "Thu, 01 Jun 2017 00:00:00 GMT",
              "PhoneNumber": "4086007283  ",
              "Price": 2700.0,
              "Spots": 2,
              "StartDate": "Sat, 01 Apr 2017 00:00:00 GMT",
              "State": "California",
              "StreetAddress": "2827 Orchard Ave                                                                                    ",
              "Title": "This is Sindhus house",
              "Zip": 90007,
              "id": 3,
              "user_id": 1349781218406667
         */
        try {
            JSONArray houseArray = (JSONArray) obj.get("houseList");
            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
            SimpleDateFormat sdf=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
            for(int i=0; i< houseArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) houseArray.get(i);
                String desc = (String) jsonObject.get("Description");
                Double price = (Double) jsonObject.get("Price");
                int houseId = (int) jsonObject.get("id");
                String subject = (String) jsonObject.get("Title");
                String endDate = (String) jsonObject.get("EndDate");
                String startDate = (String) jsonObject.get("StartDate");
                try {
                    Date dateEnd = sdf.parse(endDate);
                    endDate = formatter.format(dateEnd);
                    Date dateStart = sdf.parse(startDate);
                    startDate = formatter.format(dateStart);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String address = (String) jsonObject.get("StreetAddress");
                String phone = (String) jsonObject.get("PhoneNumber");
                int spots = (int) jsonObject.get("Spots");
                String email = "test@123";
                House house = new House(desc, subject, email,address, startDate, endDate, phone, spots, price, houseId, R.drawable.images1);
                myAdsList.add(house);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
        //return results;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}