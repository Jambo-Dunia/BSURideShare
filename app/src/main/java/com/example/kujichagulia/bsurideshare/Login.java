package com.example.kujichagulia.bsurideshare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import databaseLibrary.DatabaseHandler;
import databaseLibrary.UserFunctions;


public class Login extends Activity {
    Button loginButton;
    Button registerButton;
    Button passResetButton;
    EditText emailInput;
    EditText passwordInput;
    private TextView errorMessage;
    /**
     * Called when the activity is first created.
     */
    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_USERNAME = "uname";

    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailInput = (EditText) findViewById(R.id.editText);
        passwordInput = (EditText) findViewById(R.id.editText2);
        loginButton = (Button) findViewById(R.id.button);
        registerButton = (Button) findViewById(R.id.button2);
        passResetButton = (Button) findViewById(R.id.button3);
        errorMessage = (TextView) findViewById(R.id.textView6);

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Register.class);
                startActivityForResult(myIntent, 0);
                finish();
            }
        });
        passResetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), PassReset.class);
                startActivityForResult(myIntent, 0);
                finish();
            }
        });
/**
 * Login button click event
 * A Toast is set to alert when the Email and Password field is empty
 **/
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if ((!emailInput.getText().toString().equals("")) && (!passwordInput.getText().toString().equals(""))) {
                    NetAsync(view);
                } else if ((!emailInput.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Password field empty", Toast.LENGTH_SHORT).show();
                } else if ((!passwordInput.getText().toString().equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Email field empty", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Email and Password field are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Async Task to check whether internet connection is working.
     */
    class NetworkCheck extends AsyncTask<String,String,Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(Login.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }



        @Override
        protected Boolean doInBackground(String... args){

            /**
             * Gets current device state and checks for working internet connection by trying Google.
             */
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected())

            {
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
        protected void onPostExecute(Boolean th) {
            if (th == true) {
                nDialog.dismiss();
                new ProcessLogin().execute();
            } else {
                nDialog.dismiss();
                errorMessage.setText("Error in Network Connection");
            }
        }
    }
    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     */
    private class ProcessLogin extends AsyncTask<String,String,JSONObject> {
        private ProgressDialog pDialog;
        String email, password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            emailInput = (EditText) findViewById(R.id.editText);
            passwordInput = (EditText) findViewById(R.id.editText2);
            email = emailInput.getText().toString();
            password = passwordInput.getText().toString();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.loginUser(email, password);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    String res = json.getString(KEY_SUCCESS);
                    if (Integer.parseInt(res) == 1) {
                        pDialog.setMessage("Loading User Space");
                        pDialog.setTitle("Getting Data");
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");
                        /**
                         * Clear all previous data in SQlite database.
                         **/
                        UserFunctions logout = new UserFunctions();
                        logout.logoutUser(getApplicationContext());
                        db.addUser(json_user.getString(KEY_EMAIL), json_user.getString(KEY_USERNAME), json_user.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));
                        /**
                         *If JSON array details are stored in SQlite it launches the User Panel.
                         **/
                        Intent upanel = new Intent(getApplicationContext(), Main.class);
                        upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(upanel);
                        /**
                         * Close Login Screen
                         **/
                        finish();
                    } else {
                        pDialog.dismiss();
                        errorMessage.setText("Incorrect username/password");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    public void NetAsync(View view){
        NetworkCheck task = new NetworkCheck();
        task.execute();
    }

}