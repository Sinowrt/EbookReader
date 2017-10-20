package com.ebookreader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Jacky on 2017/10/18.
 * 新用户注册界面
 */

public class registerActivity extends Activity {
    int mYear, mMonth, mDay;
    Button btn;
    TextView dateDisplay;
    final int DATE_DIALOG = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

    }
    private void init(){
        Spinner mSpinner=(Spinner) findViewById(R.id.spinner);
        String[] mStringArray=getResources().getStringArray(R.array.sex);
        //使用自定义的ArrayAdapter
        regActivity_Spinner_Adapter mAdapter = new regActivity_Spinner_Adapter(registerActivity.this,mStringArray);


        //设置下拉列表风格(这句不些也行)
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
        //监听Item选中事件
        //mSpinner.setOnItemSelectedListener(new ItemSelectedListenerImpl());
        Button regButton = (Button) findViewById(R.id.regButton);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(registerActivity.this,"提交成功", Toast.LENGTH_SHORT ).show();

                finish();
            }
        });



    }


}
