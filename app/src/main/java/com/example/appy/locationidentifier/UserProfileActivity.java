package com.example.appy.locationidentifier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.appy.utility.AsyncResponse;
import com.example.appy.utility.HttpConnection;
import com.example.appy.utility.SessionManagement;

/**
 * Created by shivalik on 11/3/16.
 */

public class UserProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView fname = (TextView) findViewById(R.id.first_name);
        TextView lname = (TextView) findViewById(R.id.last_name);
        final EditText editPhone = (EditText) findViewById(R.id.edit_phone);
        final EditText editEmail = (EditText) findViewById(R.id.edit_email);

        Bundle bundle = getIntent().getExtras();

        fname.setText(bundle.getString("fname"));
        lname.setText(bundle.getString("lname"));
        editPhone.setText(bundle.getString("ph_number"));
        editEmail.setText(bundle.getString("email"));

        Button savePhEmailBtn = (Button) findViewById(R.id.submitPhEmail);
        savePhEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManagement session = new SessionManagement(getApplicationContext());
                String id = session.getLoggedInUserId();
                String s = "http://ec2-52-53-202-11.us-west-1.compute.amazonaws.com:8080/updateUser?"
                            + "phone_number=" +  editPhone.getText().toString()
                            + "&email=" + editEmail.getText().toString()
                            + "&user_id=" + id;
                Toast.makeText(getBaseContext(), "Saved!", Toast.LENGTH_SHORT ).show();
                HttpConnection httpConnection = new HttpConnection(UserProfileActivity.this, new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {

                    }
                });
                httpConnection.execute(s);
            }
        });
    }
}
