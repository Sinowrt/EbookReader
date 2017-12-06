package com.ebookreader;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class order_detail_confirm extends AppCompatActivity {
    private int status;
    private double totalPrice;
    private String orderNum;
    private Order_Confirm_Fragment myFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        acceptIntent();
        init_toolbar();
        init_View();




    }

    private void init_View(){
        if (status==0){
        myFragment = new Order_Confirm_Fragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.order_fragment_container, myFragment);
        Bundle bundle = new Bundle();
        bundle.putDouble("totalPrice",totalPrice);
        myFragment.setArguments(bundle);
        fragmentTransaction.commit();}
        else{
            Order_Detail_Fragment fragment = new Order_Detail_Fragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.replace(R.id.order_fragment_container, fragment);
            Bundle bundle = new Bundle();
            bundle.putString("orderNum",orderNum);
            fragment.setArguments(bundle);
            fragmentTransaction.commit();
        }
    }

    private void acceptIntent(){
        Intent intent_accept = getIntent();
        Bundle bundle = intent_accept.getExtras();
        status = bundle.getInt("order_status");
        if(status==0) totalPrice=bundle.getDouble("total_price");
        else orderNum=bundle.getString("orderNum");
    }

    private void init_toolbar(){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        Toolbar mToolbar = (Toolbar) findViewById(R.id.order_toolbar);
        TextView tv=(TextView) findViewById(R.id.order_tittle);


        tv.setTextColor(Color.parseColor("#FFFFFF"));
        if(status==0)
            tv.setText("订单确认");
        else
            tv.setText("订单详情");


        mToolbar.setTitle("");

        setSupportActionBar(mToolbar);
//        getSupportActionBar().hide();

    }
}
