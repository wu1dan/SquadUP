package com.example.squadup;

import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

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
    public static String eventID;

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

        String title = "default";
        String body = "default";

        Log.e(TAG, remoteMessage.getData().size() + " is the size of the data");

        if (!sharedPreferences.getBoolean("Ghost Mode", false)) { //if ghost mode is off, receive the notification.

            if (remoteMessage.getData().size() > 0) { //if message contains data
                eventID = remoteMessage.getData().get(0);
                sharedPreferencesEditor.putString("Pending Event", eventID);

                //set the title and body from the notification data here, and then it will use that in the notification below



            }

            else {
                title = remoteMessage.getNotification().getTitle();
                body = remoteMessage.getNotification().getBody();
            }

            MyNotificationManager.getInstance(getApplicationContext()).displayNotification(title, body);

        }
    }
}