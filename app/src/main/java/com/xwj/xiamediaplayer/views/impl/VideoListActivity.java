package com.xwj.xiamediaplayer.views.impl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.promeg.pinyinhelper.Pinyin;
import com.xwj.xiamediaplayer.R;
import com.xwj.xiamediaplayer.adapters.VideoAdapter;
import com.xwj.xiamediaplayer.entitys.VideoItem;
import com.xwj.xiamediaplayer.presenters.VideoListPresenter;
import com.xwj.xiamediaplayer.presenters.impl.VideoListPresenterImpl;
import com.xwj.xiamediaplayer.utils.CommonUtils;
import com.xwj.xiamediaplayer.utils.Constant;
import com.xwj.xiamediaplayer.views.VideoListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


/**
 * 视频列表界面
 */
public class VideoListActivity extends AppCompatActivity implements VideoListView,
        VideoAdapter.OnVideoItemClickListener,
        VideoAdapter.OnVideoItemLongClickListener,
        SearchView.OnQueryTextListener,
        SearchView.OnCloseListener,
        View.OnFocusChangeListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    private static final float TOOLBAR_ELEVATION = 3.0f;
    private VideoAdapter mVideoAdapter;
    private List<VideoItem> videoItemList;
    private RecyclerView mRvVideoList;
    private ProgressBar mProgressBar;
    private VideoListPresenter videoListPresenter;
    private Toolbar tToolbar;
    private SearchView mSearchView;
    private Dialog mDialog, mVideoInfoDialog;
    private View mDialogContentView;
    private EditText mEtDialogFileName;
    private Button mBtnDialogOk, mBtnDialogCancel;
    private View mVideoInfoContentView;
    private TextView mTvDialogInfoName, mTvDialogInfoLocation,
            mTvDialogInfoSize, mTvDialogInfoDuration,
            mTvDialogInfoAddTime, mTvDialogInfoModifiedTime, mTvDialogInfoPlayTimes;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        videoItemList = new ArrayList<>();
        initViews();
        videoListPresenter = new VideoListPresenterImpl(this, this);
        videoListPresenter.onStart();
    }

    private void initViews() {
        initDialog();
        tToolbar = (Toolbar) this.findViewById(R.id.toolbar);
        this.setSupportActionBar(tToolbar);
        tToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        tToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoListActivity.this.finish();
            }
        });
        mProgressBar = (ProgressBar) this.findViewById(R.id.progress);
        mRvVideoList = (RecyclerView) this.findViewById(R.id.rv_main_video_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.srl_video_list);
        mRvVideoList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int verticalOffset;
            boolean scrollingUp;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (scrollingUp) {
                        if (verticalOffset > tToolbar.getHeight()) {
                            toolbarAnimateHide();
                        } else {
                            toolbarAnimateShow(verticalOffset);
                        }
                    } else {
                        if (tToolbar.getTranslationY() < tToolbar.getHeight() * -0.6 && verticalOffset > tToolbar.getHeight()) {
                            toolbarAnimateHide();
                        } else {
                            toolbarAnimateShow(verticalOffset);
                        }
                    }
                }
            }

            @Override
            public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                verticalOffset += dy;
                scrollingUp = dy > 0;
                int toolbarYOffset = (int) (dy - tToolbar.getTranslationY());
                tToolbar.animate().cancel();
                if (scrollingUp) {
                    if (toolbarYOffset < tToolbar.getHeight()) {
                        if (verticalOffset > tToolbar.getHeight()) {
                            toolbarSetElevation(TOOLBAR_ELEVATION);
                        }
                        tToolbar.setTranslationY(-toolbarYOffset);
                    } else {
                        toolbarSetElevation(0);
                        tToolbar.setTranslationY(-tToolbar.getHeight());
                    }
                } else {
                    if (toolbarYOffset < 0) {
                        if (verticalOffset <= 0) {
                            toolbarSetElevation(0);
                        }
                        tToolbar.setTranslationY(0);
                    } else {
                        if (verticalOffset > tToolbar.getHeight()) {
                            toolbarSetElevation(TOOLBAR_ELEVATION);
                        }
                        tToolbar.setTranslationY(-toolbarYOffset);
                    }
                }
            }
        });
    }

    @Override
    public void bindViews(List<VideoItem> videoItems) {
        videoItemList.clear();
        videoItemList.addAll(videoItems);
        if (videoItemList != null && videoItemList.size() > 0) {
            tToolbar.setTitle(String.format("视频列表（%d）", videoItemList.size()));
            mVideoAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void showProgress() {
        mRvVideoList.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mRvVideoList.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void sortAndBindView(int sortType) {
        switch (sortType) {
            case Constant.SORT_BY_NAME:
                Collections.sort(videoItemList, new Comparator<VideoItem>() {
                    @Override
                    public int compare(VideoItem lhs, VideoItem rhs) {
                        if (Pinyin.isChinese(lhs.getInitialsOfVideoName())) {
                            if (Pinyin.isChinese(rhs.getInitialsOfVideoName())) {
                                return ((Character) Pinyin.toPinyin(lhs.getInitialsOfVideoName()).charAt(0))
                                        .compareTo(((Character) Pinyin.toPinyin(rhs.getInitialsOfVideoName()).charAt(0)));
                            } else {
                                return ((Character) Pinyin.toPinyin(lhs.getInitialsOfVideoName()).charAt(0))
                                        .compareTo(rhs.getInitialsOfVideoName());
                            }
                        } else {
                            if (Pinyin.isChinese(rhs.getInitialsOfVideoName())) {
                                return lhs.getInitialsOfVideoName()
                                        .compareTo(((Character) Pinyin.toPinyin(rhs.getInitialsOfVideoName()).charAt(0)));
                            } else {
                                return lhs.getInitialsOfVideoName().compareTo(rhs.getInitialsOfVideoName());
                            }
                        }
                    }
                });
                mVideoAdapter.notifyDataSetChanged();
                tToolbar.setTitle("视频列表（按名称排序）");
                break;
            case Constant.SORT_BY_TIME:
                Collections.sort(videoItemList, new Comparator<VideoItem>() {
                    @Override
                    public int compare(VideoItem lhs, VideoItem rhs) {
                        return lhs.getDateTaken().compareTo(rhs.getDateTaken());
                    }
                });
                mVideoAdapter.notifyDataSetChanged();
                tToolbar.setTitle("视频列表（按时间排序）");
                break;

            case Constant.SORT_BY_SIZE:
                Collections.sort(videoItemList, new Comparator<VideoItem>() {
                    @Override
                    public int compare(VideoItem lhs, VideoItem rhs) {
                        return lhs.getSize().compareTo(rhs.getSize());
                    }
                });
                mVideoAdapter.notifyDataSetChanged();
                tToolbar.setTitle("视频列表（按文件大小排序）");
                break;
            case Constant.SORT_BY_DURATION:
                Collections.sort(videoItemList, new Comparator<VideoItem>() {
                    @Override
                    public int compare(VideoItem lhs, VideoItem rhs) {
                        return lhs.getVideoDuration().compareTo(rhs.getVideoDuration());
                    }
                });
                mVideoAdapter.notifyDataSetChanged();
                tToolbar.setTitle("视频列表（按视频长度排序）");
                break;
            case Constant.SORT_BY_PLAY_TIMES:
                Collections.sort(videoItemList, new Comparator<VideoItem>() {
                    @Override
                    public int compare(VideoItem lhs, VideoItem rhs) {
                        Integer l = lhs.getPlayTimes();
                        Integer r = rhs.getPlayTimes();
                        return l.compareTo(r);
                    }
                });
                mVideoAdapter.notifyDataSetChanged();
                tToolbar.setTitle("视频列表（按播放次数排序）");
                break;
        }
    }

    @Override
    public void delete(int index) {
        videoItemList.remove(index);
        mVideoAdapter.notifyItemRemoved(index);
    }

    @Override
    public void setAdapter(int showType) {
        switch (showType) {
            case Constant.SHOW_SMALL_IMAGE:
                mVideoAdapter = new VideoAdapter(this, videoItemList, R.layout.video_item_small_image);
                break;
            case Constant.SHOW_LARGE_IMAGE:
                mVideoAdapter = new VideoAdapter(this, videoItemList, R.layout.video_item);
                break;
            default:
                mVideoAdapter = new VideoAdapter(this, videoItemList, R.layout.video_item);
                break;
        }

        mVideoAdapter.setOnVideoItemClickListener(this);
        mVideoAdapter.setOnVideoItemLongClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        if (mVideoAdapter != null) {
            mRvVideoList.setLayoutManager(new LinearLayoutManager(this));
            mRvVideoList.setHasFixedSize(true);
            mRvVideoList.setAdapter(mVideoAdapter);
        }
    }

    public void initDialog() {
        mDialog = new Dialog(this);
        mDialog.setTitle("文件重命名");
        mDialogContentView = View.inflate(this, R.layout.dialog_rename, null);
        mDialog.setContentView(mDialogContentView,
                new LinearLayout.LayoutParams(800, ViewGroup.LayoutParams.WRAP_CONTENT));
        mEtDialogFileName = (EditText) mDialogContentView.findViewById(R.id.et_dialog_rename_filename);
        mBtnDialogCancel = (Button) mDialogContentView.findViewById(R.id.btn_dialog_rename_cancel);
        mBtnDialogOk = (Button) mDialogContentView.findViewById(R.id.btn_dialog_rename_ok);

        mVideoInfoDialog = new Dialog(this);
        mVideoInfoContentView = View.inflate(this, R.layout.dialog_video_info, null);
        mVideoInfoDialog.setContentView(mVideoInfoContentView,
                new LinearLayout.LayoutParams(800, ViewGroup.LayoutParams.WRAP_CONTENT));

        mVideoInfoDialog.setTitle("视频信息");

        mTvDialogInfoName = (TextView) mVideoInfoContentView.findViewById(R.id.tv_dialog_info_name);
        mTvDialogInfoLocation = (TextView) mVideoInfoContentView.findViewById(R.id.tv_dialog_info_location);
        mTvDialogInfoSize = (TextView) mVideoInfoContentView.findViewById(R.id.tv_dialog_info_size);
        mTvDialogInfoDuration = (TextView) mVideoInfoContentView.findViewById(R.id.tv_dialog_info_duration);
        mTvDialogInfoAddTime = (TextView) mVideoInfoContentView.findViewById(R.id.tv_dialog_info_add_time);
        mTvDialogInfoModifiedTime = (TextView) mVideoInfoContentView.findViewById(R.id.tv_dialog_info_modified_time);
        mTvDialogInfoPlayTimes = (TextView) mVideoInfoContentView.findViewById(R.id.tv_dialog_info_play_times);

    }

    @Override
    public void bindDialog(VideoItem videoItem) {

        mBtnDialogOk.setTag(videoItem);
        mBtnDialogCancel.setTag(videoItem);
        String videoName = videoItem.getVideoName();

        mEtDialogFileName.setText(videoName);
        mBtnDialogOk.setOnClickListener(this);
        mBtnDialogCancel.setOnClickListener(this);
        mDialog.show();
    }

    @Override
    public Dialog getDialog() {
        return mDialog;
    }

    @Override
    public String getChangeFileName() {
        return mEtDialogFileName.getText().toString();
    }

    @Override
    public void bindVideoInfoDialog(VideoItem videoItem) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        mTvDialogInfoName.setText(videoItem.getVideoName());
        mTvDialogInfoLocation.setText(videoItem.getDataUrl());
        mTvDialogInfoSize.setText(Formatter.formatFileSize(this, videoItem.getSize()));
        mTvDialogInfoDuration.setText(CommonUtils.getTimeString(videoItem.getVideoDuration()));
        mTvDialogInfoAddTime.setText(simpleDateFormat.format(videoItem.getDateAdd()));
        mTvDialogInfoModifiedTime.setText(simpleDateFormat.format(videoItem.getDateModified()));
        mTvDialogInfoPlayTimes.setText(String.valueOf(videoItem.getPlayTimes()));
        mVideoInfoDialog.show();
    }

    @Override
    public void update(int index, VideoItem videoItem) {
        videoItemList.set(index, videoItem);
        mVideoAdapter.notifyItemChanged(index);
    }

    @Override
    public void showSortWayDialog(DialogInterface.OnClickListener clickListener, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(new String[]{
                "按名称排序", "按时间排序", "按文件大小排序", "按视频长度排序", "按播放次数排序"
        }, position, clickListener)
                .setTitle("排序方式")
                .show();
    }

    @Override
    public void refresh(List<VideoItem> list) {
        this.bindViews(list);
    }

    @Override
    public void hideRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showEmptyView() {
        mRvVideoList.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoListPresenter.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void toolbarSetElevation(float elevation) {
        // setElevation() only works on Lollipop
        tToolbar.setElevation(elevation);
    }

    private void toolbarAnimateShow(final int verticalOffset) {
        tToolbar.animate()
                .translationY(0)
                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        toolbarSetElevation(verticalOffset == 0 ? 0 : TOOLBAR_ELEVATION);
                    }
                });
    }

    private void toolbarAnimateHide() {
        tToolbar.animate()
                .translationY(-tToolbar.getHeight())
                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        toolbarSetElevation(0);
                    }
                });
    }

    @Override
    public void onVideoItemClick(VideoItem videoItem) {
        videoListPresenter.onVideoItemClick(videoItem);
    }

    @Override
    public void onMenuBtnClick(VideoItem videoItem) {
        videoListPresenter.onMenuBtnClick(videoItemList, videoItem);
    }

    @Override
    public boolean onVideoItemLongClick(VideoItem videoItem) {
        return videoListPresenter.onVideoItemLongClick(videoItemList, videoItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return videoListPresenter.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_video_list, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnQueryTextFocusChangeListener(this);
        mSearchView.setOnCloseListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return videoListPresenter.onQuerySubmit(query);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return videoListPresenter.onQueryTextChange(newText);
    }

    @Override
    public boolean onClose() {
        return videoListPresenter.onClose();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        videoListPresenter.onFocusChange(v, hasFocus);
    }

    @Override
    public void onClick(View v) {
        videoListPresenter.onClick(v);
    }


    @Override
    public void onRefresh() {
        videoListPresenter.onRefresh();
    }
}
