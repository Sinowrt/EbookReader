package com.ebookreader;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.SimpleAdapter;


public class MainActivity extends AppCompatActivity {
    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    private int[] icon = {
            R.drawable.act_info, R.drawable.bookstore,
            R.drawable.know, R.drawable.prisonin,
            R.drawable.prisonout, R.drawable.readmin,
            R.drawable.socia, R.drawable.specialedu,
            R.drawable.tech, R.drawable.think
    };
    private String[] iconName = { "活动通知", "凤凰书市", "学历教育", "入监教育", "出监教育", "每日诵读", "社会帮教",
            "专题教育", "技术教育", "思想教育" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //LoginActivity login=new LoginActivity();
        super.onCreate(savedInstanceState);
        //if(login.check_Islogin())
        setContentView(R.layout.activity_main);

        //else setContentView(R.layout.activity_login);
        gview = (GridView) findViewById(R.id.gview);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        //新建适配器
        String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.text};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.item, from, to);
        //配置适配器
        gview.setAdapter(sim_adapter);
        OpenNew();
    }

    public void OpenNew(){
        //第一个参数为当前Activity类对象，第二个参数为要打开的Activity类
        Intent intent =new Intent(MainActivity.this,Detail_Activity.class);
        startActivity(intent);
    }

    public List<Map<String, Object>> getData(){
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<icon.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }

        return data_list;
    }



}
