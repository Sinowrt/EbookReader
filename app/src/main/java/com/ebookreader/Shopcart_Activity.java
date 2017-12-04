package com.ebookreader;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
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
    private static double total;
    private static TextView addup_tv;
    private Button submitOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopcart);
        gridView = (GridView) findViewById(R.id.shopcart_gridview);
        data=new ArrayList<Content>();
        total=0;
        Initdb();
        listAdd();
        init_toolbar("购物车");
        adapter=new Shopcart_Gview_Adapter(this,data);
        gridView.setAdapter(adapter);

    }

    @Override
    protected void onResume(){
        super.onResume();
        Initdb();
        listAdd();
        adapter.notifyDataSetChanged();
        addup_update(data);
    }

    public void Initdb(){
//        DatabaseContext dbContext = new DatabaseContext(this);
        DBOpenHelper dbHelper = new DBOpenHelper(this);

        cursor=dbHelper.query("shopcart",new String[]{"商品编号","书名","价格","购买数量","封面路径","作者"},null,null,null,null,null,null);
    }

    public List<Content> listAdd(){

        data.clear();
        for(int i=0;i<cursor.getCount();i++){
            Content content=new Content();
            if(cursor.moveToPosition(i)){

            content.item_number=cursor.getString(0);

            content.bookname=cursor.getString(1);
            content.price=cursor.getDouble(2);
            content.number=cursor.getInt(3);
            content.imagePath=cursor.getString(4);
            content.author=cursor.getString(5);
            }
            data.add(content);
        }
        //Log.d("Tag",data.get(0).author);
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

    public void init_toolbar(String tittle){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        Toolbar mToolbar = (Toolbar) findViewById(R.id.shopcart_toolbar);

        TextView tittle_tv=(TextView) findViewById(R.id.shopcart_tittle);
        submitOrder=(Button) findViewById(R.id.submitOrder);
        submitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(total==0) Toast.makeText(Shopcart_Activity.this,"当前购物车无商品",Toast.LENGTH_SHORT).show();
                else{
                Intent intent_toDetail = new Intent();
                intent_toDetail.setClass(Shopcart_Activity.this,order_detail_confirm.class);
                intent_toDetail.putExtra("order_status", 0);    //0代表订单确认
                intent_toDetail.putExtra("total_price",total);
                startActivity(intent_toDetail);}
            }
        });
        addup_tv=(TextView) findViewById(R.id.shopcart_addup);

        addup_tv.setTextColor(Color.parseColor("#FFFFFF"));
        tittle_tv.setTextColor(Color.parseColor("#FFFFFF"));
        tittle_tv.setText(tittle);

        addup_update(data);

        mToolbar.setTitle("");

        setSupportActionBar(mToolbar);
//        getSupportActionBar().hide();

    }

    public static void addup_update(ArrayList<Content> data){
        total=0;
        for(int i=0;i<data.size();i++){
            total=total+data.get(i).price*data.get(i).number;
        }
        DecimalFormat form=new DecimalFormat("0.00");
        String price=form.format(total);
        addup_tv.setText("合计：¥"+price);
    }

}
