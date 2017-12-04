package com.ebookreader;

/**
 * Created by Jacky on 2017/10/18.
 */

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Detail_fragment_gviewAdapter extends BaseAdapter {
    private int getView_type;
    private Cursor cursor;
    private Context context;
    private List<File> files;
    private LayoutInflater layout;
    private Image_Adapter image_adapter;

    private Bitmap mLoadingBitmap;


    public Detail_fragment_gviewAdapter(Context cont, List<File> f) {
        this.context = cont;
        this.files = f;
        this.getView_type=0;

        image_adapter=new Image_Adapter(cont);
        mLoadingBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_doc);
        layout = LayoutInflater.from(context);
    }

    public Detail_fragment_gviewAdapter(Context cont, Cursor cursor) {

        this.cursor=new CursorWrapper(cursor);
        this.context = cont;
        this.cursor = cursor;
        this.getView_type=1;

        image_adapter=new Image_Adapter(cont);
        mLoadingBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_doc);
        layout = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        //System.out.println("+++++++++++++++++++getCount num："+count+++"                       return num:"+files.size());
        if(getView_type==0)
            return files.size();
        else

            return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //System.out.println("xcqw getView  1***position: "+position);
        ViewH vh = null;
        if (convertView == null) {
            vh = new ViewH();
            convertView = layout.inflate(R.layout.detail_fragment_gview_item, parent, false);
            vh.list_file_imag = (ImageView) convertView.findViewById(R.id.pic_grid_item);
            vh.list_file_name = (TextView) convertView.findViewById(R.id.text_grid_item);
            vh.list_file_price=(TextView) convertView.findViewById(R.id.price);
            convertView.setTag(vh);
        } else {
            vh = (ViewH) convertView.getTag();
        }

        if(getView_type==0){
        File curFile = files.get(position);

        String fileName = files.get(position).getName();
        String[] names = fileName.split("\\.");
        String fileOtherName=names[0];     //得到不带后缀的文件名

        vh.list_file_name.setText(fileOtherName);





            vh.list_file_imag.setImageResource(R.drawable.filetype_doc);


            if (image_adapter.getFileType(curFile.getName()) != 0) {
                //判断是否是图片或者是视屏

                image_adapter.setDrawable(curFile.getAbsolutePath(),vh.list_file_imag);

            }else{
                if(curFile.isDirectory()) {
                    String coverUrl=findCoverimage(curFile);
                    image_adapter.setDrawable(coverUrl,vh.list_file_imag);
                }
            }
        return convertView;}

        else{

            /**
             *      @适配书城界面
             *
             *
             *
             *      完成书城数据库接入
             *
             *
             * */


            DecimalFormat form=new DecimalFormat("0.00");



            if(cursor.moveToPosition(position)){
                vh.list_file_name.setText(cursor.getString(0));

                String price=form.format(cursor.getDouble(1));
                vh.list_file_price.setText("¥"+price);
                vh.list_file_price.setTextColor(Color.RED);
                String path=cursor.getString(2);

                image_adapter.setDrawable(path,vh.list_file_imag);

            }


            LinearLayout linearLayout=(LinearLayout) convertView.findViewById(R.id.pic_item_linearLayout);
            ViewGroup.LayoutParams lp;
            lp= linearLayout.getLayoutParams();
            lp.height=250;
            linearLayout.setLayoutParams(lp);


            return convertView;
        }
    }

    private String findCoverimage(File directory){
        File[] files=directory.listFiles();
        for(File file :files){
            if(file.getName().contains("cover")) return file.getAbsolutePath();
        }
        return directory.getAbsolutePath();
    }

    public class ViewH {
        ImageView list_file_imag;
        TextView list_file_name;
        TextView list_file_price;
    }



}