package com.example.appy.locationidentifier;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appy.utility.AsyncResponse;
import com.example.appy.utility.HttpConnection;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostMyAdForm extends AppCompatActivity {

    public static final String TAG = PostMyAdForm
            .class.getSimpleName();
    private ProgressDialog progress;

    ImageButton cameraBtn, galleryBtn;
    private int PICK_IMAGE_REQUEST = 1;
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    String imageEncoded;
    List<String> imagesEncodedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_my_ad_form);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        cameraBtn = (ImageButton)findViewById(R.id.camera_button);
        galleryBtn = (ImageButton)findViewById(R.id.gallery_button);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent in = new Intent(PostMyAdForm.this, PostMyAdForm.class);
//                startActivityForResult();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            // When an Image is picked
            if (requestCode == 1 && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Log.v("LOG_TAG", "result was OK");

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Log.v("LOG_TAG filepath - ", filePathColumn.toString());
                imagesEncodedList = new ArrayList<String>();
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();
                    Log.v("LOG_TAG uri - ", mImageUri.toString());

                    ImageView imageView = (ImageView) findViewById(R.id.gV0);


                    Bitmap bmp = null;
                    try {
                        bmp = getBitmapFromUri(mImageUri);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(bmp);


                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();

                    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    Log.v("64 encod single- - - ", encoded);



//                    // Get the cursor
//                    Cursor cursor = getContentResolver().query(mImageUri,
//                            filePathColumn, null, null, null);
//                    // Move to first row
//                    cursor.moveToFirst();
//
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    Toast.makeText(this, columnIndex, Toast.LENGTH_LONG).show();
//                    imageEncoded  = cursor.getString(columnIndex);
//                    cursor.close();
//
//                    Log.v("LOG_TAG image - ", imageEncoded);

                }else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);

                            Bitmap bmp = null;
                            try {
                                bmp = getBitmapFromUri(uri);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }


                                if(i==0) {
                                    ImageView imageView = (ImageView) findViewById(R.id.gV0);
                                    imageView.setImageBitmap(bmp);
                                }
                                if(i==1) {
                                    ImageView imageView = (ImageView) findViewById(R.id.gV1);
                                    imageView.setImageBitmap(bmp);
                                }
                                if(i==2) {
                                    ImageView imageView = (ImageView) findViewById(R.id.gV2);
                                    imageView.setImageBitmap(bmp);
                                }
                                if(i==3) {
                                    ImageView imageView = (ImageView) findViewById(R.id.gV3);
                                    imageView.setImageBitmap(bmp);
                                }
                                if(i==4) {
                                    ImageView imageView = (ImageView) findViewById(R.id.gV4);
                                    imageView.setImageBitmap(bmp);
                                }
                                if(i==5) {
                                    ImageView imageView = (ImageView) findViewById(R.id.gV5);
                                    imageView.setImageBitmap(bmp);
                                }

                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream .toByteArray();

                            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                            Log.v("64 encod multiple- - - ", encoded);


//                            // Get the cursor
//                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
//                            // Move to first row
//                            cursor.moveToFirst();
//
//                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                            imageEncoded  = cursor.getString(columnIndex);
//                            imagesEncodedList.add(imageEncoded);
//                            cursor.close();

                            //Toast.makeText(this, imageEncoded.toString(), Toast.LENGTH_LONG).show();

                        }
                        //Toast.makeText(this, imagesEncodedList.toString(), Toast.LENGTH_SHORT).show();
                        Log.v("LOG_TAG", "Selected Images uri" + mArrayUri.toString());
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);


//        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//
//            ImageView imageView = (ImageView) findViewById(R.id.galleryImgV);
//
//            Bitmap bmp = null;
//            try {
//                bmp = getBitmapFromUri(selectedImage);
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            imageView.setImageBitmap(bmp);
//
//        }

    }


    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    public void submitAddress(View arg0) {
        EditText title_et = (EditText) findViewById(R.id.post_ad_title);
        title_et.setText("my Title");
        String title = title_et.getText().toString();

        EditText address_et = (EditText) findViewById(R.id.post_ad_address);
        address_et.setText("2140 Oak Street");
        String address = address_et.getText().toString();

        EditText aptNum_et = (EditText) findViewById(R.id.post_ad_aptNumber);
//        aptNum_et.setText("2");
        String aptNum = aptNum_et.getText().toString(); // integer check, null check handled in python

        EditText city_et = (EditText) findViewById(R.id.post_ad_city);
        city_et.setText("Los Angeles");
        String city = city_et.getText().toString();

        EditText state_et = (EditText) findViewById(R.id.post_ad_state);
        state_et.setText("California");
        String state = state_et.getText().toString();

        EditText zipCd_et = (EditText) findViewById(R.id.post_ad_zip);
        zipCd_et.setText("90007");
        String zipcd = zipCd_et.getText().toString(); // int check

        EditText price_et = (EditText) findViewById(R.id.post_ad_price);
        price_et.setText("450");
        String price = price_et.getText().toString();

        EditText vacancy_et = (EditText) findViewById(R.id.post_ad_spots);
        vacancy_et.setText("2");
        String vacancy = vacancy_et.getText().toString();

        EditText startdate_et = (EditText) findViewById(R.id.post_ad_start_date);
        startdate_et.setText("2017-1-3");
        EditText enddate_et = (EditText) findViewById(R.id.post_ad_end_date);
        enddate_et.setText("2017-5-3");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startdate = new Date(), enddate = new Date();
        try {
            startdate = df.parse(startdate_et.getText().toString());
            enddate = df.parse(enddate_et.getText().toString());
            String myText = startdate.getDate() + "-" + (startdate.getMonth() + 1) + "-" + (1900 + startdate.getYear());
            Log.i(TAG, "Start date: " + startdate + ", end date: " + enddate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        EditText desc_et = (EditText) findViewById(R.id.post_ad_desc);
        desc_et.setText("my desc");
        String description = desc_et.getText().toString();

        EditText phone_et = (EditText) findViewById(R.id.post_ad_phone);
        phone_et.setText("87681");
        String phone = phone_et.getText().toString();

        Address geoCodedAddress;
        double lat = 0.0, lng = 0.0;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List addressList = null;
        try {
            addressList = geocoder.getFromLocationName(address+city+state, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addressList != null && addressList.size() > 0) {
            geoCodedAddress = (Address) addressList.get(0);
            lat = geoCodedAddress.getLatitude();
            lng = geoCodedAddress.getLongitude();

            Log.i(TAG,"Title: " + title +
                    ", Address: " + address +
                    ", Apt Num: " + aptNum +
                    ", City: " + city +
                    ", State: " + state +
                    ", Price: " + price +
                    ", vacancy: " + vacancy +
                    ", desc: " + description +
                    ", Phone: " + phone
            );

            Toast.makeText(getBaseContext(), "Submitted!" , Toast.LENGTH_SHORT ).show();
            String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/addAddress?title=" + title +
                    "&address=" + address + "&aptNum=" + aptNum + "&city=" + city + "&state=" + state + "&zipcd=" + zipcd + "&price=" + price +
                    "&vacancies=" + vacancy + "&start_date="+ startdate + "&end_date=" + enddate +
                    "&description=" + description + "&phone_number=" + phone + "&lat=" + lat + "&lng=" + lng;

            HttpConnection httpConnection = new HttpConnection(this, new AsyncResponse() {
                @Override
                public void processFinish(Object output) {
                    String s = (String) output;
                    System.out.println("In PostMyAdForm finish process with result:" + s);
                }
            });
            httpConnection.execute(s);
        } else {
            Toast.makeText(getBaseContext(), "Incorrect address, please correct and hit Submit again." , Toast.LENGTH_SHORT ).show();
        }

    }

    public void test(String s) {
        Log.i(TAG, s);
    }
}
