package com.clic.org.serve.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.clic.org.R;
import com.clic.org.serve.Utils.ClicUtils;

public class SplashActivity extends AppCompatActivity {

    VideoView mVideoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mVideoView = (VideoView)findViewById(R.id.videoView);

        String path = "android.resource://" + getPackageName() + "/"+R.raw.clip ;
        Button btn = (Button)findViewById(R.id.btn_explore);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mVideoView.stopPlayback();
                finish();
                startActivity(new Intent(SplashActivity.this,SignupGuideActvity.class));
            }
        });


        mVideoView.setVideoURI(Uri.parse(path));
        mVideoView.start();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,SignupGuideActvity.class));
                finish();
            }
        },2000);*/
    }
}
