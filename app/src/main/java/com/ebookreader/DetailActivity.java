package com.ebookreader;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ExpandableListView;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. cai on 2017/9/29.
 * 二级目录listview以及gridview视图控制
 */

public class DetailActivity extends FragmentActivity {
    /*ListView填充用*/

    public static int first_param = 0;
    private static int second_param=0;

    public List<String> group_strs=new ArrayList<String>();
    public List<List<String>> child_str=new ArrayList<List<String>>();
    public List<String> e;


    /*二级listview textview填充*/
    private String[][] group_tag = {
            {"服刑人员行为规范","国学经典语录","励志经典语录","修心养身语录"},
            {},
            {"认识监狱", "认罪服法", "监规纪律","教育改造","健康服刑","奖罚考核","常见问题问答", "测验评估"},
            {"认罪悔罪", "法律常识", "公民道德", "劳动常识", "时事政治", "测验评估"},
            {"扫盲","小学", "初中", "高中", "函授电大", "测验评估"},
            {"电器维修技术","保健养生", "种养殖技术", "计算机操作技能", "测验评估"},
            {"回归准备", "心理调适", "身份恢复", "亲情连线","社区融入","形式政策","就业指导","创业指导","测验评估"},
            {"爱国主义", "感恩教育", "心理健康","艺术教育","成功故事","国学经典" },
            {"重点推荐","畅销新书","精装图书","科幻小说","历史传记", "青春文学","人文社科","法律知识","心理健康","职业技能","养殖技术","休闲娱乐","保健养生","书法绘画","学习字典"},
            {"社会赠书", "劳务招聘（应聘意向登记表）","技能培训","帮教问答","公益求助","求职荐工"},
            {"优惠活动", "文体活动", "监狱通知"},
            {},
            {"个人信息","余额查询","学习积分","购物记录"},
            {},
            {}};
    private String[][][] child_tag={
            {{},{"诗歌","国学"},{},{}},
            {},
            {{},{},{},{},{},{},{},{}},
            {{},{},{},{},{},{}},
            {{},{"语文","数学","英语"},{"语文","数学","历史"},{},{},{}},
            {{},{},{},{},{}},
            {{},{},{},{},{},{},{},{},{}},
            {{},{},{},{},{},{}},
            {{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}},
            {{},{},{},{},{},{}},
            {{},{},{}},
            {{},{}},
            {{},{},{},{}},
            {{}},
            {{}}

    };

    private ExpandableListView expandableListView;
    private DetailAct_exlistview_Adapter adapter;   //listview adapter
    private DetailAct_Fragment myFragment;

    public static int gPosition;
    public static int cPosition;
    public static int pre_gPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        /*初始化*/
        Log.v("TAG","sussccesssssss");
        acceptIntent();
        get_Str();
        gPosition = 0;   //not init will cause The array bounds 17.10.8
        initView();
    }

    private void initView() {

            //匹配控件
            expandableListView = (ExpandableListView) findViewById(R.id.expandablelistview);
            //传参
            adapter = new DetailAct_exlistview_Adapter(this, group_strs, child_str);
            expandableListView.setAdapter(adapter);
            //监听事件
            expandableListView.setGroupIndicator(null);

        //创建MyFragment对象

            myFragment = new DetailAct_Fragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, myFragment);
            //通过bundle传值给MyFragment
            Bundle bundle = new Bundle();
            bundle.putInt("first_type", first_param);    //传递来自一级目录的类目参数
            bundle.putInt("second_type", 0);   //传递来自二级目录listview参数
        /**
         *进入DetailActivity时默认打开二级目录0；
         * 判断二级目录下是否有三级目录；
         * 若有则传入默认三级参数third_path为0并展开列表，
         * 若无则传入参数third_path为-1
         * */
        if(child_str.size()==0){
            bundle.putInt("third_path", -1);
        }
        else {
            if (child_str.get(second_param).size() == 0) {
                bundle.putInt("third_path", -1);
            } else {
                expandableListView.expandGroup(0);
                bundle.putInt("third_path", 0);
            }
        }

            myFragment.setArguments(bundle);

            fragmentTransaction.commit();
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                        expandableListView.collapseGroup(pre_gPos);
            }

        });

        /**
         * ExpandableListView的组监听事件
         */
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                pre_gPos=gPosition;

                gPosition=groupPosition;
                adapter.notifyDataSetChanged();



                if(child_str.get(groupPosition).size()==0){
                    parent.collapseGroup(pre_gPos);
                    myFragment = new DetailAct_Fragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, myFragment);
                    //通过bundle传值给MyFragment
                    Bundle bundle = new Bundle();
                    bundle.putInt("first_type",first_param);
                    bundle.putInt("second_type",groupPosition);
                    bundle.putInt("third_path",-1);   //-1表示无三级目录
                    myFragment.setArguments(bundle);
                    fragmentTransaction.commit();
                    return true;
                }
                else{
                    cPosition=0;
                    myFragment = new DetailAct_Fragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, myFragment);
                    //通过bundle传值给MyFragment
                    Bundle bundle = new Bundle();
                    bundle.putInt("first_type",first_param);
                    bundle.putInt("second_type",groupPosition);
                    bundle.putInt("third_path",0);
                    myFragment.setArguments(bundle);
                    fragmentTransaction.commit();
                }
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view,
                                        int parentPos, int childPos, long l) {
                gPosition=parentPos;
                cPosition=childPos;
                adapter.notifyDataSetChanged();
                myFragment = new DetailAct_Fragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, myFragment);
                //通过bundle传值给MyFragment
                Bundle bundle = new Bundle();
                bundle.putInt("first_type",first_param);
                bundle.putInt("second_type",parentPos);
                bundle.putInt("third_path",childPos);
                myFragment.setArguments(bundle);
                fragmentTransaction.commit();
                return true;
            }
        });
    }

    /*创建接收MainActivity参数意图*/

    public void acceptIntent() {
        Intent intent_accept = getIntent();
        Bundle bundle = intent_accept.getExtras();
        // String name = bundle.getString("first");
        first_param = bundle.getInt("first");
    }

    /*二级目录字符串装载strs*/
    private void get_Str() {
        for(int i=0;i<group_tag[first_param].length;i++)
            group_strs.add(group_tag[first_param][i]);

        for(int i=0;i<child_tag[first_param].length;i++)
        {
            e=new ArrayList<String>();
            for(int j=0;j<child_tag[first_param][i].length;j++)
            {
                e.add(child_tag[first_param][i][j]);
            }
            child_str.add(e);
        }
    }


}

