package com.innovent.erp.firabse;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.NotificationActivity;
import com.innovent.erp.R;
import com.innovent.erp.netUtils.MyPreferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

/**
 * Created by CRAFT BOX on 2/24/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    MyPreferences myPreferences;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        try {
            myPreferences = new MyPreferences(MyFirebaseMessagingService.this);
            if (!myPreferences.getPreferences(MyPreferences.id).equals("")) {
                String id = myPreferences.getPreferences(MyPreferences.id);
                String uid = remoteMessage.getData().get("user_id");
                System.out.print("" + id + "" + uid);
                if (myPreferences.getPreferences(MyPreferences.id).equals("" + remoteMessage.getData().get("user_id")) || remoteMessage.getData().get("user_id").equals("0")) {
                    String notification_title = "";
                    notification_title = remoteMessage.getData().get("notification_title");
                    String notification_description = remoteMessage.getData().get("notification_description");

                    Random r = new Random();
                    int notificationId = r.nextInt(80 - 65) + 65;
                    Intent viewIntent = new Intent(MyFirebaseMessagingService.this, NotificationActivity.class);
                    viewIntent.putExtra("type", "1");
                    PendingIntent viewPendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, notificationId, viewIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Notification notif = new Notification.Builder(MyFirebaseMessagingService.this)
                            .setContentTitle(GlobalElements.fromHtml(notification_title))
                            .setContentText("" + GlobalElements.fromHtml(notification_description))
                            .setContentIntent(viewPendingIntent)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(largeIcon)
                            .setSound(defaultSoundUri)
                            .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                            .setAutoCancel(true)
                            .build();
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyFirebaseMessagingService.this);
                    notificationManager.notify(notificationId, notif);
                } else {
                    Log.e(">>>", "ok");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
