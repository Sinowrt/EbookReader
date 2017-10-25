package com.ebookreader;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class video_player_Activity extends Activity {

    private VideoView video1;
    MediaController  mediaco;
    private String url=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.video);
        acceptIntent();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        video1=(VideoView)findViewById(R.id.video1);
        mediaco=new MediaController(this);

        File file=new File(url);
        if(file.exists()){
            //VideoView与MediaController进行关联
            video1.setVideoPath(file.getAbsolutePath());
            video1.setMediaController(mediaco);
            mediaco.setMediaPlayer(video1);
            video1.start();
            //让VideiView获取焦点
            video1.requestFocus();
        }

    }

    public void acceptIntent() {
        Intent intent_accept = getIntent();
        url = intent_accept.getStringExtra("url");
    }

}
