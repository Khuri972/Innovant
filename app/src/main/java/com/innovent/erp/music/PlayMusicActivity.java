package com.innovent.erp.music;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.music.custom.AppConstant;
import com.innovent.erp.music.custom.NotificationGenerator;
import com.innovent.erp.music.custom.Util;
import com.innovent.erp.music.fragments.MusicDetailFragment;
import com.innovent.erp.music.model.Music;
import com.innovent.erp.music.service.backgroundMusicService;
import com.innovent.erp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayMusicActivity extends BaseActivity implements MusicDetailFragment.OnMusicServiceUpdated  {

    public static Intent intent;
    @BindView(R.id.layout_play_music)
    ContentFrameLayout layoutPlayMusic;
    @BindView(R.id.img_music)
    ImageView imgMusic;
    Music music;
    @BindView(R.id.tv_playing_music_name)
    TextView tvPlayingMusicName;
    FragmentManager fm = getSupportFragmentManager();
    public static Fragment newFragment;
    @BindView(R.id.img_title_song_toolbar)
    ImageView imgTitleSongToolbar;
    @BindView(R.id.tv_song_title_toolbar)
    AppCompatTextView tvSongTitleToolbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MusicDetailFragment.mode = MusicDetailFragment.FRAGMENT_IN_PLAY_MODE;
        setContentView(R.layout.activity_play_music);
        ButterKnife.bind(this);
        int from_where = getIntent().getIntExtra(AppConstant.FROM_WHERE, AppConstant.FROM_ACTIVITY);
        setSupportActionBar(toolbar);
        if (from_where == AppConstant.FROM_NOTIFICATION) {
            Util.UpdatePlayerLayout(PlayMusicActivity.this, layoutPlayMusic, fm, newFragment);
            NotificationGenerator.customNotification(getApplicationContext(), Util.getCurrentSongName());
        } else {
            try {
                backgroundMusicService.MUSIC_LIST_SIZE = backgroundMusicService.musicArrayList.size();
                backgroundMusicService.MUSIC_LIST_CURRENT_POSITION = getIntent().getIntExtra(AppConstant.MUSIC, 0);
                updateUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
            new TestAsync().execute();
        }
    }

    @Override
    public FragmentManager getSupportFragmentManager() {
        return super.getSupportFragmentManager();
    }


    @Override
    public void updateView() {
        updateUI();
    }

    @Override
    public void OnStopMusic() {
    }

    public void updateUI() {
        Glide.with(this).load(backgroundMusicService.musicArrayList.get(backgroundMusicService.MUSIC_LIST_CURRENT_POSITION).getImageUrl())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgTitleSongToolbar);
        tvSongTitleToolbar.setText(backgroundMusicService.musicArrayList.get(backgroundMusicService.MUSIC_LIST_CURRENT_POSITION).getName());
        music = backgroundMusicService.musicArrayList.get(backgroundMusicService.MUSIC_LIST_CURRENT_POSITION);
        Glide.with(this).load(music.getImageUrl())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgMusic);
    }

    public class TestAsync extends AsyncTask<Void, Integer, String> {
        String TAG = "asyck task";
        protected void onPreExecute() {
            super.onPreExecute();
            if (GlobalElements.isMyServiceRunning(backgroundMusicService.class, PlayMusicActivity.this)) {
                try {
                    stopService(intent);
                } catch (Exception e) {
                    Log.e("Errorstoppingservice", e.toString());
                }
            }
        }

        protected String doInBackground(Void... arg0) {
            intent = new Intent(PlayMusicActivity.this, backgroundMusicService.class);
            startService(intent);
            return "You are at PostExecute";
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Util.UpdatePlayerLayout(PlayMusicActivity.this, layoutPlayMusic, fm, newFragment);
            NotificationGenerator.customNotification(getApplicationContext(), music.getName());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
