package com.example.appy.locationidentifier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.appy.utility.AsyncResponse;
import com.example.appy.utility.HttpConnection;
import com.example.appy.utility.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavListOfHouses extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FavListAdapter adapter;
    public List<House> favList;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_list_of_houses);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //houseList = new ArrayList<>();
        favList = new ArrayList<>();
        adapter = new FavListAdapter(this, favList);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        prepareListOfHouses();
        if(favList.isEmpty()) {
            Toast.makeText(this, "No favourites found", Toast.LENGTH_SHORT);
        }
    }

    //This is static and this information must be fetched from database later
    //Also this list should be generated based on user logged in and get his fav
    private void prepareListOfHouses() {
        /* SessionManagement session = new SessionManagement(getApplicationContext());
            String id = session.getLoggedInUserId();
        */

        String userId = "10208655238312268";
        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/fetchFavorite?"
                + "userId=" + userId;
        HttpConnection httpConnection = new HttpConnection(this, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String result = (String) output;
                JSONObject favHouses = null;
                try {
                    favHouses = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                fetchMinimalDetails(favHouses);
            }
        });
        httpConnection.execute(s);
    }

    public void fetchMinimalDetails(JSONObject obj) {
        try {
            JSONArray houseArray = (JSONArray) obj.get("favList");

            for (int i = 0; i < houseArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) houseArray.get(i);
                int houseId = (int) jsonObject.get("houseId");
                fetchHouseDetails(houseId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void fetchHouseDetails(int houseId) {
        String id = Integer.toString(houseId);
        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/fetchHouse?"
                + "houseId=" + id;
        HttpConnection httpConnection = new HttpConnection(this, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String result = (String) output;
                JSONObject favHouseDetails = null;
                try {
                    favHouseDetails = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                extractHouseDetails(favHouseDetails);
            }
        });
        httpConnection.execute(s);
    }

    public void extractHouseDetails(JSONObject obj) {
            try {
                JSONArray houseArray = (JSONArray) obj.get("favHouseList");

                for (int i = 0; i < houseArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) houseArray.get(i);
                    String desc = (String) jsonObject.get("Description");
                    Double price = (Double) jsonObject.get("Price");
                    int houseId = (int) jsonObject.get("houseId");
                    House house = new House(desc, price, R.drawable.images1,houseId);
                    favList.add(house);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
