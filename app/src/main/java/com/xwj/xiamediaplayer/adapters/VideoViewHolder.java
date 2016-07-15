package com.xwj.xiamediaplayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xwj.xiamediaplayer.R;
import com.xwj.xiamediaplayer.entitys.VideoItem;
import com.xwj.xiamediaplayer.utils.CommonUtils;
import com.xwj.xiamediaplayer.utils.Constant;
import com.xwj.xiamediaplayer.views.VideoItemView;
import com.xwj.xiamediaplayer.views.impl.VideoPlayActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by xiaweijia on 16/3/16.
 */
public class VideoViewHolder extends RecyclerView.ViewHolder implements VideoItemView
//        , View.OnClickListener, View.OnLongClickListener
{

    private static final String TAG = VideoViewHolder.class.getSimpleName();
    public TextView mTvDisplayName;
    public TextView mTvDuration;
    public TextView mTvSize;
    public SimpleDraweeView mIvThumbnail;
    public ImageButton mIbMenu;
    private Context mContext;
    public VideoItem videoItem;
    private ArrayList<VideoItem> mList;
    private OnItemDeleteListener mOnItemDeleteListener;
    private SimpleDateFormat simpleDateFormat;

    public VideoViewHolder(View itemView, List<VideoItem> list) {
        super(itemView);
        mContext = itemView.getContext();
        mTvDisplayName = (TextView) itemView.findViewById(R.id.tv_item_name);
        mTvDuration = (TextView) itemView.findViewById(R.id.tv_item_duration);
        mTvSize = (TextView) itemView.findViewById(R.id.tv_item_size);
        mIvThumbnail = (SimpleDraweeView) itemView.findViewById(R.id.iv_video_item_thumbnail);
        mIbMenu = (ImageButton) itemView.findViewById(R.id.ib_item_menu);
        mList = new ArrayList<>();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        mList.addAll(list);
//        itemView.setOnClickListener(this);
//        itemView.setOnLongClickListener(this);
    }

    @Override
    public void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(mContext, clazz);
        intent.putExtra(Constant.BUNDLE_NAME, bundle);
        mContext.startActivity(intent);
    }

    @Override
    public void bindView(VideoItem item) {
        videoItem = item;
        mTvDuration.setText(CommonUtils.getTimeString(item.getVideoDuration()));
        this.mTvDisplayName.setText(item.getVideoName());
        this.mTvSize.setText(Formatter.formatFileSize(mContext, item.getSize()));
        if (this.mIvThumbnail != null) {
            this.mIvThumbnail.setImageURI(Uri.fromFile(new File(item.getDataUrl())));
        }

    }


    public void play() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.VIDEO_ITEM, videoItem);
        bundle.putSerializable(Constant.VIDEO_LIST, mList);
        bundle.putInt(Constant.VIDEO_POSITION, mList.indexOf(videoItem));
        startActivity(VideoPlayActivity.class, bundle);
    }


    public interface OnItemDeleteListener {
        void onItemDelete(VideoItem videoItem);
    }

    public void setOnItemDeleteListener(OnItemDeleteListener onItemDeleteListener) {
        mOnItemDeleteListener = onItemDeleteListener;
    }
}
