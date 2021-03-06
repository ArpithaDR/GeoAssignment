package com.example.appy.locationidentifier;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

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

    public FavListAdapter(Context mContext, List<House> favList) {

        this.mContext = mContext;
        this.favList = favList;
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
        holder.title.setText(house.getDesc());
        holder.price.setText(house.getPrice() + "USD");
        holder.favbtn.setImageResource(R.drawable.favorite);
        holder.favbtn.setTag("Fav");
        // loading house image using Glide library
        //Glide.with(mContext).load(house.getThumbnail()).into(holder.thumbnail);

        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/getImage?house_id=" + String.valueOf(house.getHouseId());

        HttpConnection httpConnection = new HttpConnection(mContext, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String result = (String) output;

                JSONObject encodedImage = null;
                try {
                    encodedImage = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.v("image: ", encodedImage.toString());
                try {
                    encodedImage = encodedImage.getJSONObject("image");
                    String strBase64 = encodedImage.getString("image_data").replace("\n","");

                    byte[] decodedString = Base64.decode(strBase64, 0);

                    Glide.with(mContext).load(decodedString).asBitmap().placeholder(R.drawable.subleased).into(holder.thumbnail);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        httpConnection.execute(s);

        holder.viewbtn.setOnClickListener(onClickListener(position));
        holder.favbtn.setOnClickListener(onClickListenerFav(position));

    }

    private View.OnClickListener onClickListenerFav(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int houseId = favList.get(position).getHouseId();
                removeFromFavList(houseId, position);

            }
        };
    }

    public void removeFromFavList(int houseId, final int position) {
        SessionManagement session = new SessionManagement(getApplicationContext());
        String userId = session.getLoggedInUserId();
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
                House house = favList.get(position);
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
                intent.putExtra("HouseId", house.getHouseId());
                mContext.startActivity(intent);
            }
        };
    }

    @Override
    public int getItemCount() {
        return favList.size();
    }
}
