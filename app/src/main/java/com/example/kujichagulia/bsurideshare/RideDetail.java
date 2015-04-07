package com.example.kujichagulia.bsurideshare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import databaseLibrary.UserFunctions;

/**
 * Created by kujichagulia on 3/29/15.
 */
public class RideDetail extends Activity {


    String pidi;

    // Progress Dialog

    // JSON Node names
    private static final String TAG_PID = "pid";


    TextView username;
    TextView car;
    TextView seats;
    TextView desc;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_details);
        username = (TextView) findViewById(R.id.textView14);
        car = (TextView) findViewById(R.id.textView13);
        seats = (TextView) findViewById(R.id.textView15);
        desc = (TextView) findViewById(R.id.textView16);

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        pidi = i.getStringExtra(TAG_PID);

        // Getting complete product details in background thread
        for(RideObject ride : Rides.rideData){
           if(ride.pid.equals(pidi)){

               username.setText(ride.user);
               car.setText(ride.carname);
               seats.setText(ride.seats);
               desc.setText(ride.discription);

           }

        }

        Button sendMessage = (Button) findViewById(R.id.button13);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        MakeMes.class);
                // sending pid to next activity
                in.putExtra("uname", username.getText());

                // starting new activity and expecting some response back
                startActivity(in);
            }
        });


    }








}
