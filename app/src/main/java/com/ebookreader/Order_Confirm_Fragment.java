package com.ebookreader;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Jacky on 2017/12/2.
 */

public class Order_Confirm_Fragment extends Fragment{
    private View rootView;
    private double totalPrice;
    private Button submit;

    ListView mListView = null;
    ArrayList<booksInfo> mData= new ArrayList<booksInfo>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        init_listData();
        totalPrice= getArguments().getDouble("totalPrice");


        if (null == rootView) {
            rootView = inflater.inflate(R.layout.order_confirm_fragment, container,
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

        TextView usrName=(TextView)rootView.findViewById(R.id.order_confirm_customer_name);
        TextView usrNum=(TextView)rootView.findViewById(R.id.order_confirm_customer_Number);
        TextView usrPrisonBlock=(TextView)rootView.findViewById(R.id.order_confirm_customer_prisonBlock);
        TextView orderItemNum=(TextView)rootView.findViewById(R.id.order_confirm_booksnum);
        TextView OrderTotalPrice=(TextView)rootView.findViewById(R.id.order_confirm_totalPrice);

        submit=(Button)rootView.findViewById(R.id.order_confirm_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setMessage("订单提交后将不可修改，确认提交？").setTitle("提示");

                builder.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                        dialog.dismiss();
                        submitOrder();
                        Toast.makeText(getContext(),"订单提交成功！",Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                });

                builder.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                        dialog.dismiss();
                    }
                });

                builder.create().show();
            }
        });

        MyApplication myApplication=(MyApplication)getActivity().getApplication();
        usrName.setText("收货人："+myApplication.getUser_name());
        usrNum.setText("囚号："+myApplication.getUser_number());
        usrPrisonBlock.setText("监区："+myApplication.getUser_PrisonBlock());
        orderItemNum.setText("书目合计："+mData.size());
        DecimalFormat form=new DecimalFormat("0.00");
        String price=form.format(totalPrice);
        OrderTotalPrice.setText("应付款：¥"+price);




        mListView = (ListView) rootView.findViewById(R.id.order_confirm_listview);
        order_listviewAdapter adapter=new order_listviewAdapter(this.getContext(),mData);

        mListView.setAdapter(adapter);
    }

    /**
     *      1、时间（15）+随机数（2），从全局Application获取用户信息，结合totalPrice，生成时间，插入orders表
     *
     *     2、从shopcart表获取商品编号，数量，价格，插入orders_derail表
     */
    private void submitOrder(){
        DBOpenHelper dbOpenHelper=new DBOpenHelper(this.getContext());
        //生成UUID
//        String  uuid= UUID.randomUUID().toString().replaceAll("-", "");
        //从全局Application获取用户信息
        MyApplication myApplication=(MyApplication)getActivity().getApplication();
        //获取当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        SimpleDateFormat Or=new SimpleDateFormat("yyMMddHHmmssSS");
        String date = df.format(System.currentTimeMillis());// new Date()为获取当前系统时间，也可使用当前时间戳
        int rand=(int)Math.random()*100;
        String orderNum=Or.format(System.currentTimeMillis())+rand;

        ContentValues OrderContentValues=new ContentValues();
        OrderContentValues.put("订单号",orderNum);
        OrderContentValues.put("订单状态","待确认");
        OrderContentValues.put("囚号",myApplication.getUser_number());
        OrderContentValues.put("总金额",totalPrice);
        OrderContentValues.put("下单时间",date);
        dbOpenHelper.insert("orders",null,OrderContentValues);

        Cursor cursor=dbOpenHelper.query("shopcart",new String[]{"商品编号","购买数量"},null,null,null,null,null,null);
        ArrayList<ContentValues> items=new ArrayList<>();
        for(int i=0;i<cursor.getCount();i++){
            cursor.moveToPosition(i);
            ContentValues orderDetailContent=new ContentValues();

            orderDetailContent.put("商品编号",cursor.getString(0));
            orderDetailContent.put("商品数量",cursor.getInt(1));
            orderDetailContent.put("订单编号",orderNum);

            items.add(orderDetailContent);
        }
        cursor.close();

        for(int i=0;i<items.size();i++) {
            try{
                dbOpenHelper.insert("order_detail",null,items.get(i));
            }catch (SQLException e){
                Log.d("Tag",e.getMessage());
            }
        }

        dbOpenHelper.delete("shopcart",null,null);
    }

    private void init_listData(){
        DBOpenHelper dbOpenHelper=new DBOpenHelper(this.getContext());
        Cursor cursor=dbOpenHelper.query("shopcart",new String[]{"封面路径","书名","价格","购买数量"},null,null,null,null,null,null);
        Log.d("Tag",""+cursor.getCount());
        mData.clear();
        for (int i = 0; i<cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                booksInfo item=new booksInfo();
                item.imagePath=cursor.getString(0);
                item.bookname=cursor.getString(1);
                item.price=cursor.getDouble(2);
                item.booksnum=cursor.getInt(3);
                mData.add(item);
            }
            cursor.close();
    }



}
