package com.example.appy.utility;

import android.content.Context;

/**
 * Created by arorai on 10/16/16.
 */

public class HttpConnectionSingleton {

    private static HttpConnection instance = null;

    HttpConnectionSingleton() {

    }

    public static HttpConnection getInstance(Context c) {
        if(instance == null) {
            instance = new HttpConnection(c);
            return instance;
        } else {
            return instance;
        }
    }
}
