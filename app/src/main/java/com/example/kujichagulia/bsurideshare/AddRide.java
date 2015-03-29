package com.example.kujichagulia.bsurideshare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import databaseLibrary.JSONParser;
import databaseLibrary.UserFunctions;

/**
 * Created by kujichagulia on 3/28/15.
 */
public class AddRide extends Activity{

    private ProgressDialog pDialog;
    EditText inputCar;
    EditText inputSeats;
    EditText inputDesc;
    //JSONParser jsonParser = new JSONParser();

    // url to create new product

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ride);

        // Edit Text
        inputCar = (EditText) findViewById(R.id.editText7);
        inputSeats = (EditText) findViewById(R.id.editText8);
        inputDesc = (EditText) findViewById(R.id.editText9);

        // Create button
        Button btnCreateRide = (Button) findViewById(R.id.button12);

        // button click event
        btnCreateRide.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewRide().execute();
            }
        });
    }





    /**
     * Background Async Task to Create new product
     * */
    class CreateNewRide extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddRide.this);
            pDialog.setMessage("Creating Ride..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String car = inputCar.getText().toString();
            String seats = inputSeats.getText().toString();
            String description = inputDesc.getText().toString();


            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.addRide(Main.userName.toString(), car,seats,description,"1");



            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), Rides.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}
