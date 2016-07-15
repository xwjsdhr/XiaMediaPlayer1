package com.xwj.xiamediaplayer.dao.impl;

import android.content.Context;

import com.xwj.xiamediaplayer.dao.TimesDao;
import com.xwj.xiamediaplayer.utils.DbUtils;


/**
 * Created by xwjsd on 2016-05-08.
 */
public class TimesDaoImpl implements TimesDao {

    private DbUtils mDbUtils;

    public TimesDaoImpl(Context context) {

        mDbUtils = DbUtils.getInstance(context);
    }

    @Override
    public void autoIncrement(int id) {
        mDbUtils.autoIncrement(id);
    }

    @Override
    public int getTimesById(int id) {
        return mDbUtils.getTimeById(id);
    }
}
