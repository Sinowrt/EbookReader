package com.ebookreader;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2017/12/4.
 */

public class Shopping_History_Fragment extends Fragment {
    private View rootView;

    private ArrayList<OrderInfo> gviewData = new ArrayList<OrderInfo>();
    private ArrayList<String> imageData = new ArrayList<String>();
    private ArrayList<String> bookNumber =new ArrayList<>();
    private GridView mGview = null;
    private ShoppingHistory_Gview_Adapter shoppingHistory_gview_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        init_Gview_Data();

        if (null == rootView) {
            rootView = inflater.inflate(R.layout.shopping_history_fragment, container,
                    false);
            initView(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();

        if (null != parent) {
            parent.removeView(rootView);
        }

        return rootView;

    }

    private void initView(View rootView) {

        mGview=(GridView) rootView.findViewById(R.id.shopping_history_gridview);
        shoppingHistory_gview_adapter=new ShoppingHistory_Gview_Adapter(getActivity(),gviewData);
        mGview.setAdapter(shoppingHistory_gview_adapter);
        mGview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                intent_GoToOrderActivity(position);
            }
        });
    }

    public void intent_GoToOrderActivity(int pos) {
        Intent intent_toOrder = new Intent();
        intent_toOrder.setClass(this.getActivity(),order_detail_confirm.class);
        intent_toOrder.putExtra("order_status",1);//0代表订单确认 1表示订单详情
        intent_toOrder.putExtra("orderNum", gviewData.get(pos).orderNum);
        startActivity(intent_toOrder);
    }



    private void init_Gview_Data() {
        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        String user_number = myApplication.getUser_number();
        DBOpenHelper dbOpenHelper = new DBOpenHelper(this.getContext());
        Cursor cursor = dbOpenHelper.query("orders", new String[]{"订单号", "订单状态", "下单时间", "总金额"}, "囚号=?", new String[]{user_number}, null, null, null, null);
        gviewData = new ArrayList<OrderInfo>();

        //n个订单 n个cursor
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            imageData = new ArrayList<String>();
            //1个订单 n个Item
            Cursor cursorX = dbOpenHelper.query("order_detail", new String[]{"商品编号"}, "订单编号=?", new String[]{cursor.getString(0)},null,null,null,null);

            for(int num=0;num<cursorX.getCount();num++){
                cursorX.moveToPosition(num);
                Cursor cursorV = dbOpenHelper.query("books", new String[]{"封面路径"}, "商品编号=?", new String[]{cursorX.getString(0)},null,null,null,null);
                cursorV.moveToFirst();
                imageData.add(cursorV.getString(0));
                bookNumber.add(cursorX.getString(0));
                cursorV.close();
            }
            cursorX.close();
            OrderInfo orderInfo = new OrderInfo(cursor.getString(0), cursor.getString(1),imageData,bookNumber,cursor.getString(2),cursor.getDouble(3));
            gviewData.add(orderInfo);
        }

        cursor.close();
    }

        public class OrderInfo {
            public String orderNum;
            public String orderStatus;
            public ArrayList<String> imageUrls;
            public ArrayList<String> bookNumber;
            public String orderDate;
            public double orderPrice;

            public OrderInfo(String orderNum, String orderStatus, ArrayList<String> imageUrls,ArrayList<String> bookNumber, String orderDate, double orderPrice) {
                this.orderNum = orderNum;
                this.orderStatus = orderStatus;
                this.orderDate = orderDate;
                this.orderPrice = orderPrice;
                this.imageUrls = imageUrls;
                this.bookNumber=bookNumber;
            }
        }

        public class ShoppingHistory_Gview_Adapter extends BaseAdapter {
            private LayoutInflater mInflater;
            public Context mContext;
            private ArrayList<OrderInfo> mDatas;
            private int position;
            DecimalFormat form = new DecimalFormat("0.00");

            public ShoppingHistory_Gview_Adapter(Context context, ArrayList<OrderInfo> mDatas) {
                mInflater = LayoutInflater.from(context);
                this.mContext = context;
                this.mDatas = mDatas;

            }

            @Override
            public int getCount() {
                return mDatas.size();
            }

            @Override
            public Object getItem(int position) {
                return mDatas.size();
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder = null;

                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.shopping_history_fragment_gview_item, parent, false);
                    viewHolder = new ViewHolder();

                    viewHolder.orderNumberTv = (TextView) convertView.findViewById(R.id.shopping_history_order_number);
                    viewHolder.orderStatus=(TextView) convertView.findViewById(R.id.shopping_history_order_status);
                    viewHolder.orderItemGv = (GridView) convertView.findViewById(R.id.shopping_history_gview_order_item);
                    viewHolder.orderDateTv = (TextView) convertView.findViewById(R.id.shopping_history_order_date);
                    viewHolder.orderTpriceTv = (TextView) convertView.findViewById(R.id.shopping_history_order_totalPrice);

                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                //初始化当前项目订单编号
                viewHolder.orderNumberTv.setText("订单编号："+mDatas.get(position).orderNum);

                viewHolder.orderStatus.setText("订单状态："+mDatas.get(position).orderStatus);

                ItemGviewAdapter itemGviewAdapter=new ItemGviewAdapter(mContext,mDatas.get(position).imageUrls);
                viewHolder.orderItemGv.setAdapter(itemGviewAdapter);
                viewHolder.orderItemGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent= new Intent(getActivity(),Booksdetail_Activity.class);
                        intent.putExtra("books_number",bookNumber.get(position));
                        startActivity(intent);
//                        Toast.makeText(Shopping_History_Fragment.super.getActivity(),"请点击单号查看订单详情",Toast.LENGTH_SHORT).show();
                    }
                });


                viewHolder.orderDateTv.setText("订单日期："+mDatas.get(position).orderDate);

                String price = form.format(mDatas.get(position).orderPrice);
                viewHolder.orderTpriceTv.setText("订单合计：" + price);

                return convertView;
            }


            private class ViewHolder {
                TextView orderNumberTv;
                TextView orderStatus;
                GridView orderItemGv;
                TextView orderDateTv;
                TextView orderTpriceTv;
            }
        }

        public class ItemGviewAdapter extends BaseAdapter{
            private Image_Adapter image_adapter;
            private LayoutInflater mInflater;
            private ArrayList<String> imageUrls;

            public ItemGviewAdapter(Context context, ArrayList<String> imageUrls) {
                mInflater = LayoutInflater.from(context);
                this.imageUrls=imageUrls;
                image_adapter = new Image_Adapter(context);

            }

            @Override
            public int getCount() {
                return imageUrls.size();
            }

            @Override
            public Object getItem(int position) {
                return Math.min(imageUrls.size(),8);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder = null;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.sh_order_item_gview, parent, false);
                    viewHolder = new ViewHolder();
                    viewHolder.imageView = (ImageView) convertView.findViewById(R.id.sh_order_item_imageView);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                image_adapter.setDrawable(imageUrls.get(position),viewHolder.imageView);
                Log.d("Tag",imageUrls.get(position));
                return convertView;
            }


            private class ViewHolder {
                ImageView imageView;
            }
        }
    }