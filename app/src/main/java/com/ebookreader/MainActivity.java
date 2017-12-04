package com.ebookreader;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;



public class MainActivity extends AppCompatActivity {
    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    private int[] icon = {
            R.drawable.perday_reading,  R.drawable.phoenix_lecture,    R.drawable.prisonin,
            R.drawable.ideological_edu, R.drawable.education,          R.drawable.technology,
            R.drawable.prisonout,       R.drawable.specialedu,         R.drawable.bookstore,
            R.drawable.social_help,     R.drawable.activity_info,      R.drawable.learning_alam,
            R.drawable.myphoenix,       R.drawable.game,               R.drawable.learning_note

    };
    private String[] iconName = {
            "每日诵读","凤凰讲座","入监教育",
            "思想教育","文化教育","技术教育",
            "出监教育","专题教育","凤凰书市",
            "社会帮教","活动通知","学习提醒",
            "我的凤凰","益智游戏","学习日记"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //LoginActivity login=new LoginActivity();
        super.onCreate(savedInstanceState);
        //if(login.check_Islogin())
        setContentView(R.layout.activity_main);
        //else setContentView(R.order_confirm_fragment.activity_login);
        gview = (GridView) findViewById(R.id.gview);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        //新建适配器
        String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.text};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.main_gview_item, from, to);
        //配置适配器

        gview.setAdapter(sim_adapter);

        gview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                intent_GoToDetailActivity(position);
            }
        });




    }

    public void intent_GoToDetailActivity(int pos) {
        Intent intent_toDetail = new Intent();
        intent_toDetail.setClass(this,DetailActivity.class);
        intent_toDetail.putExtra("first", pos);
        intent_toDetail.putExtra("first_name",iconName[pos]);
        startActivity(intent_toDetail);
    }

    public List<Map<String, Object>> getData() {
        for(int i=0;i<icon.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }

        return data_list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.start:{
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("此操作将会退出登录，确认退出吗？").setTitle("提示");

                builder.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                        dialog.dismiss();
                        MainActivity.this.finish();
                    }
                });

                builder.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                        dialog.dismiss();
                    }
                });

                builder.create().show();
                break;}
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("此操作将会退出登录，确认退出吗？").setTitle("提示");

            builder.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog,int which){
                    dialog.dismiss();
                    MainActivity.this.finish();
                }
            });

            builder.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog,int which){
                    dialog.dismiss();
                }
            });

            builder.create().show();
            return true;
        }
        else
            return super.onKeyDown(keyCode,event);
    }


}
