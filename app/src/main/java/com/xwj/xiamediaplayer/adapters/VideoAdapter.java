package com.xwj.xiamediaplayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.xwj.xiamediaplayer.R;
import com.xwj.xiamediaplayer.entitys.VideoItem;

import java.util.List;

/**
 * Created by xiaweijia on 16/3/15.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {

    private Context mContext;
    private List<VideoItem> mVideoItemList;
    private LayoutInflater mInflater;
    private OnVideoItemClickListener mOnVideoItemClickListener;
    private OnVideoItemLongClickListener mOnVideoItemLongClickListener;

    ArrayAdapter<String> mStringArrayAdapter;
    private int mLayoutId;

    public VideoAdapter(Context context, List<VideoItem> videoItemList, int layoutId) {
        this.mContext = context;
        this.mVideoItemList = videoItemList;
        mInflater = LayoutInflater.from(context);
        mStringArrayAdapter = new ArrayAdapter<String>(mContext, R.layout.dialog_adapter, R.id.tv_item_dialog,
                new String[]{
                        "删除", "播放"
                });
        this.mLayoutId = layoutId;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(mLayoutId, parent, false);
        return new VideoViewHolder(itemView, mVideoItemList);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, final int position) {
        holder.bindView(mVideoItemList.get(position));
        holder.videoItem = mVideoItemList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnVideoItemClickListener != null) {
                    mOnVideoItemClickListener.onVideoItemClick(mVideoItemList.get(position));
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnVideoItemLongClickListener != null) {
                    mOnVideoItemLongClickListener.onVideoItemLongClick(mVideoItemList.get(position));
                }
                return true;
            }
        });
        if (holder.mIbMenu != null) {
            holder.mIbMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnVideoItemClickListener != null) {
                        mOnVideoItemClickListener.onMenuBtnClick(mVideoItemList.get(position));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mVideoItemList.size();
    }

    public interface OnVideoItemClickListener {
        void onVideoItemClick(VideoItem videoItem);

        void onMenuBtnClick(VideoItem videoItem);
    }

    public void setOnVideoItemClickListener(OnVideoItemClickListener onVideoItemClickListener) {
        mOnVideoItemClickListener = onVideoItemClickListener;
    }

    public interface OnVideoItemLongClickListener {
        boolean onVideoItemLongClick(VideoItem videoItem);
    }

    public void setOnVideoItemLongClickListener(OnVideoItemLongClickListener onVideoItemLongClickListener) {
        this.mOnVideoItemLongClickListener = onVideoItemLongClickListener;
    }
}

