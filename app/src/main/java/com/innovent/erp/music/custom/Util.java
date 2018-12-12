package com.innovent.erp.music.custom;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.ContentFrameLayout;
import android.util.Log;
import android.view.View;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.music.fragments.MusicDetailFragment;
import com.innovent.erp.music.service.backgroundMusicService;
import com.innovent.erp.R;

public class Util {
    public static Fragment UpdatePlayerLayout(Context context, ContentFrameLayout layoutPlayMusic, FragmentManager fm, Fragment newFragment) {
        Log.d("Play music activity", "UpdatePlayerLayout");
        if (GlobalElements.isMyServiceRunning(backgroundMusicService.class, context)) {
            if (layoutPlayMusic.getVisibility() != View.VISIBLE) {
                layoutPlayMusic.setVisibility(View.VISIBLE);
                newFragment = new MusicDetailFragment();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.layout_play_music, newFragment).commit();
            } else {
                newFragment.onResume();
            }
        } else {
            layoutPlayMusic.setVisibility(View.GONE);
        }
        return newFragment;
    }

    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }

    public static String getCurrentSongName() {
        return backgroundMusicService.musicArrayList.get(backgroundMusicService.MUSIC_LIST_CURRENT_POSITION).getName();
    }
}
