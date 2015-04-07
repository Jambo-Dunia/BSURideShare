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

import databaseLibrary.UserFunctions;

/**
 * Created by kujichagulia on 4/6/15.
 */
public class Messages extends ListActivity {

    ArrayList<HashMap<String, String>> messageList;
    static LinkedList<MessageObj> messData = new LinkedList<MessageObj>();
    private ProgressDialog pDialog;

    JSONArray messages = null;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);


        // Hashmap for ListView
        messageList = new ArrayList<HashMap<String, String>>();

        // Loading products in Background Thread
        new LoadAllMess().execute();

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
                       MessageDetail.class);

                in.putExtra("mid", pid);


               startActivity(in);
            }
        });
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllMess extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Messages.this);
            pDialog.setMessage("Loading Messages. Please wait...");
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
            JSONObject json = userFunction.getMessages(Main.userName);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());
            //Log.d("wuts in here:", );

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt("success");

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    messages = json.getJSONArray("messages");

                    // looping through All Products
                    for (int i = 0; i < messages.length(); i++) {
                        JSONObject c = messages.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString("mid");
                        String name = c.getString("username");
                        String from = c.getString("fr");
                        String info = c.getString("message");
                        String ca = c.getString("created_at");


                       MessageObj m = new MessageObj(id,name,from,info,ca);
                        messData.add(m);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put("mid", id);
                        map.put("fr", from);

                        // adding HashList to ArrayList
                        messageList.add(map);
                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(),
                            Main.class);
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
                            Messages.this, messageList,
                            R.layout.list_item2, new String[]{"mid",
                            "fr"},
                            new int[]{R.id.pid, R.id.name});
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }
    }


}
