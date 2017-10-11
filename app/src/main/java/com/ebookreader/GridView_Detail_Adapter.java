package com.ebookreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class GridView_Detail_Adapter extends BaseAdapter {
    private Context context;
    private List<Integer> data;

    public GridView_Detail_Adapter(Context context, List<Integer> data) {
        super();
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.fragment_pic_item, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView
                    .findViewById(R.id.pic_grid_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Integer current = data.get(position);
        holder.iv.setImageResource(current);
        return convertView;
    }

    static class ViewHolder {
        ImageView iv;
    }
}
