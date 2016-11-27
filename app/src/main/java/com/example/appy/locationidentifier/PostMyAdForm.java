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


//        cameraBtn = (ImageButton)findViewById(R.id.camera_button);
        galleryBtn = (ImageButton)findViewById(R.id.gallery_button);

//        cameraBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent in = new Intent(PostMyAdForm.this, PostMyAdForm.class);
////                startActivityForResult();
//            }
//        });

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
                    encoded = encoded.replace("\n","");
                    Log.v("64 encod single- - - ", encoded);
                    imagesEncodedList.add(encoded);



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
                            encoded = encoded.replace("\n","");
                            //Log.v("64 encod multiple- - - ", encoded);
                            imagesEncodedList.add(encoded);


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


        //System.out.println("Shivalik Test - " + imagesEncodedList);

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
            String url_head = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/addAddress?";
            String url_param = "title=" + title +
                    "&address=" + address + "&aptNum=" + aptNum + "&city=" + city + "&state=" + state + "&zipcd=" + zipcd + "&price=" + price +
                    "&vacancies=" + vacancy + "&start_date="+ startdate + "&end_date=" + enddate +
                    "&description=" + description + "&phone_number=" + phone + "&lat=" + lat + "&lng=" + lng;

            url_param = Uri.encode(url_param, "@#&=*+-_.,:!?()/~'%"); //important as sometimes the url dont work properly
            String s = url_head + url_param;
            System.out.println("head:" + url_head);
            System.out.println("params:" + url_param);

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

        //String sfencode = "iVBORw0KGgoAAAANSUhEUgAABkAAAAOECAIAAAB2L2r1AAAAA3NCSVQICAjb4U_gAAAgAElEQVR4nOzdW48ky3YY5rUis3tm73OhRAuSrYsFUyJlyYJhG_Cv86thwK_-JX7wm9_0akAwYBgSKVOGRAoyRYqXc9t7uisj_JDVPT0znTVdPVldkVXfByK5T093VeTKiMzIlRGR-Tv_yz8LAAAAAOhVOXcBAAAAAOAQCSwAAAAAuiaBBQAAAEDXJLAAAAAA6JoEFgAAAABdk8ACAAAAoGsSWAAAAAB0TQILAAAAgK5JYAEAAADQNQksAAAAALomgQUAAABA1ySwAAAAAOiaBBYAAAAAXZPAAgAAAKBrElgAAAAAdG1s07mLAAAAAADLjMACAAAAoGsSWAAAAAB0TQILAAAAgK5JYAEAAADQNQksAAAAALomgQUAAABA1ySwAAAAAOiaBBYAAAAAXZPAAgAAAKBrElgAAAAAdE0CCwAAAICuSWABAAAA0DUJLAAAAAC6JoEFAAAAQNcksAAAAADo2hhRz10GAAAAAFhkBBYAAAAAXZPAAgAAAKBrElgAAAAAdE0CCwAAAICuSWABAAAA0DUJLAAAAAC6JoEFAAAAQNfGzDx3GQAAAABg0dhaO3cZAAAAAGCRBBYAAAAAXZPAAgAAAKBrElgAAAAAdM1bCAEAAADomrcQAgAAANA1I7AAAAAA6NoohwUAAABAz2SvAAAAAOiaBBYAAAAAXZPAAgAAAKBrElgAAAAAdE0CCwAAAICuSWABAAAA0DUJLAAAAAC6NkbUc5cBAAAAABYZgQUAAABA1ySwAAAAAOiaBBYAAAAAXZPAAgAAAKBrElgAAAAAdE0CCwAAAICuSWABAAAA0LWxFDksAAAAAPo1TtN07jIAAAAAwKKx1nruMgAAAADAIvMHAQAAAOjaOGSeuwwAAAAAsGhMCSwAAAAAOja21s5dBgAAAABYJIEFAAAAQNdMIQQAAACga95CCAAAAEDXJLAAAAAA6JoEFgAAAABdG-u5SwAAAAAABxiBBQAAAEDXJLAAAAAA6JoEFgAAAABdk8ACAAAAoGsSWAAAAAB0bWytnbsMAAAAALBoLMUgLAAAAAD6Ne52u3OXAQAAAAAWmUIIAAAAQNfG97e35y4DAAAAACwyAgsAAACAro11N527DAAAAACwaMzMc5cBAAAAABZJYAEAAADQNQksAAAAALomgQUAAABA18q5CwAAAAAAh4xR27nLAAAAAACLjMACAAAAoGtjswQWAAAAAB0zAgsAAACArklgAQAAANC1sTWLuAMAAADQLyOwAAAAAOiaEVgAAAAAdG00CAsAAACAnsleAQAAANA1CSwAAAAAuiaBBQAAAEDXJLAAAAAA6JoEFgAAAABdk8ACAAAAoGsSWAAAAAB0bYwmhwUAAABAv0pERFZbW1tbW1tbW1tbW1tbW1tbW9s-t2NkjaiRYWtra2tra2tra2tra2tra2tr2-E2_7_L8HAAAAAPRqzMxzlwEAAAAAFlnBHQAAAICuGYEFAAAAQNcksAAAAADo2tjkrwAAAADomDWwAAAAAOja4hTC1tobF-W8xIGXUE946tgp2NdWT7QXXkI9mYnDYeIzEwdeQj2Z6afxEurJtoy11mf_4drWxhIHXkI94aml-rDk2uqJ9sJLqCczcThMfGbiwEuoJzP9NF5CPdkWi7jviQMvoZ7wlPpwmPjwEurJTBwOE5-ZOPAS6slMHHgJ9WRbTCHcEwdeQj3hKUOOD9NeeAn1ZCYOh4nPTBx4CfVkpp_GS6gn2zIuHQAHZiYOvIR6wkuoJzNx4CXUk5k4HCY-M3HgJdSTw8SHl1BPzmucpunZf7i2oXTmivMS6glPmTN_mPbCS6gnM3E4THxm4sBLqCcz_TReQj3ZFou47y1lUq8tDhymnvDUsU9grq2eaC-8hHoyE4fDxGcmDryEejLTT-Ml1JNtsQbWnjjwEuoJT5kzf5j2wkuoJzNxOEx8ZuLAS6gnM_00XkI92Zbx3AV4azKmh2nAs2PjcG0dBfWEb6G9HCYOh3_UuOzRBx4iWurJ86rM-dVvoX6wFNbOa8uJrAkembicJj4HCY-M3GYicNh4jMTh8PEZyYOM3E4THxm4nCY-MzEYSYOh4nP7FxxuLoEljmuh4nPbK2MsvjMxGF2qXFYIj4zcZg5rx6mnhxmTZ-ZejITh5nz6mHqyWHOqzP1ZLaVOCyugXWpB-ZY4nCY-BwmPjNxmInDYeIzE4fDxGcmDjNxOEx8ZuJwmPjMxGEmDoeJz-xccShn-VYAAAAAeCEJLAAAAAC6trgGlrcPzMThMPE5THxm4jATh8PEZyYOh4nPTBxm4nCY-MzE4TDxmYnDTBwOE59Zd28hvNQDs5XXQ56L-MzWmtMrPjNxmF1qHJaIz0wcZs6rh6knh3nd-0w9mYnDzHn1MPXkMOfVmXoy20ocLOL-FeJwmPgcJj4zcZiJw2HiMxOHw8RnJg4zcThMfGbicJj4zMRhJg6Hic_MIu4AAAAA8IwxL3MEHAAAAAAXwggsAAAAALo2XuoiZAAAAABcBiOwAAAAAOiaBBYAAAAAXZPAAgAAAKBrY2aeuwwAAAAAsGistZ67DAAAAACwaJym6dxlAAAAAIBF1sACAAAAoGvWwAIAAACgaxJYAAAAAHRtbK2duwwAAAAAsEgCCwAAAICujaVYxx0AAACAfsleAQAAANA1CSwAAAAAuiaBBQAAAEDXJLAAAAAA6JoEFgAAAABdk8ACAAAAoGsSWAAAAAB0bZTDAgAAAKBnY2aeuwwAAAAAsGhsUz13GQAAAABg0VirBBYAAAAA_Rpba-cuAwAAAAAsGkuxiDsAAAAA_ZK9AgAAAKBrphACAAAA0LUxM89dBgAAAABYZAohAAAAAF0bixmEAAAAAHTMCCwAAAAAuiaBBQAAAEDXLOIOAAAAQNeMwAIAAACga2NErRmlha2tra2tra2tra2tra2tra2tbYfb_Dv_4_WIjLC1tbW1tbW1tbW1tbW1tbW1ta2x-3f-Z_-t3OPAgMAAACAReOQlsECAAAAoF_eQggAAABA18Y0AgsAAACAjsleAQAAANC1sbV27jIAAAAAwCIjsAAAAADo2lhrPXcZAAAAAGCRBBYAAAAAXRuHyHOXAQAAAAAWjZkSWAAAAAD0a6zTuYsAAAAAAMuMwAIAAACgaxJYAAAAAHTNWwgBAAAA6NqYpZ27DAAAAACwyBRCAAAAALpWzl0AAAAAADjECCwAAAAAuja2Zg0sAAAAAPolgQUAAABA1ySwAAAAAOiaRdwBAAAA6NpYihwWAAAAAP2SvQIAAACgaxJYAAAAAHRNAgsAAACArklgAQAAANC1sbV27jIAAAAAwCIjsAAAAADomgQWAAAAAF2TwAIAAACgaxJYAAAAAHRNAgsAAACArklgAQAAANA1CSwAAAAAuiaBBQAAAEDXxmxyWAAAAAD0S_YKAAAAgK6NrbVzlwEAAAAAFhmBBQAAAEDXJLAAAAAA6JoEFgAAAABdk8ACAAAAoGsSWAAAAAB0TQILAAAAgK5JYAEAAADQtbG1du4yAAAAAMAiI7AAAAAA6JoEFgAAAABdk8ACAAAAoGsSWAAAAAB0TQILAAAAgK5JYAEAAADQNQksAAAAALomgQUAAABA1ySwAAAAAOiaBBYAAAAAXRsz89xlAAAAAIBFRmABAAAA0DUJLAAAAAC6JoEFAAAAQNesgQUAAABA18bdbnfuMgAAAADAIlMIAQAAAOiaBBYAAAAAXZPAAgAAAKBrElgAAAAAdE0CCwAAAICuSWABAAAA0DUJLAAAAAC6NkbUc5cBAAAAABYZgQUAAABA1ySwAAAAAOiaBBYAAAAAXZPAAgAAAKBrElgAAAAAdE0CCwAAAICuSWABAAAA0LWxtXbuMgAAAADAIiOwAAAAAOiaBBYAAAAAXZPAAgAAAKBrY2aeuwwAAAAA";
        String sfencode = imagesEncodedList.get(0);

        sfencode = sfencode.replace("+","-");
        sfencode = sfencode.replace("/","_");
        sfencode = sfencode.replace("=",",");

        String imageTest = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/addImage?img_name=sfencodeShivalik&img_data=" + sfencode;
        //Log.v("enc : ", sfencode);
        Log.v("imageTest : ", imageTest);

        HttpConnection httpConnectionImage = new HttpConnection(this, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {

            }
        });
        httpConnectionImage.execute(imageTest);

    }

    public void test(String s) {
        Log.i(TAG, s);
    }
}
