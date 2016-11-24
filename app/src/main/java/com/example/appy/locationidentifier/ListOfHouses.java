package com.example.appy.locationidentifier;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class ListOfHouses extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HouseListAdapter adapter;
    private List<House> houseList;
    private List<House> favList;
    RecyclerView.LayoutManager mLayoutManager;

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

        prepareListOfHouses();
    }

    //This is static and this information must be fetched from database later
    private void prepareListOfHouses() {
        int[] covers = new int[]{
                R.drawable.images1,
                R.drawable.images2,
                R.drawable.images3,
                R.drawable.images4,
                R.drawable.images5,
        };

        for (int index = 0; index < 5; index++) {
            House a = new House("House Summary" + index, 400, covers[index], index);
            houseList.add(a);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
