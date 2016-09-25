package com.example.appy.locationidentifier;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by appy on 9/24/16.
 */

public class AddressToLatLong extends FragmentActivity{

    private static final String TAG = "AddressToLatLong";
    private static MapView myMapView;
    private static GoogleMap mMap;


    public static void getAddressFromLocation(final String locationAddress,
                                              final Context context) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                Address address;
                String result = null;

                try {
                    List addressList= geocoder.getFromLocationName(locationAddress, 1);
                    if (addressList != null && addressList.size() > 0) {
                        address = (Address) addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        sb.append(address.getLatitude()).append("\n");
                        sb.append(address.getLongitude()).append("\n");
                        result = sb.toString();
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions();

                        markerOptions.position(latLng);
                        markerOptions.title(locationAddress);
                        //mMap = GoogleMap;
                        mMap.clear();
                        mMap.addMarker(markerOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable to connect to Geocoder", e);
                } finally {

                    if (result != null) {

                        result = "Address: " + locationAddress +
                                "\n\nLatitude and Longitude :\n" + result;
                        Log.i(TAG,"Address: " + result);

                    } else {

                        result = "Address: " + locationAddress +
                                "\n Unable to get Latitude and Longitude for this address location.";
                        Log.i(TAG, "Address: " + result);
                    }
                }
            }
        };
        thread.start();
    }
}
