package com.example.appy.locationidentifier;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appy.utility.AsyncResponse;
import com.example.appy.utility.HttpConnection;
import com.example.appy.utility.SessionManagement;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.appy.locationidentifier.R.id.fab;
import static com.example.appy.locationidentifier.R.id.textView1;


//This is the main screen page

public class MapsActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private ProgressDialog progress;
    public Dialog dialog;
    Address searchAddress;
    //int radius;
    private SeekBar seekBar;
    private TextView textView;

    public String first_name = "";
    public String last_name = "";
    private String phone_number = "";
    private String email = "";

    //For hamburger
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    //Called when user clicks search button
    public void sendLocation(View view) {
        EditText editText = (EditText) findViewById(R.id.searchView1);
        String address = editText.getText().toString();
        Log.i(TAG,"Text Values: " + address);
        search(address, getApplicationContext());
    }

    public void clearValues(View view) {
        EditText searchbar = (EditText) findViewById(R.id.searchView1);
        searchbar.setText("");
        mMap.clear();
        findViewById(R.id.list_view).setEnabled(false);
        fetchLocation();
    }

    protected void search(String locationAddress, final Context context) {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String result = null;
        try {
            List addressList = geocoder.getFromLocationName(locationAddress, 1);
            if (addressList != null && addressList.size() > 0) {
                searchAddress = (Address) addressList.get(0);
                StringBuilder sb = new StringBuilder();
                sb.append(searchAddress.getLatitude()).append("\n");
                sb.append(searchAddress.getLongitude()).append("\n");
                result = sb.toString();
                LatLng latLng = new LatLng(searchAddress.getLatitude(), searchAddress.getLongitude());

                mMap.clear();
                MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng).draggable(true)
                .title(locationAddress)
                .snippet("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                 // working - long press will drag it. But custom dragging?

                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                mMap.setOnMarkerDragListener(markerDragListener());
               /* CircleOptions circleOptions = new CircleOptions()
                        .center(latLng)
                        .radius(1609.344)
                        .strokeWidth(2)
                        .strokeColor(Color.BLUE)
                        .fillColor(Color.parseColor("#500084d3"));
                // Supported formats are: #RRGGBB #AARRGGBB
                //   #AA is the alpha, or amount of transparency

                mMap.addCircle(circleOptions);*/
                fetchHousesFromDB(latLng);
                findViewById(R.id.list_view).setEnabled(true);

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
                Toast.makeText(MapsActivity.this, "Found no nearby subleases", Toast.LENGTH_LONG).show();
                mMap.clear();
                fetchLocation();
            }
        }
    }

    public void listViewOfHouses(View view) {
        Intent intent = new Intent(this, ListOfHouses.class);
        //System.out.println("ResultList: " + resultsList);
        if (searchAddress != null) {
            intent.putExtra("Latitude", Double.toString(searchAddress.getLatitude()));
            intent.putExtra("Longitude", Double.toString(searchAddress.getLongitude()));
        }
        startActivity(intent);
    }

    private GoogleMap.OnMarkerDragListener markerDragListener() {

        return new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                System.out.println("Marker Dragging");
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                // TODO Auto-generated method stub
                LatLng markerLocation = marker.getPosition();
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                mMap.animateCamera(CameraUpdateFactory.newLatLng(markerLocation));
                /*CircleOptions circleOptions = new CircleOptions()
                        .center(markerLocation)
                        .radius(1609.344)
                        .strokeWidth(2)
                        .strokeColor(Color.BLUE)
                        .fillColor(Color.parseColor("#500084d3"));
                // Supported formats are: #RRGGBB #AARRGGBB
                //   #AA is the alpha, or amount of transparency

                mMap.addCircle(circleOptions);*/
                try {
                    addresses = geocoder.getFromLocation(markerLocation.latitude, markerLocation.longitude, 1);
                    StringBuffer address = new StringBuffer();
                    address.append(addresses.get(0).getAddressLine(0));
                    address.append(" ");
                    address.append(addresses.get(0).getAddressLine(1));
                    EditText searchbar = (EditText) findViewById(R.id.searchView1);
                    searchbar.setText(address.toString());

                } catch (IOException e) {
                    Toast.makeText(MapsActivity.this, "Please make sure you are connected to internet", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub
                System.out.println("Marker Started");

            }
        };
    }

    private void fetchHousesFromDB(LatLng latLng) {
        //String value = textView.getText().toString();
        int rad = seekBar.getProgress();
        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/searchHouseAvailable?latitude=" +
                latLng.latitude + "&longitude=" + latLng.longitude +"&radius="+ rad;
        HttpConnection httpConnection = new HttpConnection(this, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                if (output != null) {
                    String result = (String) output;
                    JSONObject houses = null;
                    try {
                        if (!result.isEmpty())
                            houses = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                    if (houses != null) {
                        ArrayList<MarkerOptions> resultArray = extractHousesFromResult(houses);
                        for (MarkerOptions mo : resultArray) {
                            mMap.addMarker(mo);
                        }
                    }
                    else {
                        mMap.clear();
                    }
                }
            }
        });
        httpConnection.execute(s);
    }



    ArrayList<MarkerOptions> extractHousesFromResult(JSONObject obj) {
        ArrayList<MarkerOptions> results = new ArrayList<>();
        //resultsList = new ArrayList<House>();

        // Read json object

        try {
            JSONArray houseArray = (JSONArray) obj.get("houseList");
            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
            SimpleDateFormat sdf=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
            for(int i=0; i< houseArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) houseArray.get(i);
                double lat = (double) jsonObject.get("Latitude");
                double lng = (double) jsonObject.get("Longitude");
                String resultAddress = (String) jsonObject.get("StreetAddress");
                LatLng latlng = new LatLng(lat, lng);
                MarkerOptions marker = new MarkerOptions();
                marker.position(latlng);
                marker.title(resultAddress);
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                results.add(marker);
                    /**/
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        textView = (TextView) findViewById(R.id.textView1);
        seekBar.setProgress(1);
        textView.setText("Range: " + seekBar.getProgress() + "/" + seekBar.getMax());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText("Range: " + progress + "/" + seekBar.getMax());
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        findViewById(R.id.list_view).setEnabled(false);

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getCurrentLocation();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog();
            }
        });

    }



    public void CustomDialog() {
        dialog = new Dialog(MapsActivity.this);
        // it remove the dialog title
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set the laytout in the dialog
        dialog.setContentView(R.layout.dialogbox);
        // set the background partial transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        // set the layout at right bottom
        param.gravity = Gravity.BOTTOM | Gravity.LEFT;
        // it dismiss the dialog when click outside the dialog frame
        dialog.setCanceledOnTouchOutside(true);

        final View gymButton = (View) dialog.findViewById(R.id.gym);
        gymButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView gymtext = (TextView) dialog.findViewById(R.id.gymtext);
                highlightOptions(gymtext.getText().toString());
            }
        });

        View groceryButton = (View) dialog.findViewById(R.id.grocery);
        groceryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView grocerytext = (TextView) dialog.findViewById(R.id.grocerytext);
                highlightOptions(grocerytext.getText().toString());
            }
        });

        View hospitalButton = (View) dialog.findViewById(R.id.hospital);
        hospitalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView hospitaltext = (TextView) dialog.findViewById(R.id.hospitaltext);
                highlightOptions(hospitaltext.getText().toString());
            }
        });

        View schoolButton = (View) dialog.findViewById(R.id.school);
        schoolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView schooltext = (TextView) dialog.findViewById(R.id.schooltext);
                highlightOptions(schooltext.getText().toString());
            }
        });

        View crossButton = (View) dialog.findViewById(R.id.cross);
        crossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dissmiss the dialog
                dialog.dismiss();
            }
        });
        // it show the dialog box
        dialog.show();
    }

    private void highlightOptions(String s) {
        getCurrentLocation();
        s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/getPublicPlaces?option=" + s;
        HttpConnection connect = new HttpConnection(this, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String result = (String) output;
                ArrayList<MarkerOptions> placesArray = extractPlaces(result);

                if(placesArray.size() > 0) {
                    dialog.dismiss();
                    LatLng searchLatLng = new LatLng(searchAddress.getLatitude(), searchAddress.getLongitude());
//                    mMap.clear();
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(searchLatLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                } else {
                    Toast.makeText(MapsActivity.this, "No places to display. Try another", Toast.LENGTH_SHORT).show();
                    System.out.println("Places Array empty");
                }

                for(MarkerOptions mo : placesArray) {
                    mMap.addMarker(mo);
                }
            }
        });
        connect.execute(s);

    }

    private void getCurrentLocation() {
        EditText editText = (EditText) findViewById(R.id.searchView1);
        String address = editText.getText().toString();
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List addressList = geocoder.getFromLocationName(address, 1);
            if (addressList != null && addressList.size() > 0) {
                searchAddress = (Address) addressList.get(0);
                LatLng latLng = new LatLng(searchAddress.getLatitude(), searchAddress.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        private ArrayList<MarkerOptions> extractPlaces(String result) {
        ArrayList<MarkerOptions> results = new ArrayList<>();;
        JSONObject places = null;
        try {
            places = new JSONObject(result);
            JSONArray placesArray = (JSONArray) places.get("public_places");
            for(int i=0; i<placesArray.length(); i++) {
                JSONObject place = (JSONObject) placesArray.get(i);
                double lat = Double.parseDouble((String) place.get("latitude"));
                double lng = Double.parseDouble((String) place.get("longitute"));
                String resultAddress = (String) place.get("st_address");
                String category_type = (String) place.get("category_type");
                LatLng latlng = new LatLng(lat, lng);
                MarkerOptions marker = new MarkerOptions();
                marker.position(latlng);
                marker.title(resultAddress);
                float colorCode;
                switch (category_type) {
                    case "Hospital" : colorCode = BitmapDescriptorFactory.HUE_ROSE; break; //HUE_ROSE
                    case "School"   : colorCode = BitmapDescriptorFactory.HUE_BLUE; break; //HUE_BLUE
                    case "Gym"   : colorCode = BitmapDescriptorFactory.HUE_ORANGE; break;
                    case "Grocery Store"   : colorCode = BitmapDescriptorFactory.HUE_AZURE; break;
                    default : colorCode = BitmapDescriptorFactory.HUE_VIOLET;
                }
                marker.icon(BitmapDescriptorFactory.defaultMarker(colorCode));
                results.add(marker);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("UserName");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hamburger_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Add elements of hamburger
    private void addDrawerItems() {
        String[] hamburgerArray = { "Post Ad", "Favourite Posts", "View Your Ads", "Profile", "Help", "Logout" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hamburgerArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (String)parent.getItemAtPosition(position);
                if(position==0) {
                    Intent myIntent = new Intent(MapsActivity.this, PostMyAdForm.class);
                    startActivity(myIntent);
                } else if(position==1) {
                    Intent myIntent = new Intent(MapsActivity.this, FavListOfHouses.class);
                    startActivity(myIntent);
                } else if(position==2) {
                    Intent myIntent = new Intent(MapsActivity.this, ViewMyAds.class);
                    startActivity(myIntent);
                } else if(position==3) {
                    viewProfile();
                } else if(position==4) {
                    Intent myIntent = new Intent(MapsActivity.this, Help.class);
                    startActivity(myIntent);
                } else if(position==5) {
                    fbLogout();
                }
            }
        });
    }

    public void fbLogout() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();
        finish();
    }

    public void viewProfile() {
        SessionManagement session = new SessionManagement(getApplicationContext());
        String id = session.getLoggedInUserId();
        Log.v("Shivalik test - " , id);
        String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/getUser?user_id=" + id;

        HttpConnection httpConnection = new HttpConnection(MapsActivity.this, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                String result = (String) output;

                JSONObject user = null;
                try {
                    user = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.v("user: ", user.toString());
                try {
                    first_name = user.getString("first_name");
                    last_name = user.getString("last_name");
                    phone_number = user.getString("phone_number");
                    email = user.getString("email");

                    Intent i = new Intent(MapsActivity.this, UserProfileActivity.class);
                    i.putExtra("fname", first_name);
                    i.putExtra("lname", last_name);
                    i.putExtra("ph_number", phone_number);
                    i.putExtra("email", email);
                    startActivity(i);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        httpConnection.execute(s);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerDragListener(markerDragListener());
    }

    public void fetchLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (location == null) {
                Log.i(TAG, "Location services was null. Requesting location.");
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                handleNewLocation(location);
            }
        }
        else {
            Log.d(TAG, "Location services permissions not granted");
            //TODO Show the pop up to provide location access request

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        fetchLocation();
    }

    private void handleNewLocation(Location location) {

        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);
            StringBuffer address = new StringBuffer();
            address.append(addresses.get(0).getAddressLine(0));
            address.append(", ");
            address.append(addresses.get(0).getAddressLine(1));
            EditText searchbar = (EditText) findViewById(R.id.searchView1);
            searchbar.setText(address.toString());

        } catch (IOException e) {
            Toast.makeText(MapsActivity.this, "Please make sure you are connected to internet", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .draggable(true)
                .title("You are here!");
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMarkerDragListener(markerDragListener());
        /*CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .radius(1609.344)
                .strokeWidth(2)
                .strokeColor(Color.BLUE)
                .fillColor(Color.parseColor("#500084d3"));
        // Supported formats are: #RRGGBB #AARRGGBB
        //   #AA is the alpha, or amount of transparency

        mMap.addCircle(circleOptions);*/
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

}
