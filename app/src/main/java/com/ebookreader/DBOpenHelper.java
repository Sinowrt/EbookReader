package com.ebookreader;

/**
 * Created by Jacky on 2017/11/14.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DataBase helper class
 * @author sinowrt
 * @date 2017-11-5 14:38:28
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "test.db";
    private static final int VERSION = 11;
    public DBOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);//it's location is data/data/pakage/database

    }

    @Override
    public void onCreate(SQLiteDatabase db) {//It will be called when the database was created first
        db.execSQL("CREATE TABLE IF NOT EXISTS exam_type (id integer primary key autoincrement, type_name varchar(100), type_id INTEGER)");


    }

    @Override  // It'll be called when the database was updated
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}