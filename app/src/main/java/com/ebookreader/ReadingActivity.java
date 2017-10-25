package com.ebookreader;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReadingActivity extends AppCompatActivity {
    private ContentResolver contentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contentResolver = this.getContentResolver();
        setContentView(R.layout.activity_reading);
        init_toolbar("reading");


        ConstraintLayout constraintLayout=(ConstraintLayout) findViewById(R.id.reading);
        Drawable drawable =new BitmapDrawable(getImageThumbnail("/storage/3633-3031/ebookReader/0/1/timg.jpg"));
        constraintLayout.setBackground(drawable);
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
        tv.setText(tittle);
        mToolbar.setTitle("");


        setSupportActionBar(mToolbar);
//        getSupportActionBar().hide();

    }

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
}
