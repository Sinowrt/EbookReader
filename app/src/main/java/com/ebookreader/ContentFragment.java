package com.ebookreader;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;

import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import android.support.v7.widget.FitWindowsViewGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ContentFragment extends Fragment {
    private View rootView;

    private GridView Contentgview;


    public int first_para;
    private int second_para;

    private String mSDCardPath;
    private List<File> mfiles = new ArrayList<File>();
    private File mCurrentPathFile = null;
    private static String url=null;
    private static LocalFileAdapter madapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        first_para= getArguments().getInt("first_type");
        second_para=getArguments().getInt("second_type");
        checkEnvironment();
        pathComplete();
        previewAdd();


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

        Contentgview = (GridView) rootView.findViewById(R.id.picview_gridview);


        // 适配器

        madapter = new LocalFileAdapter(getActivity(), mfiles);
        // 添加控件适配器

        Contentgview.setAdapter(madapter);

        // 添加GridView的监听事件
        Contentgview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String fileUrl=mfiles.get(position).getAbsolutePath();
                Intent intent = new Intent(getActivity(),
                        video_player_Activity.class);
                intent.putExtra("url", fileUrl);
                startActivity(intent);
            }
        });
    }

    private void checkEnvironment() {
        File f = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        if (sdCardExist) {
            f = Environment.getExternalStorageDirectory();
            if (f != null) {
                mSDCardPath = f.getAbsolutePath();
            }

        } else {
            f = Environment.getRootDirectory();
            if (f != null) {
                mSDCardPath = f.getAbsolutePath();
            }
        }

    }

    private void pathComplete(){
        url=mSDCardPath+"/ebookReader/"+first_para+"/"+second_para+"/";

    }

    private void previewAdd() {
        File f=new File(url);
        mfiles.clear();
        if (f!=null) {
            mCurrentPathFile = f;

            File[] files = f.listFiles();
            for (File file : files) {
                if (file.isHidden()) {
                    continue;
                }
               addItem(file);

            }
        }
    }

    private void addItem(File f) {
        mfiles.add(f);

        //madapter.notifyDataSetChanged();
    }



}

