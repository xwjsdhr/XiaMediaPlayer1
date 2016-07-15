package com.xwj.xiamediaplayer.views.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.xwj.xiamediaplayer.R;
import com.xwj.xiamediaplayer.entitys.VideoItem;
import com.xwj.xiamediaplayer.listener.OnSwipeTouchListener;
import com.xwj.xiamediaplayer.views.VideoPlayView;

import java.util.ArrayList;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by xiaweijia on 16/7/14.
 */
public class VideoPlayActivity2 extends AppCompatActivity implements VideoPlayView {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video_2);
    }

    @Override
    public void setVideoPath(String videoPath) {

    }

    @Override
    public Intent getIntentObj() {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void startSeeking(int progress) {

    }

    @Override
    public void stopSeeking() {

    }

    @Override
    public int getProgress() {
        return 0;
    }

    @Override
    public void hideTopBar() {

    }

    @Override
    public void showTopBar() {

    }

    @Override
    public void hideBottomBar() {

    }

    @Override
    public void showBottomBar() {

    }

    @Override
    public boolean isBarShown() {
        return false;
    }

    @Override
    public void setDuration(int duration) {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public void seekTo(int progress) {

    }

    @Override
    public void setVideoName(String videoName) {

    }

    @Override
    public void togglePauseAndStartIcon(int ic_pause_circle_outline_36dp) {

    }

    @Override
    public void finishActivity() {

    }

    @Override
    public Window getWindowObj() {
        return null;
    }

    @Override
    public void hideLeftBar() {

    }

    @Override
    public void showLeftBar() {

    }

    @Override
    public void showVolumeBar() {

    }

    @Override
    public void initVolumeSeekBar(int maxVolume, int currentVolume) {

    }

    @Override
    public void hideVolumeBar() {

    }

    @Override
    public boolean isVolumeBarShown() {
        return false;
    }

    @Override
    public void setOnSwipeTouchListener(OnSwipeTouchListener onSwipTouchListener) {

    }

    @Override
    public void seekByVolume(int currentVolume) {

    }

    @Override
    public void seekBySwipe(int i) {

    }

    @Override
    public void setLandScape() {

    }

    @Override
    public void setPortrait() {

    }

    @Override
    public int getOrientation() {
        return 0;
    }

    @Override
    public void showRecyclerView() {

    }

    @Override
    public void hideRecyclerView() {

    }

    @Override
    public void bindPlayListView(ArrayList<VideoItem> videoItems) {

    }

    @Override
    public void setFullScreenIcon(int drawableId) {

    }

    @Override
    public void setMuteIbIcon(int drawableId) {

    }

    @Override
    public void setMuteIvIcon(int drawableId) {

    }

    @Override
    public void setPreBtnEnable(boolean enable) {

    }

    @Override
    public void setNextBtnEnable(boolean b) {

    }

    @Override
    public void setVideoSize(int width, int height) {

    }

    @Override
    public void setLockBtnIcon(int drawableId) {

    }

    @Override
    public boolean isLeftBarShown() {
        return false;
    }

    @Override
    public void showAllBars() {

    }

    @Override
    public void hideAllBars() {

    }

    @Override
    public void release() {

    }
}
