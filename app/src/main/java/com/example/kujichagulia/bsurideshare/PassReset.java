package com.example.kujichagulia.bsurideshare;

/**
 * Created by kujichagulia on 3/2/15.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//import com.learn2crack.library.UserFunctions;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import databaseLibrary.UserFunctions;

public class PassReset extends Activity {
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    EditText emailInput;
    TextView message;
    Button resetpass;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passreset);
        Button loginButton = (Button) findViewById(R.id.button6);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Login.class);
                startActivityForResult(myIntent, 0);
                finish();
            }
        });
        emailInput = (EditText) findViewById(R.id.editText6);
        message = (TextView) findViewById(R.id.textView9);
        resetpass = (Button) findViewById(R.id.button5);
        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetAsync(view);
            }
        });}
    private class NetworkCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(PassReset.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }
        @Override
        protected Boolean doInBackground(String... args){
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean th){
            if(th == true){
                nDialog.dismiss();
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                message.setText("Error in Network Connection");
            }
        }
    }

    private class ProcessRegister extends AsyncTask<String,String,JSONObject> {
        private ProgressDialog pDialog;
        String forgotpassword;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            forgotpassword = emailInput.getText().toString();
            pDialog = new ProgressDialog(PassReset.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.forPass(forgotpassword);
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            /**
             * Checks if the Password Change Process is sucesss
             **/
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    message.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    String red = json.getString(KEY_ERROR);
                    if(Integer.parseInt(res) == 1){
                        pDialog.dismiss();
                        message.setText("A recovery email is sent to you, see it for more details.");
                    }
                    else if (Integer.parseInt(red) == 2)
                    {    pDialog.dismiss();
                        message.setText("Your email does not exist in our database.");
                    }
                    else {
                        pDialog.dismiss();
                        message.setText("Error occured in changing Password");
                    }
                }}
            catch (JSONException e) {
                e.printStackTrace();
            }
        }}
    public void NetAsync(View view){
        new NetworkCheck().execute();
    }}