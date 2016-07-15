package com.xwj.xiamediaplayer.interator;

import android.content.ContentValues;

import com.xwj.xiamediaplayer.listener.DeleteListener;
import com.xwj.xiamediaplayer.listener.UpdateListener;


/**
 * Created by xwjsd on 2016-05-04.
 */
public interface UpdateVideoItemInteractor {

    void delete(int id, DeleteListener listener);

    void update(int id, ContentValues contentValues, UpdateListener updateListener);
}
