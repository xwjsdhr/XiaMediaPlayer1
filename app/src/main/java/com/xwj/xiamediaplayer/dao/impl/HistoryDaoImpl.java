package com.xwj.xiamediaplayer.dao.impl;

import android.content.Context;

import com.xwj.xiamediaplayer.dao.HistoryDao;
import com.xwj.xiamediaplayer.entitys.HistoryVideo;
import com.xwj.xiamediaplayer.utils.DbUtils;

import java.util.List;

/**
 * Created by xwjsd on 2016-05-05.
 */
public class HistoryDaoImpl implements HistoryDao {

    private DbUtils mDbUtils;

    public HistoryDaoImpl(Context context) {
        mDbUtils = DbUtils.getInstance(context);
    }

    @Override
    public List<HistoryVideo> getAllHistory() {
        return mDbUtils.getAllHistory();
    }

    @Override
    public boolean isExist(int id) {
        return mDbUtils.hasItById(id);
    }

    @Override
    public long insert(HistoryVideo video) {
        return mDbUtils.insert(video);
    }

    @Override
    public long update(HistoryVideo video) {
        return mDbUtils.update(video);
    }

    @Override
    public void clearAll() {
        mDbUtils.clearAll();
    }


}
