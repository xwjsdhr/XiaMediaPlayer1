package com.xwj.xiamediaplayer.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.xwj.xiamediaplayer.dao.HistoryDb;
import com.xwj.xiamediaplayer.dao.TimesDb;
import com.xwj.xiamediaplayer.entitys.HistoryVideo;
import com.xwj.xiamediaplayer.entitys.VideoItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xwjsd on 2016-05-05.
 */
public class DbUtils {

    private static DbUtils instance = null;

    private static HistoryDb historyDb = null;
    private static TimesDb timesDb = null;

    public static DbUtils getInstance(Context context) {
        if (instance == null) {
            instance = new DbUtils();
            historyDb = new HistoryDb(context);
            timesDb = new TimesDb(context);
        }
        return instance;
    }

    private DbUtils() {
    }

    public List<HistoryVideo> getAllHistory() {
        List<HistoryVideo> list = new ArrayList<>();
        SQLiteDatabase readableDatabase
                = historyDb.getReadableDatabase();
        Cursor cursor
                = readableDatabase
                .query(Constant.HISTORY_TABLE_NAME, null, null, null, null, null, HistoryDb.COLUMN_START_PLAY_LONG + " desc");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(HistoryDb.COLUMN_ID_INT));
            String videoName = cursor.getString(cursor.getColumnIndex(HistoryDb.COLUMN_NAME_STR));
            String dataUrl = cursor.getString(cursor.getColumnIndex(HistoryDb.COLUMN_URL_STR));
            long duration = cursor.getLong(cursor.getColumnIndex(HistoryDb.COLUMN_DURATION_LONG));
            long size = cursor.getLong(cursor.getColumnIndex(HistoryDb.COLUMN_SIZE_LONG));
            long datalong = cursor.getLong(cursor.getColumnIndex(HistoryDb.COLUMN_DATE_LONG));
            long startPlayTime = cursor.getLong(cursor.getColumnIndex(HistoryDb.COLUMN_START_PLAY_LONG));
            int playfinish = cursor.getInt(cursor.getColumnIndex(HistoryDb.COLUMN_PLAYFINISH_INT));
            int playpot = cursor.getInt(cursor.getColumnIndex(HistoryDb.COLUMN_PLAYPOS_INT));
            HistoryVideo historyVideo = new HistoryVideo();
            SystemClock.sleep(500);

            VideoItem videoItem = new VideoItem();
            videoItem.setId(id);
            videoItem.setVideoName(videoName);
            videoItem.setDataUrl(dataUrl);
            videoItem.setVideoDuration(duration);
            videoItem.setSize(size);
            videoItem.setDateTaken(new Date(datalong));

            historyVideo.setVideoItem(videoItem);
            historyVideo.setPlayFinished(playfinish == 1);
            historyVideo.setPlayPos(playpot);
            historyVideo.setStartPlayTime(new Date(startPlayTime));
            list.add(historyVideo);
        }
        cursor.close();
        readableDatabase.close();
        return list;
    }

    public long insert(HistoryVideo video) {
        SQLiteDatabase readableDatabase = historyDb.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HistoryDb.COLUMN_ID_INT, video.getVideoItem().getId());
        contentValues.put(HistoryDb.COLUMN_NAME_STR, video.getVideoItem().getVideoName());
        contentValues.put(HistoryDb.COLUMN_URL_STR, video.getVideoItem().getDataUrl());
        contentValues.put(HistoryDb.COLUMN_DURATION_LONG, video.getVideoItem().getVideoDuration());
        contentValues.put(HistoryDb.COLUMN_SIZE_LONG, video.getVideoItem().getSize());
        contentValues.put(HistoryDb.COLUMN_DATE_LONG, video.getVideoItem().getDateTaken().getTime());
        contentValues.put(HistoryDb.COLUMN_PLAYFINISH_INT, video.isPlayFinished() ? 1 : 0);
        contentValues.put(HistoryDb.COLUMN_START_PLAY_LONG, video.getStartPlayTime().getTime());
        contentValues.put(HistoryDb.COLUMN_PLAYPOS_INT, video.getPlayPos());
        long res = readableDatabase.insert(Constant.HISTORY_TABLE_NAME, null, contentValues);
        readableDatabase.close();
        return res;
    }

    public boolean hasItById(int id) {
        SQLiteDatabase readableDatabase = historyDb.getReadableDatabase();
        String selectSql = "select * from " + Constant.HISTORY_TABLE_NAME +
                " where " + HistoryDb.COLUMN_ID_INT + " = ?";
        Cursor cursor = readableDatabase.rawQuery(selectSql, new String[]{id + ""});
        boolean b = cursor.moveToNext();
        cursor.close();
        return b;
    }

    public long update(HistoryVideo video) {
        SQLiteDatabase readableDatabase = historyDb.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HistoryDb.COLUMN_ID_INT, video.getVideoItem().getId());
        contentValues.put(HistoryDb.COLUMN_NAME_STR, video.getVideoItem().getVideoName());
        contentValues.put(HistoryDb.COLUMN_URL_STR, video.getVideoItem().getDataUrl());
        contentValues.put(HistoryDb.COLUMN_DURATION_LONG, video.getVideoItem().getVideoDuration());
        contentValues.put(HistoryDb.COLUMN_SIZE_LONG, video.getVideoItem().getSize());
        contentValues.put(HistoryDb.COLUMN_DATE_LONG, video.getVideoItem().getDateTaken().getTime());
        contentValues.put(HistoryDb.COLUMN_PLAYFINISH_INT, video.isPlayFinished() ? 1 : 0);
        contentValues.put(HistoryDb.COLUMN_START_PLAY_LONG, video.getStartPlayTime().getTime());
        contentValues.put(HistoryDb.COLUMN_PLAYPOS_INT, video.getPlayPos());
        int res = readableDatabase.update(Constant.HISTORY_TABLE_NAME, contentValues, HistoryDb.COLUMN_ID_INT + " = ?",
                new String[]{video.getVideoItem().getId() + ""});
        readableDatabase.close();
        return res;
    }

    public void clearAll() {
        SQLiteDatabase readableDatabase = historyDb.getReadableDatabase();
        readableDatabase.delete(Constant.HISTORY_TABLE_NAME, null, null);
        readableDatabase.close();
    }

    public boolean autoIncrement(int id) {
        SQLiteDatabase readableDatabase = timesDb.getReadableDatabase();
        String querySql = "select * from " +
                Constant.TIMES_TABLE_NAME + " where " + TimesDb.COLUMN_ID + " = ?";
        Cursor cursor
                = readableDatabase.rawQuery(querySql, new String[]{id + ""});
        int res = 0;
        if (cursor.moveToNext()) {
            res = cursor.getInt(cursor.getColumnIndex(TimesDb.COLUMN_TIME));
        }
        res++;
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(TimesDb.COLUMN_TIME, res);
        String where = TimesDb.COLUMN_ID + " = ?";
        String[] args = new String[]{id + ""};
        int count = readableDatabase.update(Constant.TIMES_TABLE_NAME, values, where, args);
        readableDatabase.close();
        return count > 0;
    }

    public int getTimeById(int id) {
        SQLiteDatabase readableDatabase = timesDb.getReadableDatabase();
        String querySql = "select * from " + Constant.TIMES_TABLE_NAME + " where " + TimesDb.COLUMN_ID + " = ?";
        Cursor cursor = readableDatabase.rawQuery(querySql, new String[]{id + ""});
        int res = 0;
        if (cursor.moveToNext()) {
            res = cursor.getInt(cursor.getColumnIndex(TimesDb.COLUMN_TIME));
        }
        cursor.close();
        timesDb.close();
        return res;
    }
}
