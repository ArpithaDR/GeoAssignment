package com.example.appy.locationidentifier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.appy.utility.AsyncResponse;
import com.example.appy.utility.HttpConnection;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListOfPublicPlaces extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PublicPlacesListAdapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    public List<PublicPlaces> publicPlacesList = new ArrayList<PublicPlaces>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_public_places);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        adapter = new PublicPlacesListAdapter(this, publicPlacesList);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(adapter);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Double latValue = Double.parseDouble(bundle.getString("Latitude"));
            Double longValue = Double.parseDouble(bundle.getString("Longitude"));
            prepareListOfHouses(latValue,longValue);
        }
    }

    private void prepareListOfHouses(Double latVal, Double longVal) {
        LatLng latLng = new LatLng(latVal, longVal);
        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/getNearbyPublicPlaces?latitude=" +
                latLng.latitude + "&longitude=" + latLng.longitude;
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
                extractPlacesFromResult(houses);
            }
        });
        httpConnection.execute(s);
        //fetchFavourites();
    }

    void extractPlacesFromResult(JSONObject obj) {
        try {

            JSONArray placesArray = (JSONArray) obj.get("public_places");
            for (int i = 0; i < placesArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) placesArray.get(i);
                String name = (String) jsonObject.get("name");
                String address = (String) jsonObject.get("st_address");
                String city = (String) jsonObject.get("city");
                Double distance = (Double) jsonObject.get("distance");
                int placeId = (int) jsonObject.get("id");
                PublicPlaces pPlaces = new PublicPlaces(placeId, name, address, city, distance);
                publicPlacesList.add(pPlaces);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }
}
