package com.xwj.xiamediaplayer.views;

import android.content.DialogInterface;

import com.xwj.xiamediaplayer.entitys.HistoryVideo;

import java.util.List;

/**
 * Created by xwjsd on 2016-05-05.
 */
public interface HistoryListView {

    void bindViews(List<HistoryVideo> historyVideoList);

    void updateRecyclerView();

    void clearAll();

    void showProgress();

    void hideProgress();

    void showClearAllDialog(DialogInterface.OnClickListener onPositivelistener, DialogInterface.OnClickListener onNegativelistener);
}
