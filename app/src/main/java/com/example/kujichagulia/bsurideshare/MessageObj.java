package com.example.kujichagulia.bsurideshare;

/**
 * Created by kujichagulia on 4/6/15.
 */
public class MessageObj {

    String from;
    String user;
    String discription;
    String mid;
    String created;


    public MessageObj(String mi,String uname,String f, String info,String c){

        user = uname;
        from = f;
        discription = info;
        mid = mi;
        created = c;

    }
}
