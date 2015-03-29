package databaseLibrary;

/**
 * Created by kujichagulia on 3/2/15.
 */
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.content.Context;
public class UserFunctions {
    private JSONParser jsonParser;
    //URL of the PHP API
    private static String loginURL = "http://52.4.203.150/php_api/";
    private static String registerURL = "http://52.4.203.150/php_api/";
    private static String forpassURL = "http://52.4.203.150/php_api/";
    private static String chgpassURL = "http://52.4.203.150/php_api/";
    private static String addrideURL = "http://52.4.203.150/php_api/";
    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String forpass_tag = "forpass";
    private static String chgpass_tag = "chgpass";
    private static String addride_tag = "addride";
    private static String getrides_tag = "getrides";
    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }
    /**
     * Function to Login
     **/
    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.makeHttpRequest(loginURL,"POST", params);
        return json;
    }
    /**
     * Function to change password
     **/
    public JSONObject chgPass(String newpas, String email){
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", chgpass_tag));
        params.add(new BasicNameValuePair("newpas", newpas));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.makeHttpRequest(chgpassURL,"POST", params);
        return json;
    }
    /**
     * Function to reset the password
     **/
    public JSONObject forPass(String forgotpassword){
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", forpass_tag));
        params.add(new BasicNameValuePair("forgotpassword", forgotpassword));
        JSONObject json = jsonParser.makeHttpRequest(forpassURL,"POST", params);
        return json;
    }
    /**
     * Function to  Register
     **/
    public JSONObject registerUser( String email, String uname, String password){
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("uname", uname));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.makeHttpRequest(registerURL,"POST",params);
        return json;
    }
    //Function to add ride
    public JSONObject addRide( String username, String car, String seats,String desc, String avail) {
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", addride_tag));
        params.add(new BasicNameValuePair("uname", username));
        params.add(new BasicNameValuePair("car", car));
        params.add(new BasicNameValuePair("seats", seats));
        params.add(new BasicNameValuePair("desc", desc));
        params.add(new BasicNameValuePair("avail", avail));
        JSONObject json = jsonParser.makeHttpRequest(addrideURL,"POST", params);
        return json;
    }
    //function to get rides
    public JSONObject getRides(){
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", getrides_tag));
        JSONObject json = jsonParser.makeHttpRequest(loginURL,"POST", params);
        return json;
    }
        /**
         * Function to logout user
         * Resets the temporary data stored in SQLite Database
         * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }
}
