package com.example.squadup;

import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

import android.util.Log;
//import android.widget.TextView;
//import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
//import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import java.util.Map;

/**
 * NOTE: There can only be one service in each app that receives FCM messages. If multiple
 * are declared in the Manifest then the first one will be chosen.
 *
 * In order to make this Java sample functional, you must remove the following from the Kotlin messaging
 * service in the AndroidManifest.xml:
 *
 * <intent-filter>
 *   <action android:name="com.google.firebase.MESSAGING_EVENT" />
 * </intent-filter>
 */
public class FirebaseMessaging extends FirebaseMessagingService {


    private static final String TAG = "FirebaseMessaging";
    public static String eventID = "";
    public static String notificationTitle = "";
    private String title = "default";
    private String body = "default";
    public static String url = "";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        SharedPreferences sharedPreferences;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor sharedPreferencesEditor;
        sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!sharedPreferences.getBoolean("Ghost Mode", false)) { //if ghost mode is off, receive the notification.

            if (remoteMessage.getData().size() > 0) { //if message contains data

                String tempData = remoteMessage.getData().toString();
                int dataLength = remoteMessage.getData().toString().length();
                eventID = tempData.substring(0,dataLength - 1).substring(4);

                Log.e(TAG, remoteMessage.getData().size() + " is the size of the data");
                Log.e(TAG, "Message Data: " + remoteMessage.getData().toString());
                Log.e(TAG, "eventID: " + eventID);

                sharedPreferencesEditor.putString("tempID", eventID);
                sharedPreferencesEditor.commit();

                //set the title and body from the notification data here, and then it will use that in the notification below

                getJSON();
                body = "Click here for more details.";

            }

            else {
                title = remoteMessage.getNotification().getTitle();
                body = remoteMessage.getNotification().getBody();

                notificationTitle = title;
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification(title, body);


            }

        }
    }

    //retrieve event data
    public void getJSON() {
        RequestQueue requestQueue = Volley.newRequestQueue(FirebaseMessaging.this);
        String databaseURL = "http://20.43.19.13:3000/Events/";
        StringBuilder sb = new StringBuilder(databaseURL);
        url = sb.append(eventID).toString(); //adds the event ID, which was received from the notification, to the end of the URL

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        // Process the JSON

                        try {
                            JSONObject eventObj = response.getJSONObject(0);
                            title = eventObj.getString("EventName");
                            body = "Click here for more details.";
                            notificationTitle = title;

                            MyNotificationManager.getInstance(getApplicationContext()).displayNotification(title, body);


                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Log.d("VolleyError", error.toString());
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);

    }
}