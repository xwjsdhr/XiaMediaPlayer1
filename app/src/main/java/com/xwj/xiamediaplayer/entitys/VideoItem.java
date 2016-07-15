package com.xwj.xiamediaplayer.entitys;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xiaweijia on 16/3/15.
 */
public class VideoItem implements Serializable {
    private int id;
    private String dataUrl;
    private String videoName;
    private Long videoDuration;
    private Long size;
    //private Bitmap thumbnail;
    private Character initialsOfVideoName;
    private boolean isPlaying = false;
    private Date dateTaken;
    private Date dateAdd;
    private Date dateModified;
    private int thumbId;
    private int width;
    private int height;
    private int playTimes;

    public int getPlayTimes() {
        return playTimes;
    }

    public void setPlayTimes(int playTimes) {
        this.playTimes = playTimes;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public int getThumbId() {
        return thumbId;
    }

    public void setThumbId(int thumbId) {
        this.thumbId = thumbId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(Date dateTaken) {
        this.dateTaken = dateTaken;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public Character getInitialsOfVideoName() {
        return initialsOfVideoName;
    }

    public void setInitialsOfVideoName(Character initialsOfVideoName) {
        this.initialsOfVideoName = initialsOfVideoName;
    }

//    public Bitmap getThumbnail() {
//        return thumbnail;
//    }
//
//    public void setThumbnail(Bitmap thumbnail) {
//        this.thumbnail = thumbnail;
//    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public Long getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(Long videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    @Override
    public String toString() {
        return "VideoItem{" +
                "dataUrl='" + dataUrl + '\'' +
                ", videoName='" + videoName + '\'' +
                ", videoDuration=" + videoDuration +
                ", size=" + size +
                ", initialsOfVideoName=" + initialsOfVideoName +
                '}';
    }
}
