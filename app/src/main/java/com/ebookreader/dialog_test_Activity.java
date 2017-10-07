package com.ebookreader;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class dialog_test_Activity extends AppCompatActivity{
    Button normalBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);

        normalBtn=(Button)findViewById(R.id.normalBtn);
        normalBtn.setOnClickListener(onClickListener);}
    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.normalBtn:{
                    AlertDialog.Builder builder=new AlertDialog.Builder(dialog_test_Activity.this);
                    builder.setMessage("确认退出吗？").setTitle("提示");

                    builder.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog,int which){
                            dialog.dismiss();
                            dialog_test_Activity.this.finish();
                        }
                    });

                    builder.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog,int which){
                            dialog.dismiss();
                        }
                    });

                    builder.create().show();
                    break;}
                default:
                    break;
                }

        }
    };
    }



