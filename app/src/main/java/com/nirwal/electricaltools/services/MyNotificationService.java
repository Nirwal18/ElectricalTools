package com.nirwal.electricaltools.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nirwal.electricaltools.MainActivity;
import com.nirwal.electricaltools.MyApp;
import com.nirwal.electricaltools.R;

import java.util.Objects;

public class MyNotificationService extends FirebaseMessagingService {
    private static final String TAG = "MyNotificationService";
    public static final String FCM_TOKEN="FCM_TOKEN";


    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        getSharedPreferences(MyApp.TAG,MODE_MULTI_PROCESS)
                .edit()
                .putString(FCM_TOKEN,token)
                .apply();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived: "+remoteMessage.getFrom());
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
           createNotification(remoteMessage);
        }

    }

    public void createNotification(RemoteMessage remoteMessage){
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.putExtra(MainActivity.arg1, 1);
// Set the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
// Create the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );


        Notification notification = new NotificationCompat.Builder(this, MyApp.NOTIFICATION_CHANNEL_1_ID)
                .setSmallIcon(R.drawable.color_bulb)
                .setContentTitle(Objects.requireNonNull(remoteMessage.getNotification()).getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(notifyPendingIntent)
                .build();
        NotificationManagerCompat.from(this).notify(111,notification);
    }
}
