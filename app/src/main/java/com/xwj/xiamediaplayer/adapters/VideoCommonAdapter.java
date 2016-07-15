package com.xwj.xiamediaplayer.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.format.Formatter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xwj.xiamediaplayer.R;
import com.xwj.xiamediaplayer.entitys.VideoItem;
import com.xwj.xiamediaplayer.utils.CommonUtils;

import java.io.File;
import java.util.List;

/**
 * Created by xwjsd on 2016-05-05.
 */
public class VideoCommonAdapter extends CommonRecyclerAdapter<VideoItem> {

    private Context mContext;
    private int mLayoutId;

    public VideoCommonAdapter(Context context, List<VideoItem> list, int layoutId) {
        super(context, list);
        mLayoutId = layoutId;
        mContext = context;
    }

    @Override
    protected int getItemLayoutId() {
        return mLayoutId;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, VideoItem videoItem) {
        switch (mLayoutId) {
            case R.layout.video_item:
                TextView mTvDisplayName = (TextView) holder.getView(R.id.tv_item_name);
                TextView mTvDuration = (TextView) holder.getView(R.id.tv_item_duration);
                TextView mTvSize = (TextView) holder.getView(R.id.tv_item_size);
                SimpleDraweeView mIvThumbnail = (SimpleDraweeView) holder.getView(R.id.iv_video_item_thumbnail);

                mTvDuration.setText(CommonUtils.getTimeString(videoItem.getVideoDuration()));
                mTvDisplayName.setText(videoItem.getVideoName());
                mTvSize.setText(Formatter.formatFileSize(mContext, videoItem.getSize()));
                mIvThumbnail.setImageURI(Uri.fromFile(new File(videoItem.getDataUrl())));
                break;
            case R.layout.video_item_small_image:
                mTvDisplayName = (TextView) holder.getView(R.id.tv_item_name);
                mTvDuration = (TextView) holder.getView(R.id.tv_item_duration);
                mTvSize = (TextView) holder.getView(R.id.tv_item_size);

                mTvDuration.setText(CommonUtils.getTimeString(videoItem.getVideoDuration()));
                mTvDisplayName.setText(videoItem.getVideoName());
                mTvSize.setText(Formatter.formatFileSize(mContext, videoItem.getSize()));
                break;
        }
    }
}
