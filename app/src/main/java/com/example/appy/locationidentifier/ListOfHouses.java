package com.example.appy.locationidentifier;

import android.support.v4.app.NavUtils;
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
import java.util.Set;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ListOfHouses extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HouseListAdapter adapter;
    private List<House> houseList;
    private List<House> favList = new ArrayList<House>();
    private ArrayList<Integer> favHouseId = new ArrayList<Integer>();
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
        if (bundle != null) {
            Double latValue = Double.parseDouble(bundle.getString("Latitude"));
            Double longValue = Double.parseDouble(bundle.getString("Longitude"));
            int radius  = Integer.parseInt(bundle.getString("Radius"));
            fetchFavourites(latValue, longValue, radius);
        }
    }


    private void fetchFavourites(final Double latValue, final Double longValue, final int radius) {
        SessionManagement session = new SessionManagement(getApplicationContext());
        String userId = session.getLoggedInUserId();
        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/fetchFavorite?"
                + "userId=" + userId;
        HttpConnection httpConnection = new HttpConnection(this, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String result = (String) output;
                JSONObject favHouses = null;
                try {
                    favHouses = new JSONObject(result);
                    JSONArray houseArray = (JSONArray) favHouses.get("favList");

                    for (int i = 0; i < houseArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) houseArray.get(i);
                        int houseId = (int) jsonObject.get("houseId");
                        favHouseId.add(houseId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //fetchMinimalDetails(favHouses);
                prepareListOfHouses(latValue, longValue, radius);
            }
        });
        httpConnection.execute(s);
    }


    //This is static and this information must be fetched from database later
    private void prepareListOfHouses(Double latVal, Double longVal, int radius) {
        latLng = new LatLng(latVal, longVal);
        fetchHousesFromDB(latLng, radius);
        //fetchFavourites();
    }

    private void fetchHousesFromDB(final LatLng latLng, final int radius) {

        boolean gympref = bundle.getBoolean("gympref");
        boolean hospitalpref = bundle.getBoolean("hospitalpref");
        boolean schoolpref = bundle.getBoolean("schoolpref");
        boolean grocerypref = bundle.getBoolean("grocerypref");
        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/houseSearchWithPref?latitude=" +
                latLng.latitude + "&longitude=" + latLng.longitude +"&radius="+ radius + "&gym=" + gympref +
                "&hospital=" + hospitalpref + "&school=" + schoolpref + "&grocery=" + grocerypref;
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
                //fetchFavourites(houses);
                System.out.println("Fav ID's: " + favHouseId);
                extractHousesFromResult(houses,latLng);
            }
        });
        httpConnection.execute(s);
    }



    void extractHousesFromResult(JSONObject obj, LatLng latLng) {
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
                Double latitude = (Double) jsonObject.get("Latitude");
                Double longitude = (Double) jsonObject.get("Longitude");
                Double distance = (Double) jsonObject.get("Distance");
                Double sLatitude = latLng.latitude;
                Double sLongitude = latLng.longitude;
                boolean isFav;
                if (((favHouseId !=null && favHouseId.contains(houseId))))
                    isFav = true;
                else
                    isFav = false;
                System.out.println("Printing results: " + houseId + " is Fav: " + isFav);
//                House house = new House(desc, subject, email,address, startDate, endDate, phone, spots, price, houseId, isFav);
                House house = new House(desc, subject, email,address, startDate, endDate, phone, spots, price, houseId, isFav,latitude,longitude,distance,sLatitude,sLongitude);
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
