package com.example.kujichagulia.bsurideshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by kujichagulia on 4/6/15.
 */
public class MessageDetail extends Activity {

    String midi;

    // Progress Dialog



    TextView username;
    TextView created;
    TextView desc;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mess_detail);
        username = (TextView) findViewById(R.id.textView23);
        desc = (TextView) findViewById(R.id.textView24);
        created = (TextView) findViewById(R.id.textView26);

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        midi = i.getStringExtra("mid");

        // Getting complete product details in background thread
        for(MessageObj mess : Messages.messData){
            if(mess.mid.equals(midi)){

                username.setText(mess.from);
                created.setText(mess.created);
                desc.setText(mess.discription);

            }

        }

        Button sendMessage = (Button) findViewById(R.id.button16);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        MakeMes.class);
                // sending pid to next activity
                in.putExtra("uname", username.getText());

                // starting new activit
                startActivity(in);
            }
        });


    }
}
