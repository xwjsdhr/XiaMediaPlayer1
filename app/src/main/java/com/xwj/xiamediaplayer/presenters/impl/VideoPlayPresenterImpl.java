package com.xwj.xiamediaplayer.presenters.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import com.xwj.xiamediaplayer.R;
import com.xwj.xiamediaplayer.Setting;
import com.xwj.xiamediaplayer.dao.HistoryDao;
import com.xwj.xiamediaplayer.dao.TimesDao;
import com.xwj.xiamediaplayer.dao.impl.HistoryDaoImpl;
import com.xwj.xiamediaplayer.dao.impl.TimesDaoImpl;
import com.xwj.xiamediaplayer.entitys.HistoryVideo;
import com.xwj.xiamediaplayer.entitys.VideoItem;
import com.xwj.xiamediaplayer.listener.OnSwipeTouchListener;
import com.xwj.xiamediaplayer.presenters.VideoPlayPresenter;
import com.xwj.xiamediaplayer.utils.Constant;
import com.xwj.xiamediaplayer.views.VideoPlayView;

import java.util.ArrayList;
import java.util.Date;

/**
 * 播放视频界面的业务逻辑类
 * Created by xiaweijia on 16/3/22.
 */
public class VideoPlayPresenterImpl implements VideoPlayPresenter {

    private static final String TAG = VideoPlayPresenterImpl.class.getSimpleName();
    private static final int ACTION_HIDE_BAR = 3;
    private static final int ACTION_HIDE_VOLUME = 4;
    private static final int SHOW_TIMEMILL = 3000;
    private Context mContext;
    private AudioManager mAudioManager;
    private VideoPlayView mVideoPlayView;
    private WindowManager windowManager;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private int startX = 0;
    private int mMaxVolume;
    private int mVolume = -1;
    private ArrayList<VideoItem> videoItemArrayList;
    private int mPosition;
    private int mPreVolume;
    private boolean mIsLockScreen = false;
    private SharedPreferences mDefaultSharedPreferences;
    long startTime;
    private int mCurrPosition;
    private HistoryVideo historyVideo;

    private HistoryDao mHistoryDao;
    private TimesDao mTimesDao;
    private VideoItem mVideoItem;
    private static final int ACTION_START_SEEKING = 1;
    private static final int ACTION_STOP_SEEKING = 2;
    private Setting mSetting;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ACTION_START_SEEKING:
                    int p = mVideoPlayView.getProgress();
                    mVideoPlayView.startSeeking(p);
                    mCurrPosition = p + 1000;
                    handler.sendEmptyMessageDelayed(ACTION_START_SEEKING, 1000);
                    break;
                case ACTION_STOP_SEEKING:
                    handler.removeCallbacksAndMessages(null);
                    break;
                case ACTION_HIDE_BAR:
                    if (mVideoPlayView.isBarShown()) {
                        mVideoPlayView.hideTopBar();
                        mVideoPlayView.hideBottomBar();
                        mVideoPlayView.hideLeftBar();
                    }
                case ACTION_HIDE_VOLUME:
                    mVideoPlayView.hideVolumeBar();
                    break;

            }
        }
    };

    public VideoPlayPresenterImpl(final Context context, VideoPlayView videoPlayView) {
        this.mContext = context;
        mVideoPlayView = videoPlayView;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mHistoryDao = new HistoryDaoImpl(context);
        mTimesDao = new TimesDaoImpl(context);
        mSetting = new Setting(context);

        mDefaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mVideoPlayView.setOnSwipeTouchListener(new OnSwipeTouchListener(context) {
            @Override
            public void onSwipeRight() {
                int duration = mVideoPlayView.getDuration();
                int step = duration / 20;
                if (mVideoPlayView.getProgress() != mVideoPlayView.getDuration()) {
                    if ((mVideoPlayView.getProgress() + step) > mVideoPlayView.getDuration()) {
                        mVideoPlayView.seekBySwipe(mVideoPlayView.getDuration());
                    } else {
                        mVideoPlayView.seekBySwipe(mVideoPlayView.getDuration() + step);
                    }
                }
            }

            @Override
            public void onSwipeLeft() {
                int duration = mVideoPlayView.getDuration();
                int step = duration / 20;
                if (mVideoPlayView.getProgress() != 0) {
                    if (mVideoPlayView.getProgress() - step < 0) {
                        mVideoPlayView.seekBySwipe(0);
                    } else {
                        mVideoPlayView.seekBySwipe(mVideoPlayView.getProgress() - step);
                    }
                }
            }

            //向上滑动
            @Override
            public void onSwipeUp() {
                if (!mVideoPlayView.isVolumeBarShown()) {
                    mVideoPlayView.showVolumeBar();
                }
                int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                int changeVolume = 0;
                if (mMaxVolume != currentVolume) {
                    changeVolume = currentVolume + 4;
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, changeVolume, 0);
                } else {
                    changeVolume = mMaxVolume;
                }

                mVideoPlayView.seekByVolume(changeVolume);

                if (handler.hasMessages(ACTION_HIDE_VOLUME)) {
                    handler.removeMessages(ACTION_HIDE_VOLUME);
                }
                handler.sendEmptyMessageDelayed(ACTION_HIDE_VOLUME, SHOW_TIMEMILL);
            }

            //向下滑动
            @Override
            public void onSwipeDown() {
                if (!mVideoPlayView.isVolumeBarShown()) {
                    mVideoPlayView.showVolumeBar();
                }

                int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                int changeVolume = 0;
                if (currentVolume >= 4) {
                    changeVolume = currentVolume - 4;
                } else if (currentVolume < 4 && currentVolume > 0) {
                    changeVolume = 0;
                } else {
                    changeVolume = currentVolume;
                }
                mVideoPlayView.seekByVolume(changeVolume);

                if (handler.hasMessages(ACTION_HIDE_VOLUME)) {
                    handler.removeMessages(ACTION_HIDE_VOLUME);
                }
                handler.sendEmptyMessageDelayed(ACTION_HIDE_VOLUME, SHOW_TIMEMILL);
            }

            @Override
            public void onClick() {
                if (mVideoPlayView.isBarShown()) {
                    mVideoPlayView.hideBottomBar();
                    mVideoPlayView.hideTopBar();
                    mVideoPlayView.hideLeftBar();
                    handler.removeMessages(ACTION_HIDE_BAR);

                } else {
                    mVideoPlayView.showBottomBar();
                    mVideoPlayView.showTopBar();
                    mVideoPlayView.showLeftBar();
                    if (!handler.hasMessages(ACTION_HIDE_BAR)) {
                        handler.sendEmptyMessageDelayed(ACTION_HIDE_BAR, SHOW_TIMEMILL);
                    }
                }
            }
        });

    }

    @Override
    public void onCreate() {

        String orientation = mSetting.getVideoScreenOrientation();
        if (orientation.equals("por")) {
            mVideoPlayView.setPortrait();
            mVideoPlayView.showRecyclerView();
            mVideoPlayView.setFullScreenIcon(R.drawable.ic_fullscreen_white_36dp);
        } else {
            mVideoPlayView.setLandScape();
            mVideoPlayView.hideRecyclerView();
            mVideoPlayView.setFullScreenIcon(R.drawable.ic_fullscreen_exit_white_36dp);
        }

        Bundle bundle = mVideoPlayView.getIntentObj().getBundleExtra(Constant.BUNDLE_NAME);

        if (bundle.getSerializable(Constant.VIDEO_ITEM) != null) {
            mVideoItem = (VideoItem) bundle.getSerializable(Constant.VIDEO_ITEM);
        } else {
            historyVideo = (HistoryVideo) bundle.getSerializable(Constant.HISTORY_VIDEO);
            if (historyVideo != null) {
                mVideoItem = historyVideo.getVideoItem();
            }
        }

        //获取播放列表
        if (bundle.getSerializable(Constant.VIDEO_LIST) != null) {
            videoItemArrayList = (ArrayList<VideoItem>) bundle.getSerializable(Constant.VIDEO_LIST);
        }

        //获取当前播放的位置
        mPosition = bundle.getInt(Constant.VIDEO_POSITION);
        //将视频文件的路径传给VideoView
        mVideoPlayView.setVideoPath(mVideoItem.getDataUrl());
        //获取最大音量
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //获取当前音量
        int currVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //初始化音量SeekBar的当前位置
        mVideoPlayView.initVolumeSeekBar(maxVolume, currVolume);

        if (videoItemArrayList != null) {
            videoItemArrayList.get(mPosition).setIsPlaying(true);
            //将数据绑定到当前RecyclerView上
            mVideoPlayView.bindPlayListView(videoItemArrayList);
        }
    }

    /**
     * 点击事件处理
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_video_play_toggle://点击播放或者是暂停
                if (mVideoPlayView.isPlaying()) {
                    mVideoPlayView.pause();
                    handler.sendEmptyMessage(ACTION_STOP_SEEKING);
                    mVideoPlayView.togglePauseAndStartIcon(R.drawable.ic_play_circle_outline_white_36dp);
                    handler.removeMessages(ACTION_HIDE_BAR);
                } else {
                    mVideoPlayView.start();
                    handler.sendEmptyMessage(ACTION_START_SEEKING);
                    mVideoPlayView.togglePauseAndStartIcon(R.drawable.ic_pause_circle_outline_white_36dp);
                    if (handler.hasMessages(ACTION_HIDE_BAR)) {
                        handler.removeMessages(ACTION_HIDE_BAR);
                    }
                    handler.sendEmptyMessageDelayed(ACTION_HIDE_BAR, SHOW_TIMEMILL);
                }
                break;
            case R.id.ib_video_play_pre://点击上一步
                playPre();
                break;
            case R.id.ib_video_play_next://点击播放下一步
                playNext();
                break;
            case R.id.ll_center_panel://点击中心面板
                Toast.makeText(mContext, "ll_center_panel", Toast.LENGTH_SHORT).show();
                if (mVideoPlayView.isBarShown()) {
                    mVideoPlayView.hideBottomBar();
                    mVideoPlayView.hideTopBar();
                    mVideoPlayView.hideLeftBar();

                } else {
                    mVideoPlayView.showBottomBar();
                    mVideoPlayView.showTopBar();
                    mVideoPlayView.showLeftBar();
                }

                break;
            case R.id.ib_video_play_back:
                mVideoPlayView.finishActivity();
                break;

            case R.id.ib_video_play_full_screen://点击切换全屏按钮
                if (mVideoPlayView.getOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    mVideoPlayView.setLandScape();
                    mVideoPlayView.hideRecyclerView();
                    mVideoPlayView.setFullScreenIcon(R.drawable.ic_fullscreen_exit_white_36dp);
                } else {
                    mVideoPlayView.setPortrait();
                    mVideoPlayView.showRecyclerView();
                    mVideoPlayView.setFullScreenIcon(R.drawable.ic_fullscreen_white_36dp);
                }
                break;
            case R.id.ib_video_play_mute://点击静音按钮
                if (mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) != 0) {
                    mVideoPlayView.showVolumeBar();
                    mPreVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    mVideoPlayView.seekByVolume(0);
                    mVideoPlayView.setMuteIbIcon(R.drawable.ic_volume_off_white_36dp);
                    mVideoPlayView.setMuteIvIcon(R.drawable.ic_volume_off_white_24dp);
                } else {
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mPreVolume, 0);
                    mVideoPlayView.seekByVolume(mPreVolume);
                    mVideoPlayView.setMuteIbIcon(R.drawable.ic_volume_up_white_36dp);
                    mVideoPlayView.setMuteIvIcon(R.drawable.ic_volume_up_white_24dp);
                }
                break;

            case R.id.ib_video_play_lock:
                if (mIsLockScreen) {
                    mIsLockScreen = false;
                    Log.e(TAG, "onClick: " + mIsLockScreen);
                    mVideoPlayView.setLockBtnIcon(R.drawable.ic_lock_open_white_36dp);
                    mVideoPlayView.showTopBar();
                    mVideoPlayView.showBottomBar();
                } else {
                    mIsLockScreen = true;
                    Log.e(TAG, "onClick: " + mIsLockScreen);
                    mVideoPlayView.hideTopBar();
                    mVideoPlayView.hideBottomBar();
                    mVideoPlayView.setLockBtnIcon(R.drawable.ic_lock_outline_white_36dp);
                }
                break;
        }
    }

    private void playPre() {
        if (mPosition > 1) {
            mPosition = mPosition - 1;
            VideoItem videoItem = videoItemArrayList.get(mPosition);
            this.mVideoItem = videoItem;
            mVideoPlayView.setVideoPath(videoItem.getDataUrl());
            handler.removeCallbacksAndMessages(null);
            mVideoPlayView.setNextBtnEnable(true);
        } else {
            mVideoPlayView.setPreBtnEnable(false);
        }
    }

    private void playNext() {
        if (mPosition != videoItemArrayList.size()) {
            mPosition = mPosition + 1;
            VideoItem videoItem = videoItemArrayList.get(mPosition);
            this.mVideoItem = videoItem;
            mVideoPlayView.setVideoPath(videoItem.getDataUrl());
            handler.removeCallbacksAndMessages(null);
            mVideoPlayView.setPreBtnEnable(true);
        } else {
            mVideoPlayView.setNextBtnEnable(false);
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroyed");
        handler.removeCallbacksAndMessages(null);
        handler = null;
        if (!mHistoryDao.isExist(mVideoItem.getId())) {
            HistoryVideo historyVideo = new HistoryVideo();
            historyVideo.setPlayFinished(false);
            historyVideo.setStartPlayTime(new Date(startTime));
            historyVideo.setPlayPos(mCurrPosition);
            historyVideo.setVideoItem(mVideoItem);
            long res = mHistoryDao.insert(historyVideo);
            Log.e(TAG, "onDestroy: insert" + res + mCurrPosition);
        } else {
            HistoryVideo historyVideo = new HistoryVideo();
            historyVideo.setPlayFinished(false);
            historyVideo.setStartPlayTime(new Date(startTime));
            historyVideo.setPlayPos(mCurrPosition);
            historyVideo.setVideoItem(mVideoItem);
            mHistoryDao.update(historyVideo);
            long res = mHistoryDao.update(historyVideo);
            Log.e(TAG, "onDestroy: update" + res + mCurrPosition);
        }
    }

    /**
     * 监听SeekBar的进度改变的方法
     *
     * @param seekBar  发生改变的SeekBar
     * @param progress 改变后的进度
     * @param fromUser 是否是用户拖动
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sb_video_duration:
                if (fromUser) {
                    mVideoPlayView.seekTo(progress);
                }
                break;
            case R.id.sb_video_play_volume:

                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                if (progress == 0) {
                    mVideoPlayView.setMuteIbIcon(R.drawable.ic_volume_off_white_36dp);
                    mVideoPlayView.setMuteIvIcon(R.drawable.ic_volume_off_white_24dp);
                } else {
                    mVideoPlayView.setMuteIbIcon(R.drawable.ic_volume_up_white_36dp);
                    mVideoPlayView.setMuteIvIcon(R.drawable.ic_volume_up_white_24dp);
                }
                break;
        }

    }

    /**
     * 监听停止拖动的方法
     *
     * @param seekBar
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.sb_video_duration:
                handler.sendEmptyMessageDelayed(ACTION_HIDE_BAR, SHOW_TIMEMILL);
                break;
            case R.id.sb_video_play_volume:
                handler.sendEmptyMessageDelayed(ACTION_HIDE_VOLUME, SHOW_TIMEMILL);
                break;
        }
    }

    /**
     * 监听开始拖动的方法
     *
     * @param seekBar
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.sb_video_duration:
                handler.removeMessages(ACTION_HIDE_BAR);
                break;
            case R.id.sb_video_play_volume:
                handler.removeMessages(ACTION_HIDE_VOLUME);
                break;
        }
    }

    /**
     * 监听播放视图下面的列表的每一项的点击事件的监听
     *
     * @param videoItem
     */
    @Override
    public void onVideoItemClick(VideoItem videoItem) {
        mVideoPlayView.setVideoPath(videoItem.getDataUrl());
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * @param mp
     * @param what
     * @param extra
     * @return
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(mContext, "无法播放该视频", Toast.LENGTH_SHORT).show();
        mVideoPlayView.finishActivity();
        return true;
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {
        mVideoPlayView.pause();
    }

    @Override
    public void onResume() {
        if (!mVideoPlayView.isPlaying()) mVideoPlayView.start();
    }

    /**
     * 监听当VideoView内置的MediaPlayer准备完成的方法。
     *
     * @param mediaPlayer
     */
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        startTime = System.currentTimeMillis();

        //当准备成功时，
        mVideoPlayView.setVideoSize(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
        mVideoPlayView.setDuration(mVideoPlayView.getDuration());//设置长度
        mVideoPlayView.setVideoName(mVideoItem.getVideoName());//设置视频名称
        mTimesDao.autoIncrement(mVideoItem.getId());
        if (historyVideo != null) {
            mediaPlayer.seekTo(historyVideo.getPlayPos());
        }
        mediaPlayer.start();//开始播放
        handler.sendEmptyMessage(ACTION_START_SEEKING);
        handler.sendEmptyMessageDelayed(ACTION_HIDE_BAR, SHOW_TIMEMILL);
    }

    /**
     * 监听播放完成的方法。
     *
     * @param mediaPlayer
     */
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        SharedPreferences defaultSharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(mContext);
        if (!mHistoryDao.isExist(mVideoItem.getId())) {
            HistoryVideo historyVideo = new HistoryVideo();
            historyVideo.setPlayFinished(true);
            historyVideo.setStartPlayTime(new Date(startTime));
            historyVideo.setPlayPos(0);
            historyVideo.setVideoItem(mVideoItem);
            long res = mHistoryDao.insert(historyVideo);
            Log.e(TAG, "onCompletion: insert" + res + mVideoPlayView.getProgress());
        } else {
            HistoryVideo historyVideo = new HistoryVideo();
            historyVideo.setPlayFinished(true);
            historyVideo.setStartPlayTime(new Date(startTime));
            historyVideo.setPlayPos(0);
            historyVideo.setVideoItem(mVideoItem);
            long res = mHistoryDao.update(historyVideo);
            Log.e(TAG, "onCompletion: update" + res + mVideoPlayView.getProgress());
        }
        if (videoItemArrayList != null) {
            boolean autoNext = mSetting.isAutoPlayNext();
            if (autoNext) {
                playNext();
                Toast.makeText(mContext, "自动播放下一个视频", Toast.LENGTH_SHORT).show();
            } else {
                mVideoPlayView.finishActivity();
                handler.removeCallbacksAndMessages(null);
            }
        }
    }

}
