package com.innovent.erp.music.broadcastRecievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.music.PlayMusicActivity;
import com.innovent.erp.music.custom.NotificationGenerator;
import com.innovent.erp.music.custom.Util;
import com.innovent.erp.music.fragments.MusicDetailFragment;
import com.innovent.erp.music.service.backgroundMusicService;

public class NotificationBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!GlobalElements.isActivityVisible()) {
            if (GlobalElements.isMyServiceRunning(backgroundMusicService.class, context)) {
                switch (intent.getAction()) {
                    case NotificationGenerator.NOTIFY_PLAY:
                        if (!backgroundMusicService.player.isPlaying()) {
                            backgroundMusicService.startMusic();
                            NotificationGenerator.customNotification(context, backgroundMusicService.musicArrayList.get
                                    (backgroundMusicService.MUSIC_LIST_CURRENT_POSITION).getName());
                        }
                        break;
                    case NotificationGenerator.NOTIFY_PAUSE:
                        if (backgroundMusicService.player.isPlaying()) {
                            backgroundMusicService.pauseMusic();
                            NotificationGenerator.customNotification(context, backgroundMusicService.musicArrayList.get
                                    (backgroundMusicService.MUSIC_LIST_CURRENT_POSITION).getName());
                        }
                        break;

                    case NotificationGenerator.NOTIFY_PREVIOUS:
                        if (backgroundMusicService.MUSIC_LIST_CURRENT_POSITION < backgroundMusicService.MUSIC_LIST_SIZE - 1
                                && backgroundMusicService.MUSIC_LIST_CURRENT_POSITION > 0) {
                            backgroundMusicService.MUSIC_LIST_CURRENT_POSITION = backgroundMusicService.MUSIC_LIST_CURRENT_POSITION - 1;
                            if (GlobalElements.isMyServiceRunning(backgroundMusicService.class, context)) {
                                context.stopService(PlayMusicActivity.intent);
                            }
                            PlayMusicActivity.intent = new Intent(context, backgroundMusicService.class);
                            context.startService(PlayMusicActivity.intent);
                            MusicDetailFragment.mode = MusicDetailFragment.FRAGMENT_IN_PLAY_MODE;
                            NotificationGenerator.customNotification(context, Util.getCurrentSongName());
                        } else {
                            Toast.makeText(context, "List has First Song", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case NotificationGenerator.NOTIFY_NEXT:
                        Toast.makeText(context, "next", Toast.LENGTH_LONG).show();
                        if (backgroundMusicService.MUSIC_LIST_CURRENT_POSITION < backgroundMusicService.MUSIC_LIST_SIZE - 1) {
                            Log.i("Notification", "" + backgroundMusicService.MUSIC_LIST_CURRENT_POSITION);
                            backgroundMusicService.MUSIC_LIST_CURRENT_POSITION = backgroundMusicService.MUSIC_LIST_CURRENT_POSITION + 1;
                            Log.i("Notification1", "" + backgroundMusicService.MUSIC_LIST_CURRENT_POSITION);
                            if (GlobalElements.isMyServiceRunning(backgroundMusicService.class, context)) {
                                context.stopService(PlayMusicActivity.intent);
                            }
                            Log.i("Notification2", "" + backgroundMusicService.MUSIC_LIST_CURRENT_POSITION);

                            PlayMusicActivity.intent = new Intent(context, backgroundMusicService.class);
                            context.startService(PlayMusicActivity.intent);
                            MusicDetailFragment.mode = MusicDetailFragment.FRAGMENT_IN_PLAY_MODE;
                            Log.i("Notification3", "" + backgroundMusicService.MUSIC_LIST_CURRENT_POSITION);
                            NotificationGenerator.customNotification(context, Util.getCurrentSongName());
                            Log.i("Notification4", "" + backgroundMusicService.MUSIC_LIST_CURRENT_POSITION);
                        } else {
                            Toast.makeText(context, "List has Last Song", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case NotificationGenerator.NOTIFY_DELETE:
                        Util.cancelNotification(context, NotificationGenerator.NOTIFICATION_ID);
                        if (GlobalElements.isMyServiceRunning(backgroundMusicService.class, context)) {
                            if (backgroundMusicService.player.isPlaying()) {
                                context.stopService(PlayMusicActivity.intent);
                            }
                        }
                        break;
                    default:
                        break;
                }
            } else {
                Util.cancelNotification(context, NotificationGenerator.NOTIFICATION_ID);
            }
        }
    }
}
