package com.innovent.erp.music.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.innovent.erp.GlobalElements;
import com.innovent.erp.music.Logger;
import com.innovent.erp.music.PlayMusicActivity;
import com.innovent.erp.music.custom.NotificationGenerator;
import com.innovent.erp.music.custom.Util;
import com.innovent.erp.music.service.backgroundMusicService;
import com.innovent.erp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicDetailFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private int seekMax;
    private static int songEnded = 0;
    boolean mBroadcastIsRegistered;
    Intent intent;
    BroadcastReceiver receiver;

    public interface OnMusicServiceUpdated {
        public void updateView();

        public void OnStopMusic();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMusicServiceUpdated) {
//            OnMusicServiceUpdated = (MusicDetailFragment.OnMusicServiceUpdated) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public static int FRAGMENT_IN_PLAY_MODE = 1, FRAGMENT_IN_PAUSE_MODE = 0, FRAGMENT_STOPED = 2;
    public static int mode = 1;
    Context context;
    public static AppCompatSeekBar seekbar;
    @BindView(R.id.img_previous)
    ImageView imgPrevious;
    @BindView(R.id.img_play)
    ImageView imgPlay;
    @BindView(R.id.img_pause)
    ImageView imgPause;
    @BindView(R.id.img_next)
    ImageView imgNext;
    @BindView(R.id.songName)
    TextView songName;

    Unbinder unbinder;

    // --Set up constant ID for broadcast of seekbar position--
    public static final String BROADCAST_SEEKBAR = "com.music.demo.musicapp.sendseekbar";

    public MusicDetailFragment() {
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent serviceIntent) {
            updateUI(serviceIntent);
        }
    };

    public BroadcastReceiver notificationBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.LogInfo(GlobalElements.isapplicationVisible() + "");
            if (GlobalElements.isActivityVisible()) {
                switch (intent.getAction()) {
                    case NotificationGenerator.NOTIFY_NEXT:
                        playNextSong();
                        break;
                    case NotificationGenerator.NOTIFY_DELETE:
                        Util.cancelNotification(context, NotificationGenerator.NOTIFICATION_ID);
                        if (GlobalElements.isMyServiceRunning(backgroundMusicService.class, getContext())) {
                            getContext().stopService(PlayMusicActivity.intent);
                            OnMusicServiceUpdated intercommunicator = (OnMusicServiceUpdated) getActivity();
                            intercommunicator.OnStopMusic();
                            mode = FRAGMENT_IN_PAUSE_MODE;
                            setViews();
                        }
                        break;
                    case NotificationGenerator.NOTIFY_PREVIOUS:
                        playPreviousSong();
                        break;
                    case NotificationGenerator.NOTIFY_PAUSE:
                        pauseMusic();
                        break;
                    case NotificationGenerator.NOTIFY_PLAY:
                        playMusic();
                        break;
                }
            }
        }
//        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBroadcastIsRegistered = true;
        context = getContext();
    }

    private void updateUI(Intent serviceIntent) {
        String counter = serviceIntent.getStringExtra("counter");
        String mediamax = serviceIntent.getStringExtra("mediamax");
        String strSongEnded = serviceIntent.getStringExtra("song_ended");
        int seekProgress = Integer.parseInt(counter);
        seekMax = Integer.parseInt(mediamax);
        songEnded = Integer.parseInt(strSongEnded);
        seekbar.setMax(seekMax);
        backgroundMusicService.MUSIC_PROGRESS = seekProgress;
        seekbar.setProgress(seekProgress);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_music_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        seekbar = (AppCompatSeekBar) view.findViewById(R.id.seekbar);
        seekbar.setProgress(backgroundMusicService.MUSIC_PROGRESS);
        imgPlay.setOnClickListener(this);
        imgPause.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        imgPrevious.setOnClickListener(this);
        seekbar.setOnSeekBarChangeListener(this);
        seekbar.setMax(backgroundMusicService.player.getDuration());
        setViews();
        return view;
    }

    private void setViews() {
        seekbar.setProgress(backgroundMusicService.MUSIC_PROGRESS);
        if (mode == FRAGMENT_IN_PLAY_MODE) {
            imgPause.setVisibility(View.VISIBLE);
            imgPlay.setVisibility(View.GONE);
        } else {
            imgPause.setVisibility(View.GONE);
            imgPlay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NotificationGenerator.NOTIFY_PLAY);
        intentFilter.addAction(NotificationGenerator.NOTIFY_DELETE);
        intentFilter.addAction(NotificationGenerator.NOTIFY_NEXT);
        intentFilter.addAction(NotificationGenerator.NOTIFY_PREVIOUS);
        intentFilter.addAction(NotificationGenerator.NOTIFY_PAUSE);
        getActivity().registerReceiver(notificationBroadcast, intentFilter);
        intent = new Intent(BROADCAST_SEEKBAR);
        //register reciever
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(
                backgroundMusicService.BROADCAST_ACTION));
        setViews();
        try {
            songName.setText("" + Util.getCurrentSongName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                        String type = intent.getStringExtra("type");
                        if (type.equals("1")) {
                            playNextSong();
                        } else {
                            pauseMusic();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            intentFilter = new IntentFilter();
            intentFilter.setPriority(1);
            intentFilter.addAction("com.cdms.craftbox.music.service");
            getActivity().registerReceiver(receiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(notificationBroadcast);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_play:
                playMusic();
                break;
            case R.id.img_pause:
                pauseMusic();
                break;
            case R.id.img_next:
                playNextSong();
                break;
            case R.id.img_previous:
                playPreviousSong();
                break;
        }
    }

    private void playPreviousSong() {
        Logger.LogInfo("play previous song method");
        if (backgroundMusicService.MUSIC_LIST_CURRENT_POSITION < backgroundMusicService.MUSIC_LIST_SIZE - 1
                && backgroundMusicService.MUSIC_LIST_CURRENT_POSITION > 0) {
            backgroundMusicService.MUSIC_LIST_CURRENT_POSITION = backgroundMusicService.MUSIC_LIST_CURRENT_POSITION - 1;
            UpdateSong();
        } else {
            //Toast.makeText(getContext(), "List has First Song", Toast.LENGTH_LONG).show();
        }

    }

    private void playNextSong() {
        Logger.LogInfo("play next song method");
        if (backgroundMusicService.MUSIC_LIST_CURRENT_POSITION < backgroundMusicService.MUSIC_LIST_SIZE - 1) {
            backgroundMusicService.MUSIC_LIST_CURRENT_POSITION = backgroundMusicService.MUSIC_LIST_CURRENT_POSITION + 1;
            UpdateSong();
        } else {
            //Toast.makeText(getContext(), "List has Last Song", Toast.LENGTH_LONG).show();
        }
    }

    private void pauseMusic() {
        Logger.LogInfo("pause music method");
        if (GlobalElements.isMyServiceRunning(backgroundMusicService.class, context)) {
            imgPause.setVisibility(View.GONE);
            imgPlay.setVisibility(View.VISIBLE);
            backgroundMusicService.player.pause();
            mode = FRAGMENT_IN_PAUSE_MODE;
            NotificationGenerator.customNotification(context, Util.getCurrentSongName());
            callInterface();
        } else {
            UpdateSong();
        }

    }

    private void playMusic() {
        Logger.LogInfo("play music method");
        if (GlobalElements.isMyServiceRunning(backgroundMusicService.class, context)) {
            imgPlay.setVisibility(View.GONE);
            imgPause.setVisibility(View.VISIBLE);
            backgroundMusicService.player.start();
            mode = FRAGMENT_IN_PLAY_MODE;
            NotificationGenerator.customNotification(context, Util.getCurrentSongName());
            callInterface();
        } else {
            UpdateSong();
        }
    }

    public void callInterface() {
        try {
            OnMusicServiceUpdated intercommunicator = (OnMusicServiceUpdated) getActivity();
            intercommunicator.updateView();
            Logger.LogInfo("call interface");
        } catch (Exception e) {
            e.printStackTrace();
            Logger.LogInfo(e.toString());
        }
    }

    public void UpdateSong() {
        if (GlobalElements.isMyServiceRunning(backgroundMusicService.class, getContext())) {
            getContext().stopService(PlayMusicActivity.intent);
        }
        PlayMusicActivity.intent = new Intent(getContext(), backgroundMusicService.class);
        getContext().startService(PlayMusicActivity.intent);
        mode = FRAGMENT_IN_PLAY_MODE;
        setViews();
        NotificationGenerator.customNotification(context, Util.getCurrentSongName());
        songName.setText("" + Util.getCurrentSongName());
        callInterface();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            int seekPos = seekBar.getProgress();
            intent.putExtra("seekpos", seekPos);
            getActivity().sendBroadcast(intent);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("on destroy view", "destroy view");
        if (mBroadcastIsRegistered) {
            try {
                getActivity().unregisterReceiver(broadcastReceiver);
                mBroadcastIsRegistered = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
            unbinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
