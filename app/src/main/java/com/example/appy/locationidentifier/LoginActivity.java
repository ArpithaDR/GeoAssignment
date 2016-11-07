package com.example.appy.locationidentifier;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appy.utility.AsyncResponse;
import com.example.appy.utility.HttpConnection;
import com.example.appy.utility.SessionManagement;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.internal.CallbackManagerImpl.RequestCodeOffset.Login;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    //private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProfileTracker mProfileTracker;
    SessionManagement session;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());  // late check by uncommenting this line
        setContentView(R.layout.activity_login);
        //info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");

        callbackManager = CallbackManager.Factory.create();

        session = new SessionManagement(getApplicationContext());

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        loginButton.setVisibility(View.INVISIBLE);

                        if(Profile.getCurrentProfile() == null) {
                            mProfileTracker = new ProfileTracker() {
                                @Override
                                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                                    // profile2 is the new profile
                                    Log.v("facebook - profile", profile2.getFirstName());
                                    Log.v("facebook - profile", profile2.getId());
                                    Log.v("facebook - profile", profile2.getLastName());
                                    Log.v("facebook - profile", profile2.getName());
                                    String fbData = "id=" + profile2.getId() + "&full_name=" + profile2.getName() + "&first_name=" + profile2.getFirstName() + "&last_name=" + profile2.getLastName();
                                    String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/fbLogin?" + fbData;
                                    session.storeLoggedInUserId(profile2.getId());
                                    goMapsScreen(s);
                                    mProfileTracker.stopTracking();
                                }
                            };
                            // no need to call startTracking() on mProfileTracker
                            // because it is called by its constructor, internally.
                        }
                        else {
                            Profile profile = Profile.getCurrentProfile();
                            Log.v("facebook - profile", profile.getFirstName());
                            Log.v("facebook - profile", profile.getId());
                            Log.v("facebook - profile", profile.getLastName());
                            Log.v("facebook - profile", profile.getName());
                            String fbData = "id=" + profile.getId() + "&full_name=" + profile.getName() + "&first_name=" + profile.getFirstName() + "&last_name=" + profile.getLastName();
                            String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/fbLogin?" + fbData;
                            session.storeLoggedInUserId(profile.getId());
                            goMapsScreen(s);
                        }

//                        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/fbLogin?" + fbData;
//                        goMapsScreen(s);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        //info.setText("Login attempt canceled.");
                        //Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        //info.setText("Login attempt failed.");
                        //Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goMapsScreen(String s) {

        HttpConnection httpConnection = new HttpConnection(this, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String s = (String) output;
                System.out.println("In LoginActivity Shivalik:" + s);
            }
        });
        httpConnection.execute(s);

        System.out.println("This is a test for the facebook login -- Shivalik");
        Intent intent = new Intent(this, MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
