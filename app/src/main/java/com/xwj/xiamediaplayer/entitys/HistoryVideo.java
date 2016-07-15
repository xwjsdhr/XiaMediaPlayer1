package com.xwj.xiamediaplayer.entitys;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xwjsd on 2016-05-05.
 */
public class HistoryVideo implements Serializable {
    private VideoItem videoItem;
    private boolean playFinished = false;
    private int playPos;
    private Date startPlayTime;

    public Date getStartPlayTime() {
        return startPlayTime;
    }

    public void setStartPlayTime(Date startPlayTime) {
        this.startPlayTime = startPlayTime;
    }

    public boolean isPlayFinished() {
        return playFinished;
    }

    public void setPlayFinished(boolean playFinished) {
        this.playFinished = playFinished;
    }

    public int getPlayPos() {
        return playPos;
    }

    public void setPlayPos(int playPos) {
        this.playPos = playPos;
    }

    public VideoItem getVideoItem() {
        return videoItem;
    }

    public void setVideoItem(VideoItem videoItem) {
        this.videoItem = videoItem;
    }
}
