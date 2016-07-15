package com.xwj.xiamediaplayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.xwj.xiamediaplayer.R;
import com.xwj.xiamediaplayer.entitys.VideoItem;
import com.xwj.xiamediaplayer.utils.CommonUtils;
import com.xwj.xiamediaplayer.utils.Constant;
import com.xwj.xiamediaplayer.views.impl.VideoPlayActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaweijia on 16/3/23.
 */
public class VideoHeaderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_LAYOUT = 1;
    private static final int ITEM_LAYOUT = 2;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<VideoItem> mList;

    public VideoHeaderListAdapter(Context context, List<VideoItem> list) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = list;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == HEADER_LAYOUT) {
            View itemView = mInflater.inflate(R.layout.header_layout, parent, false);
            HeaderViewHolder headerViewHolder = new HeaderViewHolder(itemView);
            return headerViewHolder;
        } else if (viewType == ITEM_LAYOUT) {
            View itemView = mInflater.inflate(R.layout.video_item, parent, false);
            ListViewHolder listViewHolder = new ListViewHolder(itemView, mList);
            return listViewHolder;
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {

            if (holder instanceof HeaderViewHolder) {
                ((HeaderViewHolder) holder).mTvInitial.setText(Character.toString(mList.get(position).getInitialsOfVideoName()));
            } else {
                ((ListViewHolder) holder).mVideoItem = mList.get(position);
                ((ListViewHolder) holder).mTvDuration.setText(CommonUtils.getTimeString(mList.get(position).getVideoDuration()));
                ((ListViewHolder) holder).mTvDisplayName.setText(mList.get(position).getVideoName());
                ((ListViewHolder) holder).mTvSize.setText(Formatter.formatFileSize(mContext, mList.get(position).getSize()));

                String dataUrl = mList.get(position).getDataUrl();
                ((ListViewHolder) holder).mIvThumbnail.setImageURI(Uri.fromFile(new File(dataUrl)));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.isEmpty(mList.get(position).getDataUrl())) {
            return HEADER_LAYOUT;
        } else {
            return ITEM_LAYOUT;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView mTvInitial;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mTvInitial = (TextView) itemView.findViewById(R.id.tv_item_initial);
        }
    }


    static class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private static final String TAG = ListViewHolder.class.getSimpleName();
        public TextView mTvDisplayName;
        public TextView mTvDuration;
        public TextView mTvSize;
        public SimpleDraweeView mIvThumbnail;
        private Context mContext;
        public VideoItem mVideoItem;
        private ArrayList<VideoItem> mList;

        public ListViewHolder(View itemView, List<VideoItem> list) {
            super(itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mList = new ArrayList<>();
            mList.addAll(list);

            mTvDisplayName = (TextView) itemView.findViewById(R.id.tv_item_name);
            mTvDuration = (TextView) itemView.findViewById(R.id.tv_item_duration);
            mTvSize = (TextView) itemView.findViewById(R.id.tv_item_size);
            mIvThumbnail = (SimpleDraweeView) itemView.findViewById(R.id.iv_video_item_thumbnail);

        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                play();
            }
        }

        private void play() {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.VIDEO_ITEM, mVideoItem);
            bundle.putSerializable(Constant.VIDEO_LIST, mList);
            bundle.putInt(Constant.VIDEO_POSITION, mList.indexOf(mVideoItem));
            Intent intent = new Intent(mContext, VideoPlayActivity.class);
            intent.putExtra(Constant.BUNDLE_NAME, bundle);
            mContext.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {

            ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(mContext, R.layout.dialog_adapter, R.id.tv_item_dialog,
                    new String[]{
                            "删除", "播放"
                    });
            DialogPlus dialog = DialogPlus.newDialog(mContext)
                    .setAdapter(stringArrayAdapter)
                    .setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                            switch (position) {
                                case 0:
                                    Toast.makeText(mContext, mVideoItem.getDataUrl(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    break;
                                case 1:
                                    play();
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    })
//                    .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                    .create();
            dialog.show();
            return true;
        }
    }
}
