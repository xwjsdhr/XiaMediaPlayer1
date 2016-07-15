package com.xwj.xiamediaplayer.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xwj.xiamediaplayer.utils.Constant;


/**
 * Created by xwjsd on 2016-05-08.
 */
public class TimesDb extends SQLiteOpenHelper {

    public static final String COLUMN_TIME = "COLUMN_TIME";
    public static final String COLUMN_ID = "COLUMN_ID";

    public TimesDb(Context context) {
        super(context, Constant.TIMES_DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + Constant.TIMES_TABLE_NAME + "( " +
                COLUMN_ID + " int primary key , " +
                COLUMN_TIME + " int not null )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
