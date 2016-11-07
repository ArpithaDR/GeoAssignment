package com.example.appy.locationidentifier;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by appy on 9/28/16.
 */

public class HouseListAdapter extends RecyclerView.Adapter<HouseListAdapter.ViewHolder> {

    private Context mContext;
    private List<House> houseList;
    //private Activity activity;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, price;
        public ImageView thumbnail;
        public Button viewbtn;
        public ImageButton favbtn;
        public boolean isFavorite;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.price);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            viewbtn = (Button) view.findViewById(R.id.viewbtn);
            isFavorite = returnIfFavourite(view);

            ((ImageButton) view.findViewById(R.id.favicon)).setImageResource(isFavorite ? R.drawable.favorite : R.drawable.addfavorite);
            favbtn = (ImageButton) view.findViewById(R.id.favicon);

        }
    }

    public boolean returnIfFavourite(View myview) {
        return true;
    }

    public HouseListAdapter(Context mContext, List<House> houseList) {

        this.mContext = mContext;
        this.houseList = houseList;
        //this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_house_card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        House house = houseList.get(position);
        holder.title.setText(house.getName());
        holder.price.setText(house.getPrice() + "USD");

        // loading house image using Glide library
        Glide.with(mContext).load(house.getThumbnail()).into(holder.thumbnail);
        holder.viewbtn.setOnClickListener(onClickListener(position));
        holder.favbtn.setOnClickListener(onClickListenerFav(position));

    }

    private void setDataToView(TextView description, TextView price, final int position) {
        description.setText(houseList.get(position).getName());
        price.setText(houseList.get(position).getPrice());
    }

    private View.OnClickListener onClickListenerFav(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This code has to be changed to compare the item at getposition is in the fav
                //list of that user. This data needs to be fetched form database
                //The below code will require database connection and checking.The one
                //currently present is solely for testing purpose

                //Check if that position item was in fav table for that user, If present remove
                // and change the icon and if not add to the fav table and change icon
                changeFavList(v);
                System.out.println("Click captured");
                //  houseList.get(position).isFavorite =! houseList.get(position).isFavorite;
                notifyDataSetChanged();
            }
        };
    }

    private void changeFavList(View view) {

        ((ImageButton) view.findViewById(R.id.favicon)).setImageResource(R.drawable.addfavorite);
        return;
    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ClickedAdActivity.class);
                mContext.startActivity(intent);
            }
        };
    }

    @Override
    public int getItemCount() {
        return houseList.size();
    }
}
