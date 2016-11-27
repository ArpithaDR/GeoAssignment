package com.example.appy.locationidentifier;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appy.utility.AsyncResponse;
import com.example.appy.utility.HttpConnection;

import java.util.List;

/**
 * Created by appy on 11/10/16.
 */

public class MyAdsListAdapter extends RecyclerView.Adapter<MyAdsListAdapter.ViewHolder> {

    private Context mContext;
    private List<House> myAdsList;
    //private Activity activity;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, price;
        public ImageView thumbnail;
        public Button viewbtn;
        public Button deletebtn;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.price);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            viewbtn = (Button) view.findViewById(R.id.viewbtn);
            deletebtn = (Button) view.findViewById(R.id.deletebtn);
        }
    }

    public MyAdsListAdapter(Context mContext, List<House> myAdsList) {
        this.mContext = mContext;
        this.myAdsList = myAdsList;
    }

    @Override
    public MyAdsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_my_ad_house_card, parent, false);

        return new MyAdsListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyAdsListAdapter.ViewHolder holder, final int position) {
        House house = myAdsList.get(position);
        holder.title.setText(house.getDesc());
        holder.price.setText(house.getPrice() + "USD");

        // loading house image using Glide library
        Glide.with(mContext).load(house.getThumbnail()).into(holder.thumbnail);
        holder.viewbtn.setOnClickListener(onClickListener(position));
        holder.deletebtn.setOnClickListener(onClickListenerDelete(position));
    }

    private View.OnClickListener onClickListenerDelete(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int houseId = myAdsList.get(position).getHouseId();
                removeFromList(houseId,position);
                notifyDataSetChanged();
            }
        };
    }

    public void removeFromList(int houseId, final int position) {
        /*SessionManagement session = new SessionManagement(getApplicationContext());
        String id = session.getLoggedInUserId();
        */
        String userId = "1349781218406667";
        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/deleteHouse?"
                + "userId=" + userId + "&houseId=" + houseId;
        HttpConnection httpConnection = new HttpConnection(mContext, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                myAdsList.remove(position);
                notifyDataSetChanged();
            }
        });
        httpConnection.execute(s);
    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                House house = myAdsList.get(position);
                Intent intent = new Intent(mContext, AdDetails.class);
                intent.putExtra("Address", house.getAddress());
                intent.putExtra("Desc", house.getDesc());
                intent.putExtra("Email", house.getEmail());
                intent.putExtra("EndDate", house.getEndDate());
                intent.putExtra("Phone", house.getPhone());
                String price = Double.toString(house.getPrice());
                intent.putExtra("Price", price);
                String spots = Integer.toString(house.getSpots());
                intent.putExtra("Spots", spots);
                intent.putExtra("StartDate", house.getStartDate());
                intent.putExtra("Subject", house.getSubject());
                mContext.startActivity(intent);
            }
        };
    }

    @Override
    public int getItemCount() {
        return myAdsList.size();
    }

}
