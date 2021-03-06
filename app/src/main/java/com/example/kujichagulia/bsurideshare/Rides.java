package com.example.kujichagulia.bsurideshare;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import databaseLibrary.JSONParser;
import databaseLibrary.UserFunctions;

/**
 * Created by kujichagulia on 3/28/15.
 */
public class Rides extends ListActivity {

    ArrayList<HashMap<String, String>> rideList;

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONArray rides = null;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_RIDES = "rides";
    private static final String TAG_PID = "pid";
    private static final String TAG_UNAME = "username";
    private static final String TAG_CAR = "car";
    private static final String TAG_SEATS = "seats";
    private static final String TAG_DESC = "description";
    //private static final String TAG_AV = "avail";

    static LinkedList<RideObject> rideData = new LinkedList<RideObject>();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rides);


        // Hashmap for ListView
        rideList = new ArrayList<HashMap<String, String>>();

        // Loading products in Background Thread
        new LoadAllRides().execute();

        ListView lv = getListView();

        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        RideDetail.class);
                // sending pid to next activity
                in.putExtra(TAG_PID, pid);

                // starting new activity and expecting some response back
                startActivity(in);
            }
        });
    }


    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllRides extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Rides.this);
            pDialog.setMessage("Loading Rides. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.getRides();

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());
            //Log.d("wuts in here:", );

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    rides = json.getJSONArray(TAG_RIDES);

                    // looping through All Products
                    for (int i = 0; i < rides.length(); i++) {
                        JSONObject c = rides.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_CAR);
                        String seats = c.getString(TAG_SEATS);
                        String info = c.getString(TAG_DESC);
                        String u = c.getString(TAG_UNAME);

                        RideObject r = new RideObject(name,seats,u,info,id);
                        rideData.add(r);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_CAR, name);

                        // adding HashList to ArrayList
                        rideList.add(map);
                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(),
                            Rides.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            Rides.this, rideList,
                            R.layout.list_item, new String[]{TAG_PID,
                            TAG_CAR},
                            new int[]{R.id.pid, R.id.name});
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }
    }



}
