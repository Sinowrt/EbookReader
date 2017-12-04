package com.ebookreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Jacky on 2017/12/3.
 */

class order_confirm_listviewAdapter extends BaseAdapter {
    private int[] colors = new int[] { 0xff626569, 0xff4f5257 };
    private Context mContext;
    private ArrayList<Order_Confirm_Fragment.booksInfo> mData;
    DecimalFormat form=new DecimalFormat("0.00");

    public order_confirm_listviewAdapter(Context context,ArrayList<Order_Confirm_Fragment.booksInfo> Data) {
        mContext = context;
        mData=Data;

    }

    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        viewH vh = null;
        if (convertView == null) {
            vh = new viewH();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_confirm_listview_item, parent, false);
            vh.bookimage= (ImageView) convertView.findViewById(R.id.order_confirm_bookImage);
            vh.bookname= (TextView) convertView.findViewById(R.id.order_confirm_bookname);
            vh.price=(TextView) convertView.findViewById(R.id.order_confirm_bookprice);
            vh.booksnum=(TextView) convertView.findViewById(R.id.order_confirm_itemNum) ;
            convertView.setTag(vh);
        } else {
            vh = (viewH) convertView.getTag();
        }

        Image_Adapter image_adapter=new Image_Adapter(mContext);
        image_adapter.setDrawable(mData.get(position).imagePath,vh.bookimage);
        vh.bookname.setText(mData.get(position).bookname);
        String price=form.format(mData.get(position).price);
        vh.price.setText("¥"+price);
        vh.booksnum.setText("x"+mData.get(position).booksnum);
        return convertView;
    }

    private class viewH{
        ImageView bookimage;
        TextView bookname;
        TextView price;
        TextView booksnum;
    }


}
