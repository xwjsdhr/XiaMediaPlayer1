package com.xwj.xiamediaplayer.presenters.impl;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.xwj.xiamediaplayer.R;
import com.xwj.xiamediaplayer.dao.HistoryDao;
import com.xwj.xiamediaplayer.dao.impl.HistoryDaoImpl;
import com.xwj.xiamediaplayer.entitys.HistoryVideo;
import com.xwj.xiamediaplayer.interator.QueryListener;
import com.xwj.xiamediaplayer.interator.QueryVideoInteractor;
import com.xwj.xiamediaplayer.interator.impl.QueryVideoInteractorImpl;
import com.xwj.xiamediaplayer.presenters.HistoryListPresenter;
import com.xwj.xiamediaplayer.utils.Constant;
import com.xwj.xiamediaplayer.views.HistoryListView;
import com.xwj.xiamediaplayer.views.impl.VideoPlayActivity;

import java.util.List;

/**
 * Created by xwjsd on 2016-05-05.
 */
public class HistoryListPresenterImpl implements HistoryListPresenter {

    private Context mContext;
    private HistoryListView mHistoryListView;
    private QueryVideoInteractor queryVideoInteractor;
    private HistoryDao historyDao;

    public HistoryListPresenterImpl(Context context, HistoryListView historyListView) {
        this.mContext = context;
        this.mHistoryListView = historyListView;
        queryVideoInteractor = new QueryVideoInteractorImpl(context);
        historyDao = new HistoryDaoImpl(context);
    }

    @Override
    public void onStart() {
        mHistoryListView.showProgress();
        queryVideoInteractor.queryHistoryFromDb(new QueryListener<HistoryVideo>() {

            @Override
            public void onSuccess(List<HistoryVideo> list) {
                mHistoryListView.bindViews(list);
                mHistoryListView.hideProgress();
            }

            @Override
            public void onFailed(String msg) {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                mHistoryListView.hideProgress();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear_history:
                mHistoryListView.showClearAllDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        historyDao.clearAll();
                        mHistoryListView.clearAll();
                        dialog.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                break;
        }
        return true;
    }

    @Override
    public void onHistoryClick(HistoryVideo historyVideo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.HISTORY_VIDEO, historyVideo);
        Intent intent = new Intent(mContext, VideoPlayActivity.class);
        intent.putExtra(Constant.BUNDLE_NAME, bundle);
        mContext.startActivity(intent);
    }
}
