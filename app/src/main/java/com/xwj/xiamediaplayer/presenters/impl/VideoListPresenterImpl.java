package com.xwj.xiamediaplayer.presenters.impl;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.xwj.xiamediaplayer.R;
import com.xwj.xiamediaplayer.entitys.VideoItem;
import com.xwj.xiamediaplayer.interator.QueryListener;
import com.xwj.xiamediaplayer.interator.QueryVideoInteractor;
import com.xwj.xiamediaplayer.interator.UpdateVideoItemInteractor;
import com.xwj.xiamediaplayer.interator.impl.QueryVideoInteractorImpl;
import com.xwj.xiamediaplayer.interator.impl.UpdateVideoInteractorImpl;
import com.xwj.xiamediaplayer.listener.DeleteListener;
import com.xwj.xiamediaplayer.listener.UpdateListener;
import com.xwj.xiamediaplayer.presenters.VideoListPresenter;
import com.xwj.xiamediaplayer.utils.CommonUtils;
import com.xwj.xiamediaplayer.utils.Constant;
import com.xwj.xiamediaplayer.utils.PreferenceUtils;
import com.xwj.xiamediaplayer.views.VideoListView;
import com.xwj.xiamediaplayer.views.impl.VideoPlayActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频列表的业务逻辑类
 * Created by xiaweijia on 16/3/15.
 */
public class VideoListPresenterImpl implements VideoListPresenter {
    private static final String TAG = VideoListPresenterImpl.class.getSimpleName();
    private Context mContext;
    private VideoListView mVideoListView;
    private QueryVideoInteractor queryVideoInteractor;
    private ArrayList<VideoItem> mList;
    ArrayAdapter<String> mStringArrayAdapter;
    private ContentResolver mContentResolver;
    private UpdateVideoItemInteractor mUpdateVideoItemInteractor;
    private SharedPreferences mSharedPreferences;
    private int showWayInt = Constant.SHOW_LARGE_IMAGE;

    //    private View mContentView;
//
//    private Dialog mDialog;
    public VideoListPresenterImpl(Context context, VideoListView videoListView) {

        mList = new ArrayList<>();
        this.mContext = context;
        this.mVideoListView = videoListView;
        queryVideoInteractor = new QueryVideoInteractorImpl(context);
        mStringArrayAdapter = new ArrayAdapter<String>(mContext, R.layout.dialog_adapter, R.id.tv_item_dialog,
                new String[]{
                        "删除", "播放", "重命名", "详细信息"
                });
//        mContentView = View.inflate(mContext,R.layout.dialog_rename,null);
//        mDialog = new Dialog(mContext);
//        mDialog.setContentView(mContentView,
//                new LinearLayout.LayoutParams(800, ViewGroup.LayoutParams.WRAP_CONTENT));


        mContentResolver = mContext.getContentResolver();
        mUpdateVideoItemInteractor = new UpdateVideoInteractorImpl(context);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String showWay = mSharedPreferences.getString(Constant.SETTING_SHOW_WAYS, "");
        if (showWay.equals("si")) {
            showWayInt = Constant.SHOW_SMALL_IMAGE;
        } else if (showWay.equals("li")) {
            showWayInt = Constant.SHOW_LARGE_IMAGE;
        } else {
            showWayInt = Constant.SHOW_LARGE_IMAGE;
        }
    }

    @Override
    public void onStart() {
        mVideoListView.showProgress();

        queryVideoInteractor.queryNormal(new QueryListener<VideoItem>() {


            @Override
            public void onSuccess(List<VideoItem> list) {
                mVideoListView.setAdapter(showWayInt);
                mList.addAll(list);
                mVideoListView.bindViews(list);
                mVideoListView.hideProgress();
            }

            @Override
            public void onFailed(String msg) {
                mVideoListView.hideProgress();
            }
        });
    }

    @Override
    public void onDestroy() {
        //queryVideoInteractor.stop();
    }

    @Override
    public void onVideoItemClick(VideoItem videoItem) {
        play(videoItem);
    }

    /**
     * 播放视频
     *
     * @param videoItem
     */
    private void play(VideoItem videoItem) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.VIDEO_ITEM, videoItem);
        bundle.putSerializable(Constant.VIDEO_LIST, mList);
        bundle.putInt(Constant.VIDEO_POSITION, mList.indexOf(videoItem));
        Intent intent = new Intent(mContext, VideoPlayActivity.class);
        intent.putExtra(Constant.BUNDLE_NAME, bundle);
        mContext.startActivity(intent);
    }

    @Override
    public boolean onVideoItemLongClick(final List<VideoItem> list, final VideoItem videoItem) {

        showMenu(list, videoItem);
        return true;
    }

    private void showMenu(final List<VideoItem> list, final VideoItem videoItem) {
        DialogPlus dialog = DialogPlus.newDialog(mContext)
                .setAdapter(mStringArrayAdapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(final DialogPlus dialog, Object item, View view, int position) {
                        switch (position) {
                            case 0:
                                mUpdateVideoItemInteractor.delete(videoItem.getId(), new DeleteListener() {
                                    @Override
                                    public void onDeleteSuccess() {
                                        int index = list.indexOf(videoItem);
                                        mVideoListView.delete(index);
                                    }

                                    @Override
                                    public void onDeleteFailed(String msg) {
                                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.dismiss();
                                break;
                            case 1:
                                play(videoItem);
                                dialog.dismiss();
                                break;
                            case 2:
                                mVideoListView.bindDialog(videoItem);
                                dialog.dismiss();
                                break;
                            case 3:
                                mVideoListView.bindVideoInfoDialog(videoItem);
                                dialog.dismiss();
                                break;
                        }
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_name:
                mVideoListView.sortAndBindView(Constant.SORT_BY_NAME);
                break;
            case R.id.sort_by_time:
                mVideoListView.sortAndBindView(Constant.SORT_BY_TIME);
                break;
            case R.id.sort_by_size:
                mVideoListView.sortAndBindView(Constant.SORT_BY_SIZE);
                break;
            case R.id.sort_by_duration:
                mVideoListView.sortAndBindView(Constant.SORT_BY_DURATION);
                break;
            case R.id.sort_ways:
                int pos = PreferenceUtils.getInt(mContext, Constant.SORT_WAY_POS);
                mVideoListView.showSortWayDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                mVideoListView.sortAndBindView(Constant.SORT_BY_NAME);
                                PreferenceUtils.putInt(mContext, Constant.SORT_WAY_POS, 0);
                                dialog.dismiss();
                                break;
                            case 1:
                                mVideoListView.sortAndBindView(Constant.SORT_BY_TIME);
                                PreferenceUtils.putInt(mContext, Constant.SORT_WAY_POS, 1);
                                dialog.dismiss();
                                break;
                            case 2:
                                mVideoListView.sortAndBindView(Constant.SORT_BY_SIZE);
                                PreferenceUtils.putInt(mContext, Constant.SORT_WAY_POS, 2);
                                dialog.dismiss();
                                break;
                            case 3:
                                mVideoListView.sortAndBindView(Constant.SORT_BY_DURATION);
                                PreferenceUtils.putInt(mContext, Constant.SORT_WAY_POS, 3);
                                dialog.dismiss();
                                break;
                        }
                    }
                }, pos);
                break;
        }
        return true;
    }

    @Override
    public boolean onQuerySubmit(String query) {
        queryVideoInteractor.queryByName(new QueryListener() {
            @Override
            public void onSuccess(List list) {
                mVideoListView.bindViews(list);
            }

            @Override
            public void onFailed(String msg) {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        }, query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public boolean onClose() {
        queryVideoInteractor.queryNormal(new QueryListener() {
            @Override
            public void onSuccess(List list) {
                mVideoListView.bindViews(list);
            }

            @Override
            public void onFailed(String msg) {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            queryVideoInteractor.queryNormal(new QueryListener() {
                @Override
                public void onSuccess(List list) {
                    mVideoListView.bindViews(list);
                }

                @Override
                public void onFailed(String msg) {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dialog_rename_ok:
                final VideoItem videoItem = (VideoItem) v.getTag();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, mVideoListView.getChangeFileName());
                mUpdateVideoItemInteractor.update(videoItem.getId(), contentValues,
                        new UpdateListener() {
                            @Override
                            public void onUpdateSuccess() {
                                mVideoListView.getDialog().dismiss();
                                int i = mList.indexOf(videoItem);
                                videoItem.setVideoName(mVideoListView.getChangeFileName());
                                mVideoListView.update(i, videoItem);
                            }

                            @Override
                            public void onUpdateFailed() {
                                CommonUtils.toast(mContext, "修改失败");
                                mVideoListView.getDialog().dismiss();
                            }
                        });
                break;
            case R.id.btn_dialog_rename_cancel:
                mVideoListView.getDialog().dismiss();
                break;
        }
    }

    @Override
    public void onMenuBtnClick(List<VideoItem> list, VideoItem videoItem) {
        showMenu(list, videoItem);
    }

    @Override
    public void onRefresh() {
        queryVideoInteractor.queryNormal(new QueryListener() {
            @Override
            public void onSuccess(List list) {
                if (list != null && list.size() > 0) {
                    mVideoListView.refresh(list);
                    mVideoListView.hideRefresh();
                } else {
                    mVideoListView.showEmptyView();
                }
            }

            @Override
            public void onFailed(String msg) {

            }
        });

    }
}
