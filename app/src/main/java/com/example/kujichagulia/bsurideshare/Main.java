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

        /**
         * Called when the activity is first created.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);
            changepass = (Button) findViewById(R.id.button9);
            loginButton = (Button) findViewById(R.id.button8);
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            /**
             * Hashmap to load data from the Sqlite database
             **/
            HashMap user = new HashMap();
            user = db.getUserDetails();
            /**
             * Change Password Activity Started
             **/
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
/**
 * Sets user first name and last name in text view.
 **/
            final TextView name = (TextView) findViewById(R.id.textView8);
            name.setText("Welcome  " + user.get("uname"));

        }
    }

