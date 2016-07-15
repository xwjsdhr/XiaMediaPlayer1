package com.xwj.xiamediaplayer.views;

import android.content.Intent;
import android.view.Window;

import com.xwj.xiamediaplayer.entitys.VideoItem;
import com.xwj.xiamediaplayer.listener.OnSwipeTouchListener;

import java.util.ArrayList;

/**
 * Created by xiaweijia on 16/3/16.
 */
public interface VideoPlayView {

    void setVideoPath(String videoPath);

    Intent getIntentObj();

    void start();

    void pause();

    boolean isPlaying();

    void startSeeking(int progress);

    void stopSeeking();

    int getProgress();

    void hideTopBar();

    void showTopBar();

    void hideBottomBar();

    void showBottomBar();

    boolean isBarShown();

    void setDuration(int duration);

    int getDuration();

    void seekTo(int progress);

    void setVideoName(String videoName);

    void togglePauseAndStartIcon(int ic_pause_circle_outline_36dp);

    void finishActivity();

    Window getWindowObj();

    void hideLeftBar();

    void showLeftBar();

    void showVolumeBar();

    void initVolumeSeekBar(int maxVolume, int currentVolume);

    void hideVolumeBar();

    boolean isVolumeBarShown();

    void setOnSwipeTouchListener(OnSwipeTouchListener onSwipTouchListener);

    void seekByVolume(int currentVolume);

    void seekBySwipe(int i);

    void setLandScape();

    void setPortrait();

    int getOrientation();

    void showRecyclerView();

    void hideRecyclerView();

    void bindPlayListView(ArrayList<VideoItem> videoItems);

    void setFullScreenIcon(int drawableId);

    void setMuteIbIcon(int drawableId);

    void setMuteIvIcon(int drawableId);

    void setPreBtnEnable(boolean enable);

    void setNextBtnEnable(boolean b);

    void setVideoSize(int width, int height);

    void setLockBtnIcon(int drawableId);

    boolean isLeftBarShown();

    void showAllBars();

    void hideAllBars();

    void release();
}
