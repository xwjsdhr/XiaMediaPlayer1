package com.xwj.xiamediaplayer.views.impl;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xwj.xiamediaplayer.R;
import com.xwj.xiamediaplayer.adapters.HistoryVideoAdapter;
import com.xwj.xiamediaplayer.entitys.HistoryVideo;
import com.xwj.xiamediaplayer.presenters.HistoryListPresenter;
import com.xwj.xiamediaplayer.presenters.impl.HistoryListPresenterImpl;
import com.xwj.xiamediaplayer.views.HistoryListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xwjsd on 2016-05-05.
 */
public class HistoryListActivity extends AppCompatActivity implements HistoryListView, HistoryVideoAdapter.HistoryListener {

    private RecyclerView mRvHistoryList;
    private List<HistoryVideo> list;
    private HistoryVideoAdapter historyVideoAdapter;
    private HistoryListPresenter historyListPresenter;
    private TextView mTvHistoryEmpty;
    private Toolbar toolbar;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyListPresenter = new HistoryListPresenterImpl(this, this);
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryListActivity.this.finish();
            }
        });
        mRvHistoryList = (RecyclerView) this.findViewById(R.id.rv_main_history_list);
        mTvHistoryEmpty = (TextView) this.findViewById(R.id.tv_history_empty);
        mProgress = (ProgressBar) this.findViewById(R.id.progress_history);

        list = new ArrayList<>();

        historyVideoAdapter = new HistoryVideoAdapter(this, list);
        historyVideoAdapter.setHistoryListener(this);
        mRvHistoryList.setLayoutManager(new LinearLayoutManager(this));
        mRvHistoryList.setAdapter(historyVideoAdapter);
        historyListPresenter.onStart();

    }

    @Override
    public void bindViews(List<HistoryVideo> historyVideoList) {
        list.addAll(historyVideoList);
        if (list != null && list.size() > 0) {
            mRvHistoryList.setVisibility(View.VISIBLE);
            mTvHistoryEmpty.setVisibility(View.GONE);
            historyVideoAdapter.notifyDataSetChanged();
            toolbar.setTitle(String.format("历史记录（%d）", list.size()));
        } else {
            mRvHistoryList.setVisibility(View.GONE);
            mTvHistoryEmpty.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void updateRecyclerView() {
        if (list.size() == 0) {
            mRvHistoryList.setVisibility(View.GONE);
            mTvHistoryEmpty.setVisibility(View.VISIBLE);
        } else {
            mRvHistoryList.setVisibility(View.VISIBLE);
            mTvHistoryEmpty.setVisibility(View.GONE);

            historyVideoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void clearAll() {
        if (list.isEmpty()) {
            Toast.makeText(this, "无任何历史记录", Toast.LENGTH_SHORT).show();
        } else {
            list.clear();
            updateRecyclerView();
        }
    }

    @Override
    public void showProgress() {
        mRvHistoryList.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mRvHistoryList.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void showClearAllDialog(DialogInterface.OnClickListener onPositivelistener, DialogInterface.OnClickListener onNegativelistener) {
        new AlertDialog
                .Builder(this)
                .setTitle("清空历史记录")
                .setMessage("确定清空历史记录？")
                .setPositiveButton("确定", onPositivelistener)
                .setNegativeButton("取消", onNegativelistener)
                .show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return historyListPresenter.onOptionsItemSelected(item);
    }

    @Override
    public void onHistoryClick(HistoryVideo historyVideo) {
        historyListPresenter.onHistoryClick(historyVideo);
    }
}
