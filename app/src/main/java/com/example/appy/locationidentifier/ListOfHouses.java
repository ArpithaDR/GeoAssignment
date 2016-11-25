package com.example.appy.locationidentifier;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.appy.utility.AsyncResponse;
import com.example.appy.utility.HttpConnection;
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

public class ListOfHouses extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HouseListAdapter adapter;
    private List<House> houseList;
    private List<House> favList;
    RecyclerView.LayoutManager mLayoutManager;
    private LatLng latLng;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_houses);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        houseList = new ArrayList<>();
        favList = new ArrayList<>();
        adapter = new HouseListAdapter(this, houseList);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(adapter);
        bundle = getIntent().getExtras();
        Double latValue = Double.parseDouble(bundle.getString("Latitude"));
        Double longValue = Double.parseDouble(bundle.getString("Longitude"));
        prepareListOfHouses(latValue, longValue);
    }

    //This is static and this information must be fetched from database later
    private void prepareListOfHouses(Double latVal, Double longVal) {

        latLng = new LatLng(latVal, longVal);
        fetchHousesFromDB(latLng);
    }

    private void fetchHousesFromDB(LatLng latLng) {

        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/searchHouseAvailable?latitude=" +
                latLng.latitude + "&longitude=" + latLng.longitude +"&radius=1.0";
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
    }



    void extractHousesFromResult(JSONObject obj) {
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
                houseList.add(house);
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
