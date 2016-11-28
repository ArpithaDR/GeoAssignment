package com.example.appy.locationidentifier;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appy.utility.AsyncResponse;
import com.example.appy.utility.HttpConnection;
import com.example.appy.utility.SessionManagement;

import java.text.DecimalFormat;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

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

            ((ImageButton) view.findViewById(R.id.favicon)).setImageResource(isFavorite ? R.drawable.favorite : R.drawable.notfavorite);
            favbtn = (ImageButton) view.findViewById(R.id.favicon);

        }
    }

    public boolean returnIfFavourite(View myview) {
        return true;
    }

    public HouseListAdapter(Context mContext, List<House> houseList) {

        this.mContext = mContext;
        this.houseList = houseList;
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
        holder.title.setText(house.getDesc());
        holder.price.setText(house.getPrice() + "USD");
        if(house.isfav) {
            holder.favbtn.setImageResource(R.drawable.favorite);
            holder.favbtn.setTag("Fav");
        } else {
            holder.favbtn.setImageResource(R.drawable.notfavorite);
            holder.favbtn.setTag("notFav");
        }

        // loading house image using Glide library
        Glide.with(mContext).load(house.getThumbnail()).into(holder.thumbnail);
        holder.viewbtn.setOnClickListener(onClickListener(position));
        holder.favbtn.setOnClickListener(onClickListenerFav(position));
    }

    public boolean checkFavButton(View v) {
        ImageView fav_Btn = (ImageView)v.findViewById(R.id.favicon);
        if(fav_Btn.getTag() != null && fav_Btn.getTag().toString().equals("Fav")) {
            System.out.println("Fav");
            return true;
        } else {
            System.out.println("Not Fav");
            return false;
        }


    }

    public void addToFavList(int houseId) {

        SessionManagement session = new SessionManagement(getApplicationContext());
        String userId = session.getLoggedInUserId();
        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/addFavHouse?"
                + "userId=" + userId + "&houseId=" + houseId;
        HttpConnection httpConnection = new HttpConnection(mContext, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
            }
        });
        httpConnection.execute(s);
    }

    public void removeFromFavList(int houseId) {
        SessionManagement session = new SessionManagement(getApplicationContext());
        String userId = session.getLoggedInUserId();
        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/removeFavHouse?"
                + "userId=" + userId + "&houseId=" + houseId;
        System.out.println("remove fav" + s);
        HttpConnection httpConnection = new HttpConnection(mContext, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {

            }
        });
        httpConnection.execute(s);

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
                int houseId = houseList.get(position).getHouseId();
                //changeFavList(v, houseId);
                System.out.println("Click captured");
                boolean isFav = checkFavButton(v);
                if (isFav) {
                    System.out.println("isFav changed" + isFav);
                    ((ImageButton) v.findViewById(R.id.favicon)).setImageResource(R.drawable.notfavorite);
                    ((ImageButton) v.findViewById(R.id.favicon)).setTag("notFav");
                    removeFromFavList(houseId);
                } else {
                    ((ImageButton) v.findViewById(R.id.favicon)).setImageResource(R.drawable.favorite);
                    ((ImageButton) v.findViewById(R.id.favicon)).setTag("Fav");
                    addToFavList(houseId);
                }
                //  houseList.get(position).isFavorite =! houseList.get(position).isFavorite;
               // notifyItemRemoved(position);
            }
        };
    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                House house = houseList.get(position);
                Intent intent = new Intent(mContext, ClickedAdActivity.class);
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
                StringBuffer sb = new StringBuffer();
                Double distVal = house.getDistance();
                DecimalFormat formattedVal = new DecimalFormat("###.##");
                Double value = Double.valueOf(formattedVal.format(distVal));
                String distance = Double.toString(value);
                sb.append(distance);
                sb.append(" ");
                sb.append("mile");
                intent.putExtra("Distance", sb.toString());
                String latitude = Double.toString(house.getLatitude());
                intent.putExtra("Latitude", latitude);
                String longitude = Double.toString(house.getLongitude());
                intent.putExtra("Longitude", longitude);
                String slatitude = Double.toString(house.getsLatitude());
                intent.putExtra("sLat", slatitude);
                String slongitude = Double.toString(house.getsLongitude());
                intent.putExtra("sLong", slongitude);
                mContext.startActivity(intent);
            }
        };
    }

    @Override
    public int getItemCount() {
        return houseList.size();
    }


}
