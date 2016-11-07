package com.example.appy.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by shivalik on 11/3/16.
 */

public class SessionManagement {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "hello"; //need to check on the pref name
    public static final String LOGGED_IN_USER_ID = "logged_in_user_id";

    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void storeLoggedInUserId(String loggedInUserId) {
        editor.putString(LOGGED_IN_USER_ID, loggedInUserId);
        editor.commit();
    }

    public String getLoggedInUserId() {
        return pref.getString(LOGGED_IN_USER_ID, null);
    }
}
