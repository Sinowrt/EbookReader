package com.ebookreader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.lang.reflect.Array;

import static com.ebookreader.R.id.parent;


/**
 * Created by Jacky on 2017/10/18.
 * 新用户注册界面
 */

public class registerActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }
    private void init(){
        Spinner mSpinner=(Spinner) findViewById(R.id.spinner);
        String[] mStringArray=getResources().getStringArray(R.array.sex);
        //使用自定义的ArrayAdapter
        register_spiadapter mAdapter = new register_spiadapter(registerActivity.this,mStringArray);


        //设置下拉列表风格(这句不些也行)
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
        //监听Item选中事件
        //mSpinner.setOnItemSelectedListener(new ItemSelectedListenerImpl());

    }


}
