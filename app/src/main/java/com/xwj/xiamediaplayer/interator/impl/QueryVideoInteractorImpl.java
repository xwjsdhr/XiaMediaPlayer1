package com.xwj.xiamediaplayer.interator.impl;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;

import com.github.promeg.pinyinhelper.Pinyin;
import com.xwj.xiamediaplayer.dao.HistoryDao;
import com.xwj.xiamediaplayer.dao.TimesDao;
import com.xwj.xiamediaplayer.dao.impl.HistoryDaoImpl;
import com.xwj.xiamediaplayer.dao.impl.TimesDaoImpl;
import com.xwj.xiamediaplayer.entitys.HistoryVideo;
import com.xwj.xiamediaplayer.entitys.VideoItem;
import com.xwj.xiamediaplayer.interator.QueryListener;
import com.xwj.xiamediaplayer.interator.QueryVideoInteractor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 * 查询视频文件
 * Created by xiaweijia on 16/3/15.
 */
public class QueryVideoInteractorImpl implements QueryVideoInteractor {
    private static final String TAG = QueryVideoInteractorImpl.class.getSimpleName();
    private Context mContext;
    private Handler mHandler;
    private Character mLastIntial = null;
    private Thread thread;
    private HistoryDao historyDao;
    private TimesDao timesDao;

    public QueryVideoInteractorImpl(Context context) {
        this.mContext = context;
        mHandler = new Handler(context.getMainLooper());
        historyDao = new HistoryDaoImpl(context);
        timesDao = new TimesDaoImpl(context);
    }

    @Override
    public void query(final QueryListener listener) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final List<VideoItem> listRes = new ArrayList<VideoItem>();
                List<VideoItem> list = new ArrayList<>();
                String[] projection = new String[]{
                        MediaStore.Video.VideoColumns.DISPLAY_NAME,
                        MediaStore.Video.VideoColumns.DURATION,
                        MediaStore.Video.VideoColumns.SIZE,
                        MediaStore.Video.VideoColumns.DATA
                };
                Cursor cursor = mContext.getContentResolver()
                        .query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null,
                                null, null);
                Log.e(TAG, "" + cursor.getCount());
                while (cursor.moveToNext()) {
                    if (Thread.interrupted()) {
                        return;
                    }
                    VideoItem videoItem = new VideoItem();
                    String videoName = cursor.getString(0);
                    long duration = cursor.getLong(1);
                    long size = cursor.getLong(2);
                    String data = cursor.getString(3);
                    //mediaMetadataRetriever.setDataSource(data);
                    //Bitmap thumbnail = mediaMetadataRetriever.getFrameAtTime(5);
                    Character initialOfVideoName = !Pinyin.isChinese(videoName.charAt(0)) ? Character.toUpperCase(videoName.charAt(0))
                            : Pinyin.toPinyin(videoName.charAt(0)).charAt(0);

                    videoItem.setInitialsOfVideoName(initialOfVideoName);
                    videoItem.setVideoName(videoName);
                    videoItem.setVideoDuration(duration);
                    videoItem.setSize(size);
                    videoItem.setDataUrl(data);
                    //videoItem.setThumbnail(thumbnail);

                    list.add(videoItem);
                    Log.e(TAG, "run: " + initialOfVideoName);
                }

                cursor.close();

                Collections.sort(list, new Comparator<VideoItem>() {
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

                Iterator<VideoItem> iterator = list.iterator();
                while (iterator.hasNext()) {
                    if (Thread.interrupted()) {
                        return;
                    }
                    VideoItem videoItem = new VideoItem();
                    VideoItem item = iterator.next();

                    Character initialOfVideoName = item.getInitialsOfVideoName();
                    String videoName = item.getVideoName();
                    long duration = item.getVideoDuration();
                    long size = item.getSize();
                    String data = item.getDataUrl();
                    //Bitmap thumbnail = item.getThumbnail();

                    if (mLastIntial == null || !mLastIntial.equals(initialOfVideoName)) {
                        mLastIntial = initialOfVideoName;
                        VideoItem videoItem1 = new VideoItem();
                        if (Pinyin.isChinese(mLastIntial)) {
                            videoItem1.setInitialsOfVideoName(Pinyin.toPinyin(mLastIntial).charAt(0));
                        } else {
                            videoItem1.setInitialsOfVideoName(initialOfVideoName);
                        }
                        listRes.add(videoItem1);
                    }
                    videoItem.setInitialsOfVideoName(initialOfVideoName);
                    videoItem.setVideoName(videoName);
                    videoItem.setVideoDuration(duration);
                    videoItem.setSize(size);
                    videoItem.setDataUrl(data);
                    //videoItem.setThumbnail(thumbnail);
                    listRes.add(videoItem);
                }

                list = null;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSuccess(listRes);
                    }
                });
            }
        });
        thread.start();
    }

    @Override
    public void stop() {
        thread.interrupt();
    }


    public void queryNormal(final QueryListener listener) {
        new AsyncTask<Void, Void, List<VideoItem>>() {
            @Override
            protected List<VideoItem> doInBackground(Void... params) {
                final List<VideoItem> list = new ArrayList<>();
                String[] projection = new String[]{
                        MediaStore.Video.VideoColumns.DISPLAY_NAME,
                        MediaStore.Video.VideoColumns.DURATION,
                        MediaStore.Video.VideoColumns.SIZE,
                        MediaStore.Video.VideoColumns.DATA,
                        MediaStore.Video.VideoColumns.DATE_TAKEN,
                        MediaStore.Video.VideoColumns._ID,
                        MediaStore.Video.VideoColumns.DATE_ADDED,
                        MediaStore.Video.VideoColumns.DATE_MODIFIED,
                        MediaStore.Video.VideoColumns.MINI_THUMB_MAGIC,
                        MediaStore.Video.VideoColumns.WIDTH,
                        MediaStore.Video.VideoColumns.HEIGHT
                };
                Cursor cursor = mContext.getContentResolver()
                        .query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null,
                                null, null);
                while (cursor.moveToNext()) {
                    VideoItem videoItem = new VideoItem();
                    String videoName = cursor.getString(0);
                    long duration = cursor.getLong(1);
                    long size = cursor.getLong(2);
                    String data = cursor.getString(3);
                    int dateTaken = cursor.getInt(4);
                    int id = cursor.getInt(5);
                    int addDate = cursor.getInt(6);
                    int modifiedDate = cursor.getInt(7);
                    int thumbId = cursor.getInt(8);
                    int width = cursor.getInt(9);
                    int height = cursor.getInt(10);
                    //模拟大量数据
                    //SystemClock.sleep(100);


                    Character initialOfVideoName = !Pinyin.isChinese(videoName.charAt(0)) ? Character.toUpperCase(videoName.charAt(0))
                            : Pinyin.toPinyin(videoName.charAt(0)).charAt(0);

                    int playTimes = timesDao.getTimesById(id);


                    videoItem.setVideoName(videoName);
                    videoItem.setVideoDuration(duration);
                    videoItem.setSize(size);
                    videoItem.setDataUrl(data);
                    videoItem.setDateTaken(new Date(dateTaken));
                    videoItem.setInitialsOfVideoName(initialOfVideoName);
                    videoItem.setId(id);
                    videoItem.setDateAdd(new Date(addDate));
                    videoItem.setDateModified(new Date(modifiedDate));
                    videoItem.setThumbId(thumbId);
                    videoItem.setWidth(width);
                    videoItem.setHeight(height);
                    videoItem.setPlayTimes(playTimes);

                    list.add(videoItem);
                }
                return list;
            }

            @Override
            protected void onPostExecute(List<VideoItem> list) {
                if (list != null) {
                    listener.onSuccess(list);
                } else {
                    listener.onFailed("查询失败");
                }
            }
        }.execute();
    }

    @Override
    public void queryByName(final QueryListener listener, final String text) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final List<VideoItem> list = new ArrayList<>();
                String[] projection = new String[]{
                        MediaStore.Video.VideoColumns.DISPLAY_NAME,
                        MediaStore.Video.VideoColumns.DURATION,
                        MediaStore.Video.VideoColumns.SIZE,
                        MediaStore.Video.VideoColumns.DATA,
                        MediaStore.Video.VideoColumns.DATE_TAKEN,
                        MediaStore.Video.VideoColumns._ID
                };
                Cursor cursor = mContext.getContentResolver()
                        .query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, MediaStore.Video.VideoColumns.DISPLAY_NAME + " like ?",
                                new String[]{"%" + text + "%"}, null);
                Log.e(TAG, "" + cursor.getCount());
                while (cursor.moveToNext()) {
                    if (Thread.interrupted()) {
                        return;
                    }
                    VideoItem videoItem = new VideoItem();
                    String videoName = cursor.getString(0);
                    long duration = cursor.getLong(1);
                    long size = cursor.getLong(2);
                    String data = cursor.getString(3);
                    int dateTaken = cursor.getInt(4);
                    int id = cursor.getInt(5);

                    Character initialOfVideoName = !Pinyin.isChinese(videoName.charAt(0)) ? Character.toUpperCase(videoName.charAt(0))
                            : Pinyin.toPinyin(videoName.charAt(0)).charAt(0);

                    videoItem.setVideoName(videoName);
                    videoItem.setVideoDuration(duration);
                    videoItem.setSize(size);
                    videoItem.setDataUrl(data);
                    videoItem.setDateTaken(new Date(dateTaken));
                    videoItem.setInitialsOfVideoName(initialOfVideoName);
                    videoItem.setId(id);

                    list.add(videoItem);
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSuccess(list);
                    }
                });
                cursor.close();
            }
        });
        thread.start();
    }

    @Override
    public void queryHistoryFromDb(final QueryListener listener) {
        new AsyncTask<Void, Void, List<HistoryVideo>>() {
            @Override
            protected List<HistoryVideo> doInBackground(Void... params) {
                return historyDao.getAllHistory();
            }

            @Override
            protected void onPostExecute(List<HistoryVideo> historyVideos) {
                if (historyVideos != null) {
                    listener.onSuccess(historyVideos);
                } else {
                    listener.onFailed("查询失败");
                }
            }
        }.execute();
    }


}
