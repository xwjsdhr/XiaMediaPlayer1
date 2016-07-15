package com.xwj.xiamediaplayer.views;

import android.app.Dialog;
import android.content.DialogInterface;

import com.xwj.xiamediaplayer.entitys.VideoItem;

import java.util.List;

/**
 * Created by xiaweijia on 16/3/15.
 */
public interface VideoListView extends BaseView<List<VideoItem>> {

    void showProgress();

    void hideProgress();

    void sortAndBindView(int sortType);

    void delete(int index);

    void setAdapter(int showType);

    void bindDialog(VideoItem videoItem);

    Dialog getDialog();

    String getChangeFileName();

    void bindVideoInfoDialog(VideoItem videoItem);

    void update(int index, VideoItem videoItem);

    void showSortWayDialog(DialogInterface.OnClickListener onClickListener, int position);

    void refresh(List<VideoItem> list);

    void hideRefresh();

    void showEmptyView();
}
