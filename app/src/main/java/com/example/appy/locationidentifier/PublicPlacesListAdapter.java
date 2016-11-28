package com.example.appy.locationidentifier;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by appy on 11/27/16.
 */

public class PublicPlacesListAdapter extends RecyclerView.Adapter<PublicPlacesListAdapter.ViewHolder> {

    private Context mContext;
    private List<PublicPlaces> publicPlacesList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, address;
        public TextView distance;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            address = (TextView) view.findViewById(R.id.address);
            distance = (TextView) view.findViewById(R.id.distance);

        }
    }

    public PublicPlacesListAdapter(Context mContext, List<PublicPlaces> placesList) {

        this.mContext = mContext;
        this.publicPlacesList = placesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_public_places_card, parent, false);

        return new PublicPlacesListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PublicPlacesListAdapter.ViewHolder holder, int position) {
        PublicPlaces pPlaces = publicPlacesList.get(position);
        holder.title.setText(pPlaces.getName());
        StringBuffer sb = new StringBuffer();
        sb.append(pPlaces.getAddress());
        sb.append(" ");
        sb.append(pPlaces.getCity());
        holder.address.setText(sb.toString());
        Double distValue = pPlaces.getDistance();
        DecimalFormat formattedVal = new DecimalFormat("###.##");
        Double val = Double.valueOf(formattedVal.format(distValue));
        String dist = Double.toString(val);
        StringBuffer sb1 = new StringBuffer();
        sb1.append(dist);
        sb1.append(" ");
        sb1.append("mile");
        holder.distance.setText(sb1.toString());
    }

    @Override
    public int getItemCount() {
        return publicPlacesList.size();
    }
}
