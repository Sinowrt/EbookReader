package com.ebookreader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Booksdetail_Activity extends AppCompatActivity {

    private String books_num;
    private Cursor cursor;
    private Image_Adapter image_adapter;
    private String cover_path;
    private Bitmap mLoadingBitmap;
    private int num,totalnum;

    private bookInfo bookinfo;

    private TextView numbertv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booksdetail);


        image_adapter=new Image_Adapter(this);

        mLoadingBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.filetype_doc);

        accept_Intent();
        Initdb();

        ImageView imageView=(ImageView) findViewById(R.id.bk_cover);
        TextView bookname=(TextView) findViewById(R.id.bk_bookname);
        bookname.setTextColor(Color.BLACK);

        TextView  summary=(TextView) findViewById(R.id.bk_summary);
        TextView  booksnum=(TextView) findViewById(R.id.bk_booksnum);
        TextView  price=(TextView) findViewById(R.id.bk_price);
        Button increase_btn=(Button) findViewById(R.id.bk_increase);
        Button decrease_btn=(Button) findViewById(R.id.bk_decrease);

        numbertv=(TextView) findViewById(R.id.bk_goodnum);
        numbertv.setText("0");

        price.setTextColor(Color.RED);

        Button addToshopcart=(Button) findViewById(R.id.bk_shopcartadd);



        bookname.setText(bookinfo.bookname);
        summary.setText(bookinfo.content);
        booksnum.setText("商品编号："+bookinfo.booknum);
        price.setText("价格："+bookinfo.price);
        cover_path=bookinfo.coverpath;


        BitmapDrawable drawable = image_adapter.getBitmapFromMemoryCache(cover_path);//先查看缓存是否有



        if (drawable != null) {

            imageView.setImageDrawable(drawable);
        } else if (image_adapter.cancelPotentialWork(cover_path, imageView)) {//没有就异步缓存

            Image_Adapter.BitmapWorkerTask task = image_adapter.new BitmapWorkerTask(imageView);
            Image_Adapter.AsyncDrawable asyncDrawable = image_adapter.new AsyncDrawable(this.getResources(), mLoadingBitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(cover_path);
        }

        addToshopcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bookinfo.buy_num!=0) {
                    totalnum=num+bookinfo.buy_num;
                    ContentValues cv = new ContentValues();
                    cv.put("商品编号", bookinfo.booknum);
                    cv.put("书名", bookinfo.bookname);
                    cv.put("价格", bookinfo.price);
                    cv.put("购买数量", totalnum);
                    cv.put("封面路径", bookinfo.coverpath);
                    cv.put("作者", bookinfo.author);

                    /////////////////////////////////////////////
                    DatabaseContext dbContext = new DatabaseContext(Booksdetail_Activity.this);
                    DBOpenHelper dbHelper = new DBOpenHelper(dbContext);

                        dbHelper.insert("shopcart", null, cv);

                    //cursor=dbHelper.query("shopcart",new String[]{"商品编号","书名","价格","购买数量","封面路径","作者"},null,null,null,null,null,null);
                    Toast.makeText(dbContext, bookinfo.buy_num+"本《"+bookinfo.bookname+"》已加入购物车", Toast.LENGTH_SHORT).show();
                }
            }
        });
        increase_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookinfo.buy_num++;

                numbertv.setText(""+bookinfo.buy_num);

            }
        });
        decrease_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookinfo.buy_num= max(--bookinfo.buy_num,0);

                numbertv.setText(""+bookinfo.buy_num);

            }
        });
    }


    public void accept_Intent(){
        Intent intent_accept = getIntent();
        Bundle bundle = intent_accept.getExtras();
        books_num = bundle.getString("books_number");
    }

    private void Initdb(){
        bookinfo=new bookInfo();
        DatabaseContext dbContext = new DatabaseContext(this);
        DBOpenHelper dbHelper = new DBOpenHelper(dbContext);


        cursor=dbHelper.query("books",new String[]{"书名","内容提要","价格","封面路径","作者"},"商品编号=?",new String[]{books_num},null,null,null,null);

        if(cursor.moveToFirst()){
            bookinfo.bookname=cursor.getString(0);
            bookinfo.content=cursor.getString(1);
            bookinfo.price=cursor.getDouble(2);
            bookinfo.coverpath=cursor.getString(3);
            bookinfo.author=cursor.getString(4);
        }
        cursor.close();
        bookinfo.buy_num=0;
        bookinfo.booknum=books_num;


        cursor=dbHelper.query("shopcart",new String[]{"购买数量"},"商品编号=?",new String[]{books_num},null,null,null,null);
        if(cursor.getCount()!=0){
            if(cursor.moveToFirst())
            num=cursor.getInt(0);
        }else
            num=0;

    }

    class bookInfo{
        String bookname;
        String content;
        double price;
        String coverpath;
        String booknum;
        int  buy_num;
        String author;
    }
}
