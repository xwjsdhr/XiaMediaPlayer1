package com.xwj.xiamediaplayer.presenters;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.SeekBar;

import com.xwj.xiamediaplayer.entitys.VideoItem;


/**
 * Created by xiaweijia on 16/3/22.
 */
public interface VideoPlayPresenter {
    void onCreate();

    void onClick(View view);

    void onDestroy();

    void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);

    void onPrepared(MediaPlayer mediaPlayer);

    void onCompletion(MediaPlayer mediaPlayer);

    void onStopTrackingTouch(SeekBar seekBar);

    void onStartTrackingTouch(SeekBar seekBar);

    void onVideoItemClick(VideoItem videoItem);

    boolean onError(MediaPlayer mp, int what, int extra);

    void onStop();

    void onPause();

    void onResume();
}
