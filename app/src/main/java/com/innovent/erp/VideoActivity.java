package com.innovent.erp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import com.innovent.erp.custom.FullScreenVideoView;
import com.innovent.erp.netUtils.RetrofitClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoActivity extends AppCompatActivity {

    @BindView(R.id.video_view)
    FullScreenVideoView videoView;
    @BindView(R.id.login_txt)
    TextView loginTxt;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        try {
            pd = new ProgressDialog(VideoActivity.this);
            pd.setMessage("Buffering video please wait...");
            //pd.show();

            //Uri uri = Uri.parse("https://s3.amazonaws.com/androidvideostutorial/862013714.mp4");
            Uri uri = Uri.parse("" + RetrofitClient.video_url);

            videoView.setVideoURI(uri);
            //videoView.start();
            //videoView.setZOrderOnTop(true);
            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    videoView.start();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VideoActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
