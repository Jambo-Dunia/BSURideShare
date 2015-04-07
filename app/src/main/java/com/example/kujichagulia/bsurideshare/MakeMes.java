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

import org.json.JSONException;
import org.json.JSONObject;

import databaseLibrary.UserFunctions;

/**
 * Created by kujichagulia on 4/6/15.
 */
public class MakeMes extends Activity{

    String toUser;
    private ProgressDialog pDialog;
    EditText inputMess;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_mess);

        Intent i = getIntent();

        // getting product id (pid) from intent
        toUser = i.getStringExtra("uname");

        // Edit Text
        inputMess= (EditText) findViewById(R.id.editText10);


        // Create button
        Button btnCreateMess = (Button) findViewById(R.id.button14);

        // button click event
        btnCreateMess.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewMessage().execute();
            }
        });
    }

    /**
     * Background Async Task to Create new product
     * */
    class CreateNewMessage extends AsyncTask<String, String, String> {

        /**Creating Ride
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MakeMes.this);
            pDialog.setMessage("Sending Message..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String message = inputMess.getText().toString();



            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.addMessage(toUser,Main.userName.toString(), inputMess.getText().toString());



            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt("success");

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
