package com.ebookreader;

import java.io.File;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.video);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setContentView(R.layout.video);
        video1=(VideoView)findViewById(R.id.video1);
        mediaco=new MediaController(this);
        File file=new File("/storage/emulated/0/Pictures/Screenshots/11.mp4");
        if(file.exists()){
            //VideoView与MediaController进行关联
            video1.setVideoPath(file.getAbsolutePath());
            video1.setMediaController(mediaco);
            mediaco.setMediaPlayer(video1);
            //让VideiView获取焦点
            video1.requestFocus();
        }

    }

   // @Override
   // public boolean onCreateOptionsMenu(Menu menu) {
   //     getMenuInflater().inflate(R.menu.main, menu);
    //    return true;
    //}

}
