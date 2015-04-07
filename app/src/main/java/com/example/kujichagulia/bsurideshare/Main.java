package com.example.kujichagulia.bsurideshare;

/**
 * Created by kujichagulia on 3/2/15.
 */

    import android.app.Activity;
    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.TextView;


    import java.util.HashMap;

    import databaseLibrary.DatabaseHandler;
    import databaseLibrary.UserFunctions;

public class Main extends Activity {
        Button loginButton;
        Button changepass;
        Button rides;
        Button messages;
        public static String userName;
        /**
         * Called when the activity is first created.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);
            changepass = (Button) findViewById(R.id.button9);
            loginButton = (Button) findViewById(R.id.button8);
            rides = (Button) findViewById(R.id.button10);
            messages = (Button) findViewById(R.id.button15);


            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            /**
             * Hashmap to load data from the Sqlite database
             **/
            HashMap user = new HashMap();
            user = db.getUserDetails();
            /**Intent change = new Intent(getApplicationContext(), Rides.class);
                    startActivity(change);
             * Change Password Activity Started
             **/
            userName = user.get("uname").toString();
            changepass.setOnClickListener(new View.OnClickListener(){
                public void onClick(View arg0){
                    Intent change = new Intent(getApplicationContext(), PassChange.class);
                    startActivity(change);
                }
            });
            /**
             *Logout from the User Panel which clears the data in Sqlite database
             **/
            loginButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    UserFunctions logout = new UserFunctions();
                    logout.logoutUser(getApplicationContext());
                    Intent login = new Intent(getApplicationContext(), Login.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                    finish();
                }
            });

            messages.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent change = new Intent(getApplicationContext(), Messages.class);
                    startActivity(change);
                }
            });

            // Create button
            Button addride = (Button) findViewById(R.id.button11);

            // button click event
            addride.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    // creating new product in background thread
                    Intent change = new Intent(getApplicationContext(), AddRide.class);
                    startActivity(change);
                }
            });

            rides.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent change = new Intent(getApplicationContext(), Rides.class);
                    startActivity(change);
                }
            });
/**
 * Sets user first name and last name in text view.
 **/
            final TextView name = (TextView) findViewById(R.id.textView8);
            name.setText("Welcome  " + userName);

        }
    }

