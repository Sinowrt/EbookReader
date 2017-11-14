package com.ebookreader;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.os.Environment;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DetailAct_Fragment extends Fragment {
    private View rootView;

    private GridView Contentgview;


    public int first_para;
    private int second_para;
    private int third_para;

    private String mSDCardPath;
    private List<File> mfiles = new ArrayList<File>();
    private File mCurrentPathFile = null;
    private static String url=null;
    private static Detail_fragment_gviewAdapter madapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        first_para= getArguments().getInt("first_type");
        second_para=getArguments().getInt("second_type");
        third_para=getArguments().getInt("third_path");

        checkEnvironment();

        pathComplete();
        previewAdd();

        //DatabaseContext dbContext = new DatabaseContext(this.getContext());
        //DBOpenHelper dbHelper = new DBOpenHelper(dbContext);
        //SQLiteDatabase rdb=dbHelper.getReadableDatabase();
        SQLiteDatabase wdb=SQLiteDatabase.openOrCreateDatabase("/storage/3633-3031/ebookReader/db/reader.db",null);




        if (null == rootView) {
            rootView = inflater.inflate(R.layout.detail_fragment_mainview, container,
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

        madapter = new Detail_fragment_gviewAdapter(getActivity(), mfiles,first_para);
        // 添加控件适配器

        Contentgview.setAdapter(madapter);

        // 添加GridView的监听事件
        Contentgview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {



                startActivity(intent_create(position));
            }
        });
    }

    private Intent intent_create(int position){
        Intent intent;
        String fileUrl=mfiles.get(position).getAbsolutePath();
        if(first_para==0&&second_para!=0){
            intent = new Intent(getActivity(), ReadingActivity.class);}

        else{
            intent = new Intent(getActivity(), video_player_Activity.class);
        }
            intent.putExtra("url", fileUrl);
            return intent;
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
        if(third_para==-1){
        url=mSDCardPath+"/ebookReader/"+first_para+"/"+second_para+"/";}
        else
        {
            url=mSDCardPath+"/ebookReader/"+first_para+"/"+second_para+"/"+third_para+"/";
        }

    }

    private void previewAdd() {
        File f=new File(url);
        mfiles.clear();
        if (f!=null) {
            mCurrentPathFile = f;

            File[] files = f.listFiles();
            Log.v("TAG","LENGRH"+files.length);

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
    }
}

