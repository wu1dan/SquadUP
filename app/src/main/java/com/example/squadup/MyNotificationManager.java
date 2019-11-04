package com.example.squadup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.app.PendingIntent;
import android.app.NotificationManager;

import androidx.core.app.NotificationCompat;

public class MyNotificationManager {

    private Context myContext;
    @SuppressLint("StaticFieldLeak")
    private static MyNotificationManager myInstance;

    private MyNotificationManager(Context context){
        myContext = context;
    }

    public static synchronized MyNotificationManager getInstance(Context context){
        if(myInstance == null)
            myInstance = new MyNotificationManager(context);

        return myInstance;
    }

    public void displayNotification(String title, String body){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(myContext, ConstantsUtil.CHANNEL_ID)
                .setSmallIcon(R.drawable.squaduppenguin)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        //String defValue = "default";

        //if(!MainActivity.sharedPreferences.getString("Pending Event", defValue).equals(defValue)) {
            Intent intent = new Intent(myContext, PendingEvent.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(myContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        /*}else{ //if the notification didn't contain a payload, send them to the home page
            Intent intent = new Intent(myContext, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(myContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }*/

        /* the above declared event will only be fired off when the notification is clicked
         * FLAG_UPDATE_INTENT is used to keep any old pending intents but to replace extra data with a new intent.
         * request code of 0 makes the action happen on click.
         */

        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) myContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if(mNotificationManager != null)
            mNotificationManager.notify(1, mBuilder.build());
    }
}
