package com.ebookreader;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Jacky on 2017/10/8.
 */

public class DetailAct_listview_Adapter extends BaseAdapter {

    private Context context;
    private String[] strings;
    public static int mPosition;

    public DetailAct_listview_Adapter(Context context, String[] strings){
        this.context =context;
        this.strings = strings;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return strings.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return strings[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder  viewHolder=new ViewHolder();
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            viewHolder.tv=(TextView) convertView.findViewById(R.id.tv);
            mPosition = position;
            viewHolder.tv.setText(strings[position]);
            if (position == DetailActivity.mPosition) {
                convertView.setBackgroundResource(R.drawable.activity_info);
            } else {
                convertView.setBackgroundColor(Color.parseColor("#f4f4f4"));
            }
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv.setText(strings[position]);
        if (position == DetailActivity.mPosition) {
//            convertView.setBackgroundResource(R.drawable.bookstore);
            convertView.setBackgroundColor(Color.parseColor("#6699ff"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#f4f4f4"));
        }

        return convertView;
    }

    private static class ViewHolder
    {
        TextView tv;
    }
}