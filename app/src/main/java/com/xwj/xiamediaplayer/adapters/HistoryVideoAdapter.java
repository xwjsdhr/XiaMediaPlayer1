package com.xwj.xiamediaplayer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xwj.xiamediaplayer.R;
import com.xwj.xiamediaplayer.entitys.HistoryVideo;
import com.xwj.xiamediaplayer.entitys.VideoItem;
import com.xwj.xiamediaplayer.utils.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by xwjsd on 2016-05-05.
 */
public class HistoryVideoAdapter extends RecyclerView.Adapter<HistoryVideoAdapter.HistoryViewHolder> {

    private Context mContext;
    private List<HistoryVideo> mList;
    private LayoutInflater mLayoutInflater;
    private HistoryListener historyListener;
    private SimpleDateFormat simpleDateFormat;

    public HistoryVideoAdapter(Context context, List<HistoryVideo> historyVideoList) {

        mContext = context;
        mList = historyVideoList;
        mLayoutInflater = LayoutInflater.from(context);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);

    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.history_item, parent, false);
        HistoryViewHolder historyViewHolder = new HistoryViewHolder(itemView);
        return historyViewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, final int position) {
        HistoryVideo historyVideo = mList.get(position);
        VideoItem videoItem = historyVideo.getVideoItem();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (historyListener != null) {
                    historyListener.onHistoryClick(mList.get(position));
                }
            }
        });
        if (historyVideo.isPlayFinished()) {
            holder.mTvHasFinish.setText("已播放完成");
            holder.mTvHasFinish.setTextColor(Color.parseColor("#fc810f"));
            holder.mTvPlayTo.setVisibility(View.GONE);
        } else {
            holder.mTvHasFinish.setText("未播放完成");
            holder.mTvHasFinish.setTextColor(Color.GRAY);
            holder.mTvPlayTo.setVisibility(View.VISIBLE);
        }
        holder.mTvName.setText(videoItem.getVideoName());
        holder.mTvPlayTo.setText(String.format("已播放至 %s", CommonUtils.getTimeString(historyVideo.getPlayPos())));
        holder.mTvStartTime.setText(String.format("播放时间 %s",
                DateUtils.getRelativeTimeSpanString(mContext, historyVideo.getStartPlayTime().getTime())
        ));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        public TextView mTvName, mTvPlayTo, mTvHasFinish, mTvStartTime;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tv_item_history_name);
            mTvHasFinish = (TextView) itemView.findViewById(R.id.tv_item_history_has_play_finish);
            mTvPlayTo = (TextView) itemView.findViewById(R.id.tv_item_history_play_to);
            mTvStartTime = (TextView) itemView.findViewById(R.id.tv_item_history_play_video_time);
        }
    }

    public void setHistoryListener(HistoryListener listener) {
        historyListener = listener;
    }

    public interface HistoryListener {
        void onHistoryClick(HistoryVideo historyVideo);
    }
}
