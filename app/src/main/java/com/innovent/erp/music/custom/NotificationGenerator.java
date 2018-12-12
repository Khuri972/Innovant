package com.innovent.erp.music.custom;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.innovent.erp.music.PlayMusicActivity;
import com.innovent.erp.music.broadcastRecievers.NotificationDismissedReceiver;
import com.innovent.erp.music.fragments.MusicDetailFragment;
import com.innovent.erp.music.service.backgroundMusicService;
import com.innovent.erp.R;

public class NotificationGenerator {

    public static final String NOTIFY_PREVIOUS = "com.cdms.craftbox.music.previous";
    public static final String NOTIFY_DELETE = "com.cdms.craftbox.music.delete";
    public static final String NOTIFY_PAUSE = "com.cdms.craftbox.music.pause";
    public static final String NOTIFY_PLAY = "com.cdms.craftbox.music.play";
    public static final String NOTIFY_NEXT = "com.cdms.craftbox.music.next";
    public static final String NOTIFY_MAIN = "com.cdms.craftbox.music.main";
    public static final int NOTIFICATION_ID = 0;
    Notification status;

    private final String LOG_TAG = "NotificationService";

    @SuppressLint("RestrictedApi")
    public static void customNotification(Context context, String SongName) {
        //set remote views
        try {
            NotificationTarget notificationTarget;
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.activity_notification);
            remoteViews.setTextViewText(R.id.tv_song_name, SongName);
            if (MusicDetailFragment.mode == MusicDetailFragment.FRAGMENT_IN_PLAY_MODE) {
                remoteViews.setViewVisibility(R.id.img_song_play, View.GONE);
                remoteViews.setViewVisibility(R.id.img_song_pause, View.VISIBLE);
            } else if (MusicDetailFragment.mode == MusicDetailFragment.FRAGMENT_IN_PAUSE_MODE) {
                remoteViews.setViewVisibility(R.id.img_song_play, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.img_song_pause, View.GONE);
            }

            NotificationCompat.Builder nb;
            nb = new NotificationCompat.Builder(context);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //the intent that is started when the notification is clicked (works)
            Intent notificationIntent = new Intent(context, PlayMusicActivity.class);
            notificationIntent.putExtra(AppConstant.FROM_WHERE, AppConstant.FROM_NOTIFICATION);
            notificationIntent.putExtra(AppConstant.MUSIC, backgroundMusicService.MUSIC_LIST_CURRENT_POSITION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);


            nb.setContentIntent(pendingIntent);
            nb.setSmallIcon(R.drawable.ic_music_note);
            nb.setAutoCancel(true);
            nb.setCustomBigContentView(remoteViews);
            nb.setContentTitle("MusicApp");
            nb.setContentText(SongName);
            nb.getBigContentView().setTextViewText(R.id.tv_song_name, SongName);
            nb.setDeleteIntent(createOnDismissedIntent(context, NOTIFICATION_ID));
            setListners(remoteViews, context);
            Notification notification = nb.build();
            notificationTarget = new NotificationTarget(
                    context,
                    remoteViews,
                    R.id.img_song,
                    notification,
                    NOTIFICATION_ID);
            Glide   .with(context.getApplicationContext())
                    .load(backgroundMusicService.musicArrayList.get(backgroundMusicService.MUSIC_LIST_CURRENT_POSITION).getImageUrl())
                    .asBitmap()
                    .into(notificationTarget);
            notificationManager.notify(NOTIFICATION_ID, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setListners(RemoteViews remoteViews, Context context) {
        Intent previous = new Intent(NOTIFY_PREVIOUS);
        Intent delete = new Intent(NOTIFY_DELETE);
        Intent pause = new Intent(NOTIFY_PAUSE);
        Intent play = new Intent(NOTIFY_PLAY);
        Intent next = new Intent(NOTIFY_NEXT);

        PendingIntent pendingIntentPrevious = PendingIntent.getBroadcast(context, 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.img_song_previous, pendingIntentPrevious);
        PendingIntent pendingIntentdelete = PendingIntent.getBroadcast(context, 0, delete, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.img_song_delete, pendingIntentdelete);
        PendingIntent pendingIntentpause = PendingIntent.getBroadcast(context, 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.img_song_pause, pendingIntentpause);
        PendingIntent pendingIntentplay = PendingIntent.getBroadcast(context, 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.img_song_play, pendingIntentplay);
        PendingIntent pendingIntentnext = PendingIntent.getBroadcast(context, 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.img_song_next, pendingIntentnext);
    }

    public static PendingIntent createOnDismissedIntent(Context context, int notificationId) {
        Intent intent = new Intent(context, NotificationDismissedReceiver.class);
        intent.putExtra("com.my.app.notificationId", notificationId);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context.getApplicationContext(),
                        notificationId, intent, 0);
        return pendingIntent;
    }

}
