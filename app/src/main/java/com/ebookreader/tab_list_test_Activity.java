package com.ebookreader;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class tab_list_test_Activity extends ListActivity {
    Button MyButton;
    private String[] tag_1={"优惠活动","文体活动","监狱通知"};
    private String[] tag_2={"畅销榜单","重磅推荐","经典名著","名家散文","知识技能","国学经典","社科文学","青春文学","套装图书","健康保健","书法绘画","军事小说","历史人物","学习字典","精装图书","成功励志","心灵鸡汤"};
    private String[] tag_3={"小学","初中","高中","函授电大","测验评估"};
    private String[] tag_4={"广东省监狱罪犯服刑指南","监规纪律","心理测试","测验评估"};
    private String[] tag_5={"出监教育","出监教育大纲","就业创业指导","测验评估"};
    private String[] tag_6={"修心课内容"};
    private String[] tag_7={"社会赠书","公益讲座","劳务招聘"};
    private String[] tag_8={"心理健康","文艺教育","测验评估"};
    private String[] tag_9={"保健按摩师","电器维修技术","种养殖技术","文案写作","办公室软件应用","测验评估"};
    private String[] tag_0={"认罪悔罪","法律常识","公民道德","劳动常识","时事政治","测验评估"};

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taglist);

        MyButton = (Button)findViewById(R.id.myButton);
        setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, tag_1));
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        MyButton.setText(tag_1[position]);
        super.onListItemClick(l, v, position, id);
    }
    private void get_Strindex(){
        int i=1;
        String[] str;
        switch (i){
            case 1: str=tag_1;break;
            case 2: str=tag_2;break;
            case 3: str=tag_3;break;
            case 4: str=tag_4;break;
            case 5: str=tag_5;break;
            case 6: str=tag_6;break;
            case 7: str=tag_7;break;
            case 8: str=tag_8;break;
            case 9: str=tag_9;break;
            case 0: str=tag_0;break;
        }
    }


}