package com.innovent.erp.schedualReminder.schedualservice;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;

import com.innovent.erp.DashboardActivity;
import com.innovent.erp.R;

import java.util.Random;

public class MyReceiver extends BroadcastReceiver {

    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = null;
        String desc = null;
        if (intent.getExtras() != null) {
            title = intent.getStringExtra("title");
            desc = intent.getStringExtra("desc");

            Random r = new Random();
            int notificationId = r.nextInt(80 - 65) + 65;
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle(title);
            builder.setContentText(desc);
            builder.setSmallIcon(R.drawable.ic_holyday);
            Intent notifyIntent = new Intent(context, DashboardActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            //to be able to launch your activity from the notification
            builder.setContentIntent(pendingIntent);
            Notification notificationCompat = builder.build();
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
            managerCompat.notify(notificationId, notificationCompat);
        }
    }
}