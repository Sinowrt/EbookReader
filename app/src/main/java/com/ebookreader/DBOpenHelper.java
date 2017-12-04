package com.ebookreader;

/**
 * Created by Jacky on 2017/11/14.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DataBase helper class
 * @author sinowrt
 * @date 2017-11-5 14:38:28
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "bookstoreInfo.db";
    private static final int VERSION = 11;
    public DBOpenHelper(Context context) {
        super(new DatabaseContext(context), DBNAME, null, VERSION);//it's location is data/data/pakage/database

    }

    @Override
    public void onCreate(SQLiteDatabase db) {//It will be called when the database was created first
        /*db.execSQL("CREATE TABLE IF NOT EXISTS books (id integer primary key autoincrement, type_name varchar(100), type_id INTEGER)");*/


    }

    @Override  // It'll be called when the database was updated
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 添加数据
     * @param nullColumnHack 空列的默认值
     * @param values ContentValues类型的一个封装了列名称和列值的Map
     * @return
     */
    public long insert(String table_name, String nullColumnHack, ContentValues values) {

        SQLiteDatabase wdb = getWritableDatabase();
        return wdb.insert(table_name, nullColumnHack, values);
    }

    /**
     * 更新数据
     * @param values 更行列ContentValues类型的键值对（Map）
     * @param whereClause 更新条件（where字句）
     * @param whereArgs 更新条件数组
     * @return
     */
    public int update(String table,ContentValues values, String whereClause, String[] whereArgs)
    {
        SQLiteDatabase wdb = getWritableDatabase();
        return wdb.update(table, values, whereClause, whereArgs);
    }

    /**
     * 删除数据
     * @param whereClause 删除条件
     * @param whereArgs  删除条件值数组
     * @return
     */
    public int delete(String table,String whereClause, String[] whereArgs) {

        SQLiteDatabase wdb = getWritableDatabase();
        return wdb.delete(table, whereClause, whereArgs);
    }

    /**
     * 查询数据
     * @param columns 要查询的字段
     * @param selection 要查询的条件
     * @param selectionArgs 要查询的条件中占位符的值
     * @param groupBy 对查询的结果进行分组
     * @param having 对分组的结果进行限制，分组条件
     * @param orderBy 对查询的结果进行排序（“id desc”表示根据id倒序）
     * @param limit 分页查询限制（如”1,3”表示获取第1到第3的数据共3条，
     *                            如“2”表示获取两条数据）
     * @return
     */
    public Cursor query(String tableName,String[] columns, String selection, String[] selectionArgs,
                        String groupBy, String having, String orderBy, String limit) {

        SQLiteDatabase rdb = getReadableDatabase();
        return rdb.query(tableName, columns, selection, selectionArgs, groupBy,
                having, orderBy, limit);
    }

    /**
     * Cursor游标接口常用方法：
     * getCount()   总记录条数
     * isFirst()     判断是否第一条记录
     * isLast()      判断是否最后一条记录
     * moveToFirst()    移动到第一条记录
     * moveToLast()    移动到最后一条记录
     * move(int offset)   移动到指定记录
     * moveToNext()    移动到下一条记录
     * moveToPrevious()    移动到上一条记录
     * getColumnIndex(String columnName) 根据列名得到列位置id
     * getColumnIndexOrThrow(String columnName)  根据列名称获得列索引
     * getInt(int columnIndex)   获得指定列索引的int类型值
     * getString(int columnIndex)   获得指定列索引的String类型值
     */

}