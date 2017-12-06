package com.ebookreader;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Jacky on 2017/12/6.
 */

public class Order_Detail_Fragment extends Fragment {
    private View rootView;
    private String order_Num;


    ListView mListView = null;
    ArrayList<booksInfo> mData= new ArrayList<booksInfo>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        order_Num= getArguments().getString("orderNum");
        init_listData();

        if (null == rootView) {
            rootView = inflater.inflate(R.layout.order_detail_fragment, container,
                    false);
            initView(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();

        if (null != parent) {
            parent.removeView(rootView);
        }

        return rootView;

    }

    private void initView(View rootView){


        TextView usrName=(TextView)rootView.findViewById(R.id.order_detail_customer_name);
        TextView usrNum=(TextView)rootView.findViewById(R.id.order_detail_customer_Number);
        TextView usrPrisonBlock=(TextView)rootView.findViewById(R.id.order_detail_customer_prisonBlock);
        TextView orderNumber=(TextView)rootView.findViewById(R.id.order_detail_orderNum);
        TextView orderItemNum=(TextView)rootView.findViewById(R.id.order_detail_booksnum);
        TextView OrderTotalPrice=(TextView)rootView.findViewById(R.id.order_detail_totalPrice);
        TextView orderDate=(TextView)rootView.findViewById(R.id.order_detail_time);
        TextView orderStatus=(TextView)rootView.findViewById(R.id.order_detail_status);



        MyApplication myApplication=(MyApplication)getActivity().getApplication();
        usrName.setText("收货人："+myApplication.getUser_name());
        usrNum.setText("囚号："+myApplication.getUser_number());
        usrPrisonBlock.setText("监区："+myApplication.getUser_PrisonBlock());
        orderItemNum.setText("书目合计："+mData.size());
        orderNumber.setText("订单编号："+order_Num);

        DBOpenHelper dbOpenHelper=new DBOpenHelper(this.getContext());
        Cursor cursorTemp = dbOpenHelper.query("orders", new String[]{"订单状态", "总金额", "下单时间"}, "订单号=?", new String[]{order_Num},null,null,null,null);

        cursorTemp.moveToFirst();
        orderDate.setText("下单时间："+cursorTemp.getString(2));
        orderStatus.setText("订单状态："+cursorTemp.getString(0));

        DecimalFormat form=new DecimalFormat("0.00");
        String price=form.format(cursorTemp.getDouble(1));
        OrderTotalPrice.setText("应付款：¥"+price);




        mListView = (ListView) rootView.findViewById(R.id.order_detail_listview);
        order_listviewAdapter adapter=new order_listviewAdapter(this.getContext(),mData);

        mListView.setAdapter(adapter);
    }

    private void init_listData(){
        DBOpenHelper dbOpenHelper=new DBOpenHelper(this.getContext());
        Cursor cursor = dbOpenHelper.query("order_detail", new String[]{"商品编号", "商品数量"}, "订单编号=?", new String[]{order_Num}, null, null, null, null);

        mData.clear();
        for (int i = 0; i<cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            Cursor cursorX = dbOpenHelper.query("books", new String[]{"封面路径","价格","书名"}, "商品编号=?", new String[]{cursor.getString(0)},null,null,null,null);
            cursorX.moveToFirst();
            booksInfo item=new booksInfo();
            item.imagePath=cursorX.getString(0);
            item.price=cursorX.getDouble(1);
            item.bookname=cursorX.getString(2);
            cursorX.close();
            item.booksnum=cursor.getInt(1);
            mData.add(item);
        }
        cursor.close();
    }

}

class booksInfo{
    String imagePath;
    String bookname;
    double price;
    int booksnum;
}
