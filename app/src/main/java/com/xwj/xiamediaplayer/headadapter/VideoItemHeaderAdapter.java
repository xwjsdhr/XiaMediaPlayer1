package com.xwj.xiamediaplayer.headadapter;

import android.content.Context;
import android.net.Uri;
import android.text.format.Formatter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.promeg.pinyinhelper.Pinyin;
import com.xwj.xiamediaplayer.R;
import com.xwj.xiamediaplayer.entitys.VideoItem;
import com.xwj.xiamediaplayer.utils.CommonUtils;

import java.io.File;
import java.util.Comparator;
import java.util.List;

/**
 * Created by xwjsd on 2016-04-28.
 */
public class VideoItemHeaderAdapter extends BaseHeaderAdapter<VideoItem> {

    private Context mContext;

    public VideoItemHeaderAdapter(Context context, List<VideoItem> list, Comparator<VideoItem> comparator) {
        super(context, list, comparator);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.video_item;
    }

    @Override
    protected void bindItemView(BaseHeaderAdapter<VideoItem>.ContenViewHolder holder, VideoItem videoItem) {
        holder.bindContentText(R.id.tv_item_name, videoItem.getVideoName());
        holder.bindContentText(R.id.tv_item_duration, CommonUtils.getTimeString(videoItem.getVideoDuration()));
        holder.bindContentText(R.id.tv_item_size, Formatter.formatFileSize(mContext, videoItem.getSize()));

        SimpleDraweeView mIvThumbnail = (SimpleDraweeView) holder.getViewById(R.id.iv_video_item_thumbnail);
        mIvThumbnail.setImageURI(Uri.fromFile(new File(videoItem.getDataUrl())));
    }

    @Override
    protected long getHeaderTypeId(VideoItem videoItem) {
        String videoName = videoItem.getVideoName();
        char c1 = videoName.toCharArray()[0];
        if (Pinyin.isChinese(c1)) {
            return Character.toUpperCase(Pinyin.toPinyin(c1).toCharArray()[0]);
        } else {
            return Character.toUpperCase(c1);
        }
    }


    @Override
    protected int getHeaderLayoutId() {
        return R.layout.header_layout;
    }

    @Override
    protected void bindHeaderView(BaseHeaderAdapter<VideoItem>.HeaderViewHolder holder, VideoItem videoItem) {
        char initial = videoItem.getVideoName().toCharArray()[0];
        char initialchar = ' ';
        if (Pinyin.isChinese(initial)) {
            String initialStr = Pinyin.toPinyin(initial);
            initialchar = initialStr.toCharArray()[0];
        } else {
            initialchar = initial;
        }
        holder.bindContentText(R.id.tv_item_initial, Character.toString(initialchar));
    }
}
