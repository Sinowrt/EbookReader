package com.ebookreader;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;


import java.util.ArrayList;
import java.util.List;

public class ContentFragment extends Fragment {
    private View rootView;

    private List<Integer> res_preview;
    private GridView gridViewtuijian;
    private BaseAdapter tuijianAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

    private void init_res_preview(View rootView2) {
        gridViewtuijian = (GridView) rootView.findViewById(R.id.picview_gridview);
        // 数据源
        res_preview = new ArrayList<Integer>();
        res_preview.add(R.drawable.act_info);
        res_preview.add(R.drawable.bookstore);

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
                        video_player_Activity.class);
                //intent.putExtra("productId", position);
                startActivity(intent);
            }
        });
    }

}

