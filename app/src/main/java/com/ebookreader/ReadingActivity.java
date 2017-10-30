package com.ebookreader;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresPermission;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.R.id.button1;
import static android.R.id.hint;

public class ReadingActivity extends AppCompatActivity {
    private ContentResolver contentResolver;
    private File file;
    private MediaPlayer player;
    private String url;
    private String tittle;
    private String mp3Url;
    private String imageUrl;
    private String textUrl;
    private String recordUrl;
    private TextView bookContent = null;
    private AudioRecordManager Recorder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contentResolver = this.getContentResolver();
        setContentView(R.layout.activity_reading);

        acceptIntent();
        init_date();   //初始化数据
        getFilePath();
        init_toolbar(tittle);


        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.textbg);
        Drawable drawable =new BitmapDrawable(getImageThumbnail(imageUrl));
        linearLayout.setBackground(drawable);

        textDisplay();
        mediaPlayer();
        FloatingActionButton recordBtn = (FloatingActionButton) findViewById(R.id.recordButton);
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Recorder.isStart==true){
                    Recorder.stopRecord();
                }
                else{
                    if(player.isPlaying()==true)
                        player.stop();
                    Recorder.startRecord(recordUrl);

                }

            }
        });

        FloatingActionButton recplayBtn = (FloatingActionButton) findViewById(R.id.recplayButton);
        recplayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recorder.play();
            }
        });

    }

    public void init_toolbar(String tittle){
        if (Build.VERSION.SDK_INT >= 21) {
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tv=(TextView) findViewById(R.id.tittle);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        tv.setText(tittle);
        mToolbar.setTitle("");

        setSupportActionBar(mToolbar);
        getSupportActionBar().hide();

    }

    private void init_date(){
        try {
            Recorder = new AudioRecordManager();
            recordUrl=url+"/recordfile/exampleRecord";
            Toast.makeText(ReadingActivity.this, recordUrl, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(ReadingActivity.this, "init AudioRecordManager error！", Toast.LENGTH_SHORT).show();//提示异常
            finish();//直接关闭界面
        }
    }
/*
*         获取图片
*         @para imageUrl
*/
    public Bitmap getImageThumbnail(String myPath) {
        String[] projection = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, };
        String whereClause = MediaStore.Images.Media.DATA + "='" + myPath + "'";
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, whereClause,
                null, null);
        int _id = 0;
        String imagePath = "";
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        int id = 0;
        if (cursor.moveToFirst()) {

            int _idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            int _dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

            _id = cursor.getInt(_idColumn);
            imagePath = cursor.getString(_dataColumn);
            if (imagePath.equals(myPath)) {
                id = _id;
            }
        }
        cursor.close();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, id, MediaStore.Images.Thumbnails.MINI_KIND,
                options);
        return bitmap;
    }

    public void mediaPlayer(){
        file = new File(mp3Url);
        System.out.println(file.exists());
        if (file.exists()) {
            player = MediaPlayer
                    .create(this, Uri.parse(file.getAbsolutePath()));
            play();
        }else{
            /*hint.setText("要播放的音频文件不存在！");
            button1.setEnabled(false);*/
            return;
        }

        /*player.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

            @Override
            public void onCompletion(MediaPlayer mp) {
                play();
            }
        });*/
    }

    private void play() {
        try {
            player.reset();
            player.setDataSource(file.getAbsolutePath());
            player.prepare();
            player.start();
            /*hint.setText("正在播放音频...");*/
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void acceptIntent() {
        Intent intent_accept = getIntent();
        url = intent_accept.getStringExtra("url");
    }

    private void getFilePath(){
        file = new File(url);
        tittle=file.getName();
        if(file.isDirectory()){
        File[] files=file.listFiles();
        for(File file : files){
            String name =file.getName();
            if(name.contains(".mp3")) {mp3Url=file.getAbsolutePath();continue;}
            if(name.contains("background")) {imageUrl=file.getAbsolutePath();continue;}
            if(name.contains(".txt")) {textUrl=file.getAbsolutePath();continue;}
           }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            player.stop();
            this.finish();
            return true;
        }
        else
            return super.onKeyDown(keyCode,event);
    }

    public void textDisplay(){
        bookContent = (TextView)findViewById(R.id.textview);//绑定文本组件
        bookContent.setTextSize(20);
        bookContent.setMovementMethod(ScrollingMovementMethod.getInstance());//给文本组件加入滚动条

        String txtFilePath = textUrl;//获取传进来的文本路径
        if(txtFilePath != null){//如果有传进数据
            int index = txtFilePath.lastIndexOf(File.separator);//用来截取文本的名字的第一个参数
            String name = txtFilePath.substring(index+1, txtFilePath.length());//截取文本名字
            setTitle(name);//设置标题为文本名字
            try {
                FileInputStream fr = new FileInputStream(txtFilePath);//文件输出流
                BufferedReader br = new BufferedReader(new InputStreamReader(fr, "utf-8"));//缓冲读取文件数据
                String line = "" ;//记录每一行数据
                String content = "" ;
                while((line = br.readLine()) != null){//如果还有下一行数据
                    content += line + "\n" ;
                }
                bookContent.setText(content);;//追加显示数据
                br.close();//关闭文件输出流
                fr.close();//关闭缓冲区
            } catch (IOException e) {//抛出异常
                Toast.makeText(ReadingActivity.this, "没有此文件！", Toast.LENGTH_SHORT).show();//提示异常
                finish();//直接关闭界面
            }
        }
    }
}

