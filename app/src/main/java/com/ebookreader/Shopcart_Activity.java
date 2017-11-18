package com.ebookreader;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 *    @ author sinowrt
 *    @ 书城购物车界面
 *
 * */

public class Shopcart_Activity extends AppCompatActivity {

    private ArrayList<Content> data;
    private Cursor cursor;
    private GridView gridView;
    private Shopcart_Gview_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopcart);
        gridView = (GridView) findViewById(R.id.shopcart_gridview);
        data=new ArrayList<Content>();
        Initdb();
        listAdd();
        adapter=new Shopcart_Gview_Adapter(this,data);
        gridView.setAdapter(adapter);
    }

    public void Initdb(){
        DatabaseContext dbContext = new DatabaseContext(this);
        DBOpenHelper dbHelper = new DBOpenHelper(dbContext);

        cursor=dbHelper.query("shopcart",new String[]{"商品编号","书名","价格","购买数量","封面路径","作者"},null,null,null,null,null,null);
    }

    public List<Content> listAdd(){
        Content content=new Content();
        data.clear();
        for(int i=0;i<cursor.getCount();i++){
            if(cursor.move(i+1)){
            content.item_number=cursor.getString(0);
                Log.d("Tag",cursor.getString(0));
            content.bookname=cursor.getString(1);
            content.price=cursor.getDouble(2);
            content.number=cursor.getInt(3);
            content.imagePath=cursor.getString(4);
            content.author=cursor.getString(5);

            data.add(content);}
            Log.d("Tag",""+data.size());

        }
        cursor.close();
        return data;
    }

    class Content{
        String imagePath;
        String item_number;
        String bookname;
        String author;
        double price;
        int number;
    }

}
