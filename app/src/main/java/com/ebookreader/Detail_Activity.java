package com.ebookreader;


import android.os.Bundle;

import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.Intent;

/**
 * Created by Mr. cai on 2017/9/29.
 * 二级目录listview以及gridview视图控制
 */

public class Detail_Activity extends FragmentActivity implements AdapterView.OnItemClickListener {
    /*ListView填充用*/

    public static int first_param = 0;

    public String[] strs = null;


    private static final String[] VIDEO_PROJECT = {MediaStore.Video.Media._ID,   MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE};

    /*二级listview textview填充*/
    private String[] tag_0 = {"修心课内容"};
    private String[] tag_1 = {};
    private String[] tag_2 = {"广东省监狱罪犯服刑指南", "监规纪律", "心理测试", "测验评估"};
    private String[] tag_3 = {"认罪悔罪", "法律常识", "公民道德", "劳动常识", "时事政治", "测验评估"};
    private String[] tag_4 = {"小学", "初中", "高中", "函授电大", "测验评估"};
    private String[] tag_5 = {"保健按摩师", "电器维修技术", "种养殖技术", "文案写作", "办公室软件应用", "测验评估"};
    private String[] tag_6 = {"出监教育", "出监教育大纲", "就业创业指导", "测验评估"};
    private String[] tag_7 = {"心理健康", "文艺教育", "测验评估"};
    private String[] tag_8 = {"畅销榜单", "重磅推荐", "经典名著", "名家散文", "知识技能", "国学经典", "社科文学", "青春文学", "套装图书", "健康保健", "书法绘画", "军事小说", "历史人物", "学习字典", "精装图书", "成功励志", "心灵鸡汤"};
    private String[] tag_9 = {"社会赠书", "公益讲座", "劳务招聘"};
    private String[] tag_10 = {"优惠活动", "文体活动", "监狱通知"};
    private String[] tag_11 = {};
    private String[] tag_12 = {};
    private String[] tag_13 = {};
    private String[] tag_14 = {};

    private ListView listView;
    private DetailAct_listview_Adapter adapter;
    private ContentFragment myFragment;
    /*选中的item的位数号码*/
    public static int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        /*初始化*/
        acceptIntent();
        get_Strindex();
        mPosition = 0;   //not init will cause The array bounds 17.10.8
        initView();
    }

    private void initView() {
        //匹配控件
        listView = (ListView) findViewById(R.id.listview);
        //传参
        adapter = new DetailAct_listview_Adapter(this, strs);
        listView.setAdapter(adapter);
        //监听事件
        listView.setOnItemClickListener(this);

        //创建MyFragment对象
        myFragment = new ContentFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, myFragment);
        //通过bundle传值给MyFragment
        Bundle bundle = new Bundle();
        bundle.putInt("first_type", first_param);    //来自一级目录的类目参数
        myFragment.setArguments(bundle);
        fragmentTransaction.commit();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        //拿到当前位置
        mPosition = position;
        //即使刷新adapter
        adapter.notifyDataSetChanged();
//for        for (int i = 0; i < strs.length; i++) {
            myFragment = new ContentFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, myFragment);
            //通过bundle传值给MyFragment
            Bundle bundle = new Bundle();
            bundle.putInt("first_type",first_param);
            bundle.putInt("second_index",position);
            myFragment.setArguments(bundle);
            fragmentTransaction.commit();
//for        }
    }

    /*创建接收MainActivity参数意图*/

    public void acceptIntent() {
        Intent intent_accept = getIntent();
        Bundle bundle = intent_accept.getExtras();
        // String name = bundle.getString("first");
        first_param = bundle.getInt("first");
    }

    /*二级目录字符串装载strs*/

    private void get_Strindex() {
        switch (first_param) {
            case 0:
                strs = tag_0;
                break;
            case 1:
                strs = tag_1;
                break;
            case 2:
                strs = tag_2;
                break;
            case 3:
                strs = tag_3;
                break;
            case 4:
                strs = tag_4;
                break;
            case 5:
                strs = tag_5;
                break;
            case 6:
                strs = tag_6;
                break;
            case 7:
                strs = tag_7;
                break;
            case 8:
                strs = tag_8;
                break;
            case 9:
                strs = tag_9;
                break;
            case 10:
                strs = tag_10;
                break;
            case 11:
                strs = tag_11;
                break;
            case 12:
                strs = tag_12;
                break;
            case 13:
                strs = tag_13;
                break;
            case 14:
                strs = tag_14;
                break;
            default:
                strs=null;

        }
    }

    /*public void video_getrespath() {

        String selection = MediaStore.Video.Media.DATA + " like ?";
        Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_PROJECT, selection, new String[]{"/storage/emulated/0/DCIM/Camera/Vc" + "%"}, MediaStore.MediaColumns.DATE_MODIFIED + " DESC");
        if (cursor != null) {
            int idindex = cursor.getColumnIndex(BaseColumns._ID);
            int modifiedindex = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
            int durationindex = cursor.getColumnIndex(MediaStore.Video.Media.TITLE);
            while (cursor.moveToNext()) {
                VideoInfo info = new VideoInfo();
                info.setId(cursor.getInt(idindex));
                info.setPath(cursor.getString(dataindex));
                info.setDateModified(cursor.getLong(modifiedindex));
                info.setDateTaken(cursor.getLong(takenindex));
                info.setDuration(cursor.getInt(durationindex));
                mVideoInfos.add(info);
                Log.d("videoinfo", info.toString());
            }
        }
    }*/


}

