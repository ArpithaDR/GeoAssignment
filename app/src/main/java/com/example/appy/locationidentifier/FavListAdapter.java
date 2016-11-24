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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by appy on 11/9/16.
 */

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.ViewHolder> {

    private Context mContext;
    private List<House> favList;
    public String isFav;
    //private Activity activity;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, price;
        public ImageView thumbnail;
        public Button viewbtn;
        public ImageButton favbtn;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.price);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            viewbtn = (Button) view.findViewById(R.id.viewbtn);
            favbtn = (ImageButton)view.findViewById(R.id.favicon);
            favbtn.setImageResource(R.drawable.favorite);
        }
    }

    /* public void setIfFavorite(int position) {

        int houseId = favList.get(position).getHouseId();
        String housenum = Integer.toString(houseId);
        String userId = "10208655238312268";
        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/checkIfFavHouse?"
                + "userId=" + userId + "&houseId=" + housenum;
        System.out.println("check fav" + s);
        HttpConnection httpConnection = new HttpConnection(mContext, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String result = (String) output;
                JSONObject favHouses = null;
                try {
                    favHouses = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setValue(favHouses);
                System.out.println("The value of string is setifFav: " + isFav);
                if(isFav != null) {
                    if (isFav.contains("true")) {
                        System.out.println("isFav contains");

                    }
                } else {
                    System.out.println("isFav not contains");
                }
                //setValue(retvalue.toString());
                //isFav = retvalue.toString();
            }
        });
        httpConnection.execute(s);

        //return false;
    }

    public void setValue(JSONObject value) {
        isFav= null;
        try {
            JSONArray houseArray = (JSONArray) value.get("favList");

            for (int i = 0; i < houseArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) houseArray.get(i);
                String housevalue = (String) jsonObject.get("value");
                isFav = housevalue;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
       // isFav = housevalue;
        System.out.println("The value of string is in setValue: " + isFav);
    } */

    public FavListAdapter(Context mContext, List<House> favList) {

        this.mContext = mContext;
        this.favList = favList;
        //this.activity = activity;
    }

    @Override
    public FavListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_house_card, parent, false);

        return new FavListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FavListAdapter.ViewHolder holder, final int position) {

        House house = favList.get(position);
        holder.title.setText(house.getName());
        holder.price.setText(house.getPrice() + "USD");
       // setIfFavorite(position);
        holder.favbtn.setImageResource(R.drawable.favorite);
        holder.favbtn.setTag("Fav");
       /* isFav = new String("true");

        if (isFav.contains("true")) {
            holder.favbtn.setImageResource(R.drawable.favorite);
            holder.favbtn.setTag("Fav");
            System.out.println("Entered Fav");
        } else {
            holder.favbtn.setImageResource(R.drawable.notfavorite);
            holder.favbtn.setTag("notFav");
            System.out.println("Entered notFav");
        }*/
        // loading house image using Glide library
        Glide.with(mContext).load(house.getThumbnail()).into(holder.thumbnail);
        holder.viewbtn.setOnClickListener(onClickListener(position));
        holder.favbtn.setOnClickListener(onClickListenerFav(position));
//        notifyDataSetChanged();

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

                int houseId = favList.get(position).getHouseId();
                removeFromFavList(houseId, position);
                //notifyDataSetChanged();
                /*boolean isFav = checkFavButton(v);
                if (isFav) {
                    System.out.println("isFav changed" + isFav);
                    ((ImageButton) v.findViewById(R.id.favicon)).setImageResource(R.drawable.notfavorite);
                    removeFromFavList(houseId);
                } else {
                    ((ImageButton) v.findViewById(R.id.favicon)).setImageResource(R.drawable.favorite);
                    addToFavList(houseId);
                }
               // System.out.println("Click captured");
                //  houseList.get(position).isFavorite =! houseList.get(position).isFavorite; */

            }
        };
    }

    /*public boolean checkFavButton(View v) {
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

        String userId = "10208655238312268";
        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/addFavHouse?"
                + "userId=" + userId + "&houseId=" + houseId;
        System.out.println("add fav" + s);
        HttpConnection httpConnection = new HttpConnection(mContext, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
            }
        });
        httpConnection.execute(s);
    }*/

    public void removeFromFavList(int houseId, final int position) {
        System.out.println("reached remove" + isFav);
        String userId = "10208655238312268";
        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/removeFavHouse?"
                + "userId=" + userId + "&houseId=" + houseId;
        System.out.println("remove fav" + s);
        HttpConnection httpConnection = new HttpConnection(mContext, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                favList.remove(position);
                notifyDataSetChanged();
            }
        });
        httpConnection.execute(s);

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
        return favList.size();
    }
}
