package com.nirwal.electricaltools;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

public class MyApp extends Application {
    public static final String TAG = "MyApp";
    public static final String NOTIFICATION_CHANNEL_1_ID="CHANNEL_1";
    public static final String NOTIFICATION_CHANNEL_2_ID="CHANNEL_2";
    public static final String NOTIFICATION_CHANNEL_3_ID="CHANNEL_3";

    @Override
    public void onCreate() {
        super.onCreate();

        onTokenRefresh();
    }

    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
    }


    void createNotificationChannel(){
         if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
             NotificationChannel channel1 = new NotificationChannel(
                     NOTIFICATION_CHANNEL_1_ID,
                     "Channel low",
                     NotificationManager.IMPORTANCE_LOW
             );

             NotificationChannel channel2 = new NotificationChannel(
                     NOTIFICATION_CHANNEL_2_ID,
                     "Channel default",
                     NotificationManager.IMPORTANCE_DEFAULT
             );

             NotificationChannel channel3 = new NotificationChannel(
                     NOTIFICATION_CHANNEL_3_ID,
                     "Channel high",
                     NotificationManager.IMPORTANCE_HIGH
             );

             channel1.setDescription("This is channel 1 (Low).");
             channel2.setDescription("This is channel 2 (Default).");
             channel3.setDescription("This is channel 3 (High).");

             NotificationManager notificationManager = getSystemService(NotificationManager.class);
             notificationManager.createNotificationChannel(channel1);
             notificationManager.createNotificationChannel(channel2);
             notificationManager.createNotificationChannel(channel3);


         }
    }

}
