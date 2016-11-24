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

import java.util.ArrayList;
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

        try {
            JSONArray houseArray = (JSONArray) obj.get("houseList");

            for(int i=0; i< houseArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) houseArray.get(i);
                String desc = (String) jsonObject.get("Description");
                Double price = (Double) jsonObject.get("Price");
                int houseId = (int) jsonObject.get("id");
                House house = new House(desc, price, R.drawable.images1,houseId);
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