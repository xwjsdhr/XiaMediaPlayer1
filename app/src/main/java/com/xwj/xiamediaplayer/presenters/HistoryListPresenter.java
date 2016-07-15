package com.xwj.xiamediaplayer.presenters;

import android.view.MenuItem;

import com.xwj.xiamediaplayer.entitys.HistoryVideo;


/**
 * Created by xwjsd on 2016-05-05.
 */
public interface HistoryListPresenter {
    void onStart();

    boolean onOptionsItemSelected(MenuItem item);

    void onHistoryClick(HistoryVideo historyVideo);
}
