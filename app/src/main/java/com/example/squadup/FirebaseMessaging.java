package com.example.squadup;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
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
    public void onMessageReceived(RemoteMessage remoteMessage) {
        MainActivity.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        MainActivity.editor = MainActivity.sharedPreferences.edit();

        Log.e(TAG, remoteMessage.getData().size() + " is the size of the data");

        if (MainActivity.sharedPreferences.getBoolean("Ghost Mode", false) == false) { //if ghost mode is off, receive the notification.

            if (remoteMessage.getData().size() > 0) { //if message contains data
                eventID = remoteMessage.getData().get(0);
                MainActivity.editor.putString("Pending Event", eventID);

            }

            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            MyNotificationManager.getInstance(getApplicationContext()).displayNotification(title, body);

        }
    }
}