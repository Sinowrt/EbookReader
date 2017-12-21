package com.ebookreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

public class Account_detail extends Fragment {
    private View rootView;
    private String name;
    private String id;
    private String prison_block;
    private String image_path;
    private String sex;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_account_detail, container,
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
        TextView Account_id=(TextView) rootView.findViewById(R.id.Account_id);
        TextView Account_name=(TextView) rootView.findViewById(R.id.Account_name);
        TextView Account_prison_block=(TextView) rootView.findViewById(R.id.Account_prison_block);
        TextView Account_sex=(TextView) rootView.findViewById(R.id.Account_Sex);
        ImageView Account_image=(ImageView) rootView.findViewById(R.id.Account_image);

        MyApplication myApplication=(MyApplication)this.getActivity().getApplication();
        name=myApplication.getUser_name();
        id=myApplication.getUser_number();
        prison_block=myApplication.getUser_PrisonBlock();
        image_path=myApplication.getUser_image();
        sex=myApplication.getUser_sex();
        Image_Adapter image_adapter=new Image_Adapter(this.getContext());
        if(image_path==null){
            Account_image.setImageResource(R.drawable.initimage);
        }else
            image_adapter.setDrawable(image_path,Account_image);
        Account_id.setText("囚号："+id);
        Account_name.setText("姓名:"+name);
        Account_prison_block.setText("监区:"+prison_block);
        Account_sex.setText("性别："+sex);
    }
}