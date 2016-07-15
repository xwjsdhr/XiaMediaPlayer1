package com.xwj.xiamediaplayer.presenters;

import android.view.MenuItem;
import android.view.View;

import com.xwj.xiamediaplayer.entitys.VideoItem;

import java.util.List;

/**
 * Created by xiaweijia on 16/3/15.
 */
public interface VideoListPresenter {
    void onStart();

    void onDestroy();

    void onVideoItemClick(VideoItem videoItem);

    boolean onVideoItemLongClick(List<VideoItem> list, VideoItem videoItem);

    boolean onOptionsItemSelected(MenuItem item);

    boolean onQuerySubmit(String query);

    boolean onQueryTextChange(String newText);

    boolean onClose();

    void onFocusChange(View v, boolean hasFocus);

    void onClick(View v);

    void onMenuBtnClick(List<VideoItem> list, VideoItem videoItem);

    void onRefresh();
}
