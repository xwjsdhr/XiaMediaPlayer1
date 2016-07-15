package com.xwj.xiamediaplayer.views.impl;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xwj.xiamediaplayer.R;
import com.xwj.xiamediaplayer.adapters.PlayVideoItemAdapter;
import com.xwj.xiamediaplayer.entitys.VideoItem;
import com.xwj.xiamediaplayer.listener.OnSwipeTouchListener;
import com.xwj.xiamediaplayer.presenters.VideoPlayPresenter;
import com.xwj.xiamediaplayer.presenters.impl.VideoPlayPresenterImpl;
import com.xwj.xiamediaplayer.utils.CommonUtils;
import com.xwj.xiamediaplayer.views.VideoPlayView;
import com.xwj.xiamediaplayer.widget.MyVideoView;

import java.util.ArrayList;
import java.util.List;


/**
 * 视频播放界面
 * Created by xiaweijia on 16/3/16.
 */
public class VideoPlayActivity extends Activity implements VideoPlayView,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        View.OnClickListener, SeekBar.OnSeekBarChangeListener, PlayVideoItemAdapter.OnPlayVideoItemClickListener, MediaPlayer.OnErrorListener {
    private static final String TAG = VideoPlayActivity.class.getSimpleName();
    private MyVideoView mVideoView;
    private VideoPlayPresenter videoPlayPresenter;
    private ImageButton mIbToggle, mIbPre, mIbNext;
    private SeekBar mSbDuration;
    private LinearLayout mLlCenterPanel;
    private LinearLayout mLlTopBar, mLlBottomBar;
    private TextView mTvVideoName;
    private TextView mTvVideoProgress, mTvVideoDuration;
    private ImageButton mIbVideoBack;
    private RelativeLayout mRlLeftBar;
    //    private Button mTvShowVolumeBar;
    private LinearLayout mLlVolumeBar;
    private SeekBar mSbVolume;
    private ImageButton mIBToggleFullScreen;
    private RecyclerView mRvPlayList;
    private RelativeLayout rl_video_play;
    private ImageButton mIbMute;
    private ImageView mIvVideoPlayMute;
    private ImageButton mIbLock;
    private List<VideoItem> videoItems;
    private PlayVideoItemAdapter playVideoItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        videoItems = new ArrayList<>();
        playVideoItemAdapter = new PlayVideoItemAdapter(this, videoItems);
        initViews();
        setListeners();
        videoPlayPresenter = new VideoPlayPresenterImpl(this, this);
        videoPlayPresenter.onCreate();
    }

    private void setListeners() {
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnClickListener(this);
        mVideoView.setMediaController(null);
        mIbToggle.setOnClickListener(this);
        mIbPre.setOnClickListener(this);
        mIbNext.setOnClickListener(this);
//        mLlCenterPanel.setOnClickListener(this);
        mSbDuration.setOnSeekBarChangeListener(this);
        mSbVolume.setOnSeekBarChangeListener(this);
        mIbVideoBack.setOnClickListener(this);
//        mTvShowVolumeBar.setOnClickListener(this);
        mIbMute.setOnClickListener(this);
        mIBToggleFullScreen.setOnClickListener(this);
        playVideoItemAdapter.setOnPlayVideoItemClickListener(this);
        mVideoView.setOnErrorListener(this);
        mIbLock.setOnClickListener(this);
    }

    private void initViews() {
        mVideoView = (MyVideoView) this.findViewById(R.id.video_view);
        mIbToggle = (ImageButton) this.findViewById(R.id.ib_video_play_toggle);
        mIbPre = (ImageButton) this.findViewById(R.id.ib_video_play_pre);
        mIbNext = (ImageButton) this.findViewById(R.id.ib_video_play_next);
        mSbDuration = (SeekBar) this.findViewById(R.id.sb_video_duration);
        mLlCenterPanel = (LinearLayout) this.findViewById(R.id.ll_center_panel);
        mLlTopBar = (LinearLayout) this.findViewById(R.id.ll_video_play_top_bar);
        mLlBottomBar = (LinearLayout) this.findViewById(R.id.ll_bottom_bar);
        mTvVideoName = (TextView) this.findViewById(R.id.tv_video_play_name);
        mTvVideoProgress = (TextView) this.findViewById(R.id.tv_video_play_progress);
        mTvVideoDuration = (TextView) this.findViewById(R.id.tv_video_play_duration);
        mIbVideoBack = (ImageButton) this.findViewById(R.id.ib_video_play_back);
        mRlLeftBar = (RelativeLayout) this.findViewById(R.id.rl_left_bar);
//        mTvShowVolumeBar = (Button) this.findViewById(R.id.tv_show_volume_bar);
        mLlVolumeBar = (LinearLayout) this.findViewById(R.id.ll_volume_bar);
        mSbVolume = (SeekBar) this.findViewById(R.id.sb_video_play_volume);
        mIBToggleFullScreen = (ImageButton) this.findViewById(R.id.ib_video_play_full_screen);
        mRvPlayList = (RecyclerView) this.findViewById(R.id.rv_video_play_list);
        rl_video_play = (RelativeLayout) this.findViewById(R.id.rl_video_play);
        mIbMute = (ImageButton) this.findViewById(R.id.ib_video_play_mute);
        mIvVideoPlayMute = (ImageView) this.findViewById(R.id.iv_video_play_mute);
        mIbLock = (ImageButton) this.findViewById(R.id.ib_video_play_lock);

        mRvPlayList.setLayoutManager(new LinearLayoutManager(this));
        mRvPlayList.setAdapter(playVideoItemAdapter);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.e(TAG, "正在准备。。");
        videoPlayPresenter.onPrepared(mp);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.e(TAG, "准备完成..");
        videoPlayPresenter.onCompletion(mp);
    }

    @Override
    public void setVideoPath(String videoPath) {
        mVideoView.setVideoPath(videoPath);
    }

    @Override
    public Intent getIntentObj() {
        return this.getIntent();
    }

    @Override
    public void start() {
        mVideoView.start();
    }

    @Override
    public void pause() {
        mVideoView.pause();
    }

    @Override
    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }

    @Override
    public void startSeeking(int progress) {
        mSbDuration.setProgress(progress);
        mTvVideoProgress.setText(CommonUtils.getTimeString(progress));
    }

    @Override
    public void stopSeeking() {

    }

    @Override
    public int getProgress() {
        int res = mVideoView.getCurrentPosition();
        Log.e(TAG, "getProgress: progress" + res);
        return res;
    }

    @Override
    public void hideTopBar() {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0, Animation.RELATIVE_TO_SELF,
                0, Animation.RELATIVE_TO_SELF,
                0, Animation.RELATIVE_TO_SELF,
                -1.0f);
        animation.setDuration(300);
        animation.setFillAfter(false);
        mLlTopBar.startAnimation(animation);
        mLlTopBar.setVisibility(View.GONE);
    }

    @Override
    public void showTopBar() {
        Log.e(TAG, "showTopBar: ");
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0, Animation.RELATIVE_TO_SELF,
                0, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF,
                0);
        animation.setDuration(300);
        animation.setFillAfter(false);
        mLlTopBar.startAnimation(animation);
        mLlTopBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomBar() {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0, Animation.RELATIVE_TO_SELF,
                0, Animation.RELATIVE_TO_SELF,
                0, Animation.RELATIVE_TO_PARENT,
                1);
        animation.setDuration(300);
        animation.setFillAfter(false);
        mLlBottomBar.startAnimation(animation);
        mLlBottomBar.setVisibility(View.GONE);
    }

    /**
     * 显示底部栏
     */
    @Override
    public void showBottomBar() {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0, Animation.RELATIVE_TO_SELF,
                0, Animation.RELATIVE_TO_PARENT,
                1, Animation.RELATIVE_TO_PARENT,
                0);
        animation.setDuration(300);
        animation.setFillAfter(false);
        mLlBottomBar.startAnimation(animation);
        mLlBottomBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isBarShown() {
        return mLlBottomBar.isShown() && mLlTopBar.isShown();
    }

    @Override
    public void setDuration(int duration) {
        mSbDuration.setMax(duration);
        mTvVideoDuration.setText(CommonUtils.getTimeString(duration));
    }

    @Override
    public int getDuration() {
        return mVideoView.getDuration();
    }

    @Override
    public void seekTo(int progress) {
        mVideoView.seekTo(progress);
    }

    @Override
    public void setVideoName(String videoName) {
        mTvVideoName.setText(videoName);
    }

    /**
     * 切换播放按钮图标
     *
     * @param drawableId
     */
    @Override
    public void togglePauseAndStartIcon(int drawableId) {
        mIbToggle.setImageResource(drawableId);
    }

    /**
     * 结束当前窗口
     */
    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public Window getWindowObj() {
        return this.getWindow();
    }

    @Override
    public void hideLeftBar() {
        Animation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -1,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0
        );
        animation.setDuration(300);
        animation.setFillAfter(false);
        mRlLeftBar.startAnimation(animation);
        mRlLeftBar.setVisibility(View.GONE);
    }

    @Override
    public void showLeftBar() {
        Animation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, -1,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0
        );
        animation.setDuration(300);
        animation.setFillAfter(false);
//        mRlLeftBar.startAnimation(animation);
//        mRlLeftBar.setVisibility(View.VISIBLE);
    }

    /**
     * 显示音量条
     */
    @Override
    public void showVolumeBar() {
        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(300);
        mLlVolumeBar.startAnimation(animation);
        mLlVolumeBar.setVisibility(View.VISIBLE);

    }

    /**
     * 初始化音量条
     *
     * @param maxVolume
     * @param currentVolume
     */
    @Override
    public void initVolumeSeekBar(int maxVolume, int currentVolume) {
        mSbVolume.setMax(maxVolume);
        mSbVolume.setProgress(currentVolume);
    }

    /**
     * 隐藏音量条
     */
    @Override
    public void hideVolumeBar() {
        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(300);
        mLlVolumeBar.startAnimation(animation);
        mLlVolumeBar.setVisibility(View.GONE);

    }

    /**
     * 判断音量条是否显示
     *
     * @return
     */
    @Override
    public boolean isVolumeBarShown() {
        return mLlVolumeBar.isShown();
    }

    @Override
    public void setOnSwipeTouchListener(OnSwipeTouchListener onSwipeTouchListener) {
        mLlCenterPanel.setOnTouchListener(onSwipeTouchListener);
    }

    /**
     * 根据指定为音量大小改变当前Seekbar的进度
     *
     * @param currentVolume
     */
    @Override
    public void seekByVolume(int currentVolume) {
        mSbVolume.setProgress(currentVolume);
    }

    @Override
    public void seekBySwipe(int i) {
        mVideoView.seekTo(i);
    }

    /**
     * 设置当前窗口为横屏
     */
    @Override
    public void setLandScape() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 设置当前窗口为竖屏
     */
    @Override
    public void setPortrait() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 获取当前屏幕方向
     *
     * @return
     */
    @Override
    public int getOrientation() {
        return getResources().getConfiguration().orientation;
    }

    /**
     * 显示列表
     */
    @Override
    public void showRecyclerView() {
        mRvPlayList.setVisibility(View.VISIBLE);
        rl_video_play.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) CommonUtils.dp2px(this, 300)));
    }

    /**
     * 隐藏列表
     */
    @Override
    public void hideRecyclerView() {
        mRvPlayList.setVisibility(View.GONE);
        rl_video_play.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    /**
     * 绑定播放列表
     *
     * @param videoItems
     */
    @Override
    public void bindPlayListView(ArrayList<VideoItem> videoItems) {
        Log.e(TAG, "bindPlayListView: ");
        this.videoItems.addAll(videoItems);
        playVideoItemAdapter.notifyDataSetChanged();
    }

    /**
     * 设置全屏按钮图标。
     *
     * @param drawableId
     */
    @Override
    public void setFullScreenIcon(int drawableId) {
        mIBToggleFullScreen.setImageResource(drawableId);
    }

    /**
     * 设置静音按钮图标
     *
     * @param drawableId
     */
    @Override
    public void setMuteIbIcon(int drawableId) {
        mIbMute.setImageResource(drawableId);
    }

    /**
     * 设置静音图片图标
     *
     * @param drawableId
     */
    @Override
    public void setMuteIvIcon(int drawableId) {
        mIvVideoPlayMute.setImageResource(drawableId);
    }

    @Override
    public void setPreBtnEnable(boolean enable) {
        mIbPre.setClickable(enable);
        mIbPre.setEnabled(enable);
    }

    @Override
    public void setNextBtnEnable(boolean enable) {
        mIbNext.setEnabled(enable);
        mIbNext.setClickable(enable);

    }

    @Override
    public void setVideoSize(int width, int height) {
        mVideoView.setVideoSize(width, height);
    }

    @Override
    public void setLockBtnIcon(int drawableId) {
        mIbLock.setImageResource(drawableId);
    }

    @Override
    public boolean isLeftBarShown() {
        return mRlLeftBar.isShown();
    }

    @Override
    public void showAllBars() {
        this.showBottomBar();
        this.showTopBar();
        this.showLeftBar();
    }

    @Override
    public void hideAllBars() {
        this.hideLeftBar();
        this.hideTopBar();
        this.hideBottomBar();
    }

    @Override
    public void release() {
        mVideoView.stopPlayback();
    }

    /**
     * 点击监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        videoPlayPresenter.onClick(v);
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayPresenter.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
        videoPlayPresenter.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayPresenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoPlayPresenter.onDestroy();
    }


    /**
     * 进度改变监听
     *
     * @param seekBar
     * @param progress
     * @param fromUser
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        videoPlayPresenter.onProgressChanged(seekBar, progress, fromUser);
    }

    /**
     * 开始拖动
     *
     * @param seekBar
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        videoPlayPresenter.onStartTrackingTouch(seekBar);
    }

    /**
     * 停止拖动
     *
     * @param seekBar
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        videoPlayPresenter.onStopTrackingTouch(seekBar);
    }

    /**
     * item点击监听
     *
     * @param videoItem
     */
    @Override
    public void onVideoItemClick(VideoItem videoItem) {
        videoPlayPresenter.onVideoItemClick(videoItem);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return videoPlayPresenter.onError(mp, what, extra);
    }
}