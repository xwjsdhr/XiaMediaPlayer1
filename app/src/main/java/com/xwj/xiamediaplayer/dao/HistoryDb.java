package com.xwj.xiamediaplayer.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xwj.xiamediaplayer.utils.Constant;


/**
 * Created by xwjsd on 2016-05-05.
 */
public class HistoryDb extends SQLiteOpenHelper {


    public HistoryDb(Context context) {
        super(context, Constant.HISTORY_DB_NAME, null, 2);
    }

    public static final String COLUMN_ID_INT = "COLUMN_ID_INT";
    public static final String COLUMN_URL_STR = "COLUMN_URL_STR";
    public static final String COLUMN_NAME_STR = "COLUMN_NAME_STR";
    public static final String COLUMN_DURATION_LONG = "COLUMN_DURATION_LONG";
    public static final String COLUMN_SIZE_LONG = "COLUMN_SIZE_LONG";
    public static final String COLUMN_DATE_LONG = "COLUMN_DATE_LONG";
    public static final String COLUMN_PLAYFINISH_INT = "COLUMN_PLAYFINISH_INT";
    public static final String COLUMN_PLAYPOS_INT = "COLUMN_PLAYPOS_INT";
    public static final String COLUMN_START_PLAY_LONG = "COLUMN_START_PLAY_LONG";

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableSql = "CREATE TABLE " + Constant.HISTORY_TABLE_NAME + "(" +
                COLUMN_ID_INT + " INT PRIMARY KEY ," +
                COLUMN_URL_STR + " text not null ," +
                COLUMN_NAME_STR + " text not null ," +
                COLUMN_DURATION_LONG + " Long not null ," +
                COLUMN_SIZE_LONG + " Long not null ," +
                COLUMN_DATE_LONG + " Long not null ," +
                COLUMN_START_PLAY_LONG + " Long not null ," +
                COLUMN_PLAYFINISH_INT + " int not null ," +
                COLUMN_PLAYPOS_INT + " INT " +
                ");";
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
