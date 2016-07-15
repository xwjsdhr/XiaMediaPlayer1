package com.xwj.xiamediaplayer.interator.impl;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.xwj.xiamediaplayer.interator.UpdateVideoItemInteractor;
import com.xwj.xiamediaplayer.listener.DeleteListener;
import com.xwj.xiamediaplayer.listener.UpdateListener;


/**
 * Created by xwjsd on 2016-05-04.
 */
public class UpdateVideoInteractorImpl implements UpdateVideoItemInteractor {
    private Context mContext;

    private ContentResolver mContentResolver;

    public UpdateVideoInteractorImpl(Context context) {
        this.mContext = context;
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void delete(int id, final DeleteListener listener) {
        new AsyncTask<Integer, Void, Integer>() {
            @Override
            protected Integer doInBackground(Integer... params) {
                int id = params[0];
                String where = MediaStore.Video.Media._ID + " = ?";
                int count = mContentResolver.delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, where,
                        new String[]{id + ""});
                return count;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                if (integer > 0) {
                    listener.onDeleteSuccess();
                } else {
                    listener.onDeleteFailed("删除失败");
                }
            }
        }.execute(id);

    }

    @Override
    public void update(int id, final ContentValues contentValues, final UpdateListener updateListener) {
        new AsyncTask<Integer, Void, Integer>() {

            @Override
            protected Integer doInBackground(Integer... params) {
                int id = params[0];
                String where = MediaStore.Video.Media._ID + " = ?";
                int count = mContentResolver.update(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues, where,
                        new String[]{id + ""});
                return count;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                if (integer > 0) {
                    updateListener.onUpdateSuccess();
                } else {
                    updateListener.onUpdateFailed();
                }
            }
        }.execute(id);
    }
}
