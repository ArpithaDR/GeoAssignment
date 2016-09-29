package com.example.appy.locationidentifier;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by appy on 9/28/16.
 */

public class HouseListAdapter extends RecyclerView.Adapter<HouseListAdapter.MyViewHolder> {

    private Context mContext;
    private List<House> houseList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, price;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.price);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

        }
    }

    public HouseListAdapter(Context mContext, List<House> houseList) {

        this.mContext = mContext;
        this.houseList = houseList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_house_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        House house = houseList.get(position);
        holder.title.setText(house.getName());
        holder.price.setText(house.getPrice() + "USD");

        // loading house image using Glide library
        Glide.with(mContext).load(house.getThumbnail()).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return houseList.size();
    }
}
