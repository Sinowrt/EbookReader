package com.ebookreader;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


import java.util.ArrayList;
import java.util.List;

public class ContentFragment extends Fragment {
    private View rootView;

    private List<Integer> res_preview;
    private GridView gridViewtuijian;
    private BaseAdapter tuijianAdapter;

    public int first_para;
    private int second_para;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        first_para= getArguments().getInt("first_type");
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_picview, container,
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
        init_res_preview(rootView);
    }


    private void init_res_preview(View rootView) {
        gridViewtuijian = (GridView) rootView.findViewById(R.id.picview_gridview);
        // 数据源
        res_preview = new ArrayList<Integer>();
        if(first_para==9)
        res_preview.add(R.drawable.video_preview);
        //res_preview.add(R.drawable.bookstore);

        // 适配器
        tuijianAdapter = new GridView_Detail_Adapter(getActivity(), res_preview);
        // 添加控件适配器
        gridViewtuijian.setAdapter(tuijianAdapter);
        // 添加GridView的监听事件
        gridViewtuijian.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // goto video_player_Activity
                Intent intent = new Intent(getActivity(),
                        LoginActivity.class);
                //intent.putExtra("productId", position);

                startActivity(intent);
            }
        });
    }



    public static class GridView_Detail_Adapter extends BaseAdapter {
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
}

