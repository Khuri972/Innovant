package com.innovent.erp.music.broadcastRecievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.music.PlayMusicActivity;
import com.innovent.erp.music.custom.NotificationGenerator;
import com.innovent.erp.music.custom.Util;
import com.innovent.erp.music.service.backgroundMusicService;

/**
 * Created by CRAFT BOX on 4/25/2018.
 */

public class NotificationDismissedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getExtras().getInt("com.my.app.notificationId");
        Util.cancelNotification(context, NotificationGenerator.NOTIFICATION_ID);
        if (GlobalElements.isMyServiceRunning(backgroundMusicService.class, context)) {
            if (backgroundMusicService.player.isPlaying()) {
                context.stopService(PlayMusicActivity.intent);
            }
        }
      /* Your code to handle the event here */
    }
}
