package com.xwj.xiamediaplayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xwj.xiamediaplayer.R;
import com.xwj.xiamediaplayer.entitys.VideoItem;

import java.util.List;

/**
 * Created by xwjsd on 2016-04-28.
 */
public class PlayVideoItemAdapter extends RecyclerView.Adapter<PlayVideoItemAdapter.PlayVideoHolder> {

    private Context mContext;
    private List<VideoItem> mList;
    private LayoutInflater mInflater;
    private OnPlayVideoItemClickListener mOnPlayVideoItemClickListener;


    public PlayVideoItemAdapter(Context context, List<VideoItem> list) {

        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mList = list;

    }

    @Override
    public PlayVideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.play_view_item, parent, false);
        PlayVideoHolder playVideoHolder = new PlayVideoHolder(itemView);
        return playVideoHolder;
    }

    @Override
    public void onBindViewHolder(PlayVideoHolder holder, final int position) {
        holder.mTvVideoName.setText(mList.get(position).getVideoName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnPlayVideoItemClickListener != null) {
                    mOnPlayVideoItemClickListener.onVideoItemClick(mList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class PlayVideoHolder extends RecyclerView.ViewHolder {
        public TextView mTvVideoName;
        public RelativeLayout mLlPlayItem;

        public PlayVideoHolder(View itemView) {
            super(itemView);
            mTvVideoName = (TextView) itemView.findViewById(R.id.tv_play_video_name);
            mLlPlayItem = (RelativeLayout) itemView.findViewById(R.id.ll_play_item);
        }

    }

    public interface OnPlayVideoItemClickListener {
        void onVideoItemClick(VideoItem videoItem);
    }

    public void setOnPlayVideoItemClickListener(OnPlayVideoItemClickListener onPlayVideoItemClickListener) {
        mOnPlayVideoItemClickListener = onPlayVideoItemClickListener;
    }
}
