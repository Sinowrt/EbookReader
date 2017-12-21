package com.ebookreader;

import android.app.Application;
import android.database.Cursor;

/**
 * Created by Jacky on 2017/12/3.
 *
 * 用于全局变量的存储
 *
 */

public class MyApplication extends Application {
    private CustomInfo customerInfo;

    public void setUserInfo(String number){
        customerInfo=new CustomInfo();
        DBOpenHelper dbOpenHelper=new DBOpenHelper(this);
        Cursor cursor=dbOpenHelper.query("user_account",new String[]{"姓名","监区","用户照片","性别"},"囚号=?",new String[]{number},null,null,null,null);
        if (cursor.moveToFirst()){
            customerInfo.user_name=cursor.getString(0);
            customerInfo.user_PrisonBlock=cursor.getString(1);
            customerInfo.user_number=number;
            customerInfo.user_image=cursor.getString(2);
            customerInfo.user_sex = cursor.getString(3);
        }
        cursor.close();
    }

    public String getUser_sex(){
        return customerInfo.user_sex;
    }

    public String getUser_name(){
        return customerInfo.user_name;
    }

    public String getUser_number(){
        return customerInfo.user_number;
    }

    public String getUser_image(){
        return customerInfo.user_image;
    }

    public String getUser_PrisonBlock(){return customerInfo.user_PrisonBlock; }

    public class CustomInfo{
        String user_name;
        String user_number;
        String user_PrisonBlock;
        String user_image;
        String user_sex;
    }

}
