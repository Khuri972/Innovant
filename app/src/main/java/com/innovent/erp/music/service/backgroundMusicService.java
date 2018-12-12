package com.innovent.erp.music.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;


import com.innovent.erp.music.fragments.MusicDetailFragment;
import com.innovent.erp.music.model.MainCategory;
import com.innovent.erp.music.model.Music;
import com.innovent.erp.music.model.SubCategory;

import java.io.IOException;
import java.util.ArrayList;

public class backgroundMusicService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {

    public static ArrayList<MainCategory> mainCategories;
    public static ArrayList<SubCategory> subCategories;
    public static ArrayList<Music> musicArrayList;

    public static int MUSIC_LIST_SIZE = 0;
    public static int MUSIC_LIST_CURRENT_POSITION = 0;
    public static int MAIN_CATEGORY_LIST_POSITION = 0;
    public static int SUB_CATEGORY_LIST_POSITION = 0;
    public static int MUSIC_PROGRESS = 0;


    public static MediaPlayer player;

    // ---Variables for seekbar processing---
    private final Handler handler = new Handler();
    int mediaPosition;
    int mediaMax;
    public static final String BROADCAST_ACTION = "com.glowingpigs.tutorialstreamaudiopart1b.seekprogress";
    Intent seekIntent;
    private static int songEnded;


    public interface changeSongIntercommunitor {
        public void changeSong();
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    //on create
    @Override
    public void onCreate() {
        super.onCreate();
// ---Set up intent for seekbar broadcast ---
        seekIntent = new Intent(BROADCAST_ACTION);
        Log.d("service", "onCreate");
        if (player != null) {
            player.release();
        }
        player = new MediaPlayer();
    }

    public void playSong(int songIndex) {
        Music music = backgroundMusicService.musicArrayList.get(songIndex);
        try {
            if (player != null) {
                Log.d("play song", "player not null" + music.getDesc());
                player.setDataSource(music.getMusicUrl());
                player.prepare();

            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
// ---Set up receiver for seekbar change ---
        registerReceiver(broadcastReceiver, new IntentFilter(
                MusicDetailFragment.BROADCAST_SEEKBAR));
//        AppConstant.MUSIC_LIST_CURRENT_POSITION = initent.getIntExtra(AppConstant.CURRENT_POSITION, 0);
        Log.d("service", "onStartCommand" + backgroundMusicService.MUSIC_LIST_CURRENT_POSITION);
        Log.d("service", "onStartCommand");
        try {
            playSong(backgroundMusicService.MUSIC_LIST_CURRENT_POSITION);
            player.start();
            player.setOnCompletionListener(this);
            player.setOnBufferingUpdateListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setupHandler();
        return START_STICKY;
    }

    private void setupHandler() {
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            // // Log.d(TAG, "entered sendUpdatesToUI");
            LogMediaPosition();
            handler.postDelayed(this, 1000); // 2 seconds
        }
    };

    private void LogMediaPosition() {
        // // Log.d(TAG, "entered LogMediaPosition");
        if (player.isPlaying()) {
            mediaPosition = player.getCurrentPosition();
            mediaMax = player.getDuration();
            seekIntent.putExtra("counter", String.valueOf(mediaPosition));
            seekIntent.putExtra("mediamax", String.valueOf(mediaMax));
            seekIntent.putExtra("song_ended", String.valueOf(songEnded));
            sendBroadcast(seekIntent);
        }
    }

    // --Receive seekbar position if it has been changed by the user in the
    // activity
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateSeekPos(intent);
        }
    };

    // Update seek position from Activity
    public void updateSeekPos(Intent intent) {
        int seekPos = intent.getIntExtra("seekpos", 0);
        if (player.isPlaying()) {
            handler.removeCallbacks(sendUpdatesToUI);
            player.seekTo(seekPos);
            setupHandler();
        }
    }

    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    @Override
    public void onDestroy() {
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
        }
        // Unregister seekbar receiver
        unregisterReceiver(broadcastReceiver);
        // Stop the seekbar handler from sending updates to UI
        handler.removeCallbacks(sendUpdatesToUI);
    }

    @Override
    public void onLowMemory() {

    }

    public static void startMusic() {
        player.start();
        MusicDetailFragment.mode = MusicDetailFragment.FRAGMENT_IN_PLAY_MODE;
    }

    public static void pauseMusic() {
        player.pause();
        MusicDetailFragment.mode = MusicDetailFragment.FRAGMENT_IN_PAUSE_MODE;
    }

    public static void stopMusic() {

    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            if (backgroundMusicService.MUSIC_LIST_CURRENT_POSITION + 1 != backgroundMusicService.musicArrayList.size()) {
                Intent intent = new Intent();
                intent.putExtra("type", "1");
                intent.setAction("com.cdms.craftbox.music.service");
                sendOrderedBroadcast(intent, null);
            } else {
                Intent intent = new Intent();
                intent.putExtra("type", "0");
                intent.setAction("com.cdms.craftbox.music.service");
                sendOrderedBroadcast(intent, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }
}