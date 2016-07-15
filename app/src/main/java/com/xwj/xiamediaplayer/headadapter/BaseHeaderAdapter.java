package com.xwj.xiamediaplayer.headadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by xwjsd on 2016-04-28.
 */
public abstract class BaseHeaderAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyRecyclerHeadersAdapter {

    private List<T> mList;
    private LayoutInflater mInflater;

    public BaseHeaderAdapter(Context context, List<T> list, Comparator<T> comparator) {
        mInflater = LayoutInflater.from(context);
        mList = list;
        Collections.sort(mList, comparator);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentItem = mInflater.inflate(getContentLayoutId(), parent, false);
        ContenViewHolder contenViewHolder = new ContenViewHolder(contentItem);
        return contenViewHolder;
    }

    protected abstract int getContentLayoutId();

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindItemView((ContenViewHolder) holder, mList.get(position));
    }

    protected abstract void bindItemView(ContenViewHolder holder, T t);

    @Override
    public long getHeaderId(int position) {
        return getHeaderTypeId(mList.get(position));
    }

    protected abstract long getHeaderTypeId(T t);

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {

        View headerItemView = mInflater.inflate(getHeaderLayoutId(), parent, false);
        HeaderViewHolder headerViewHolder = new HeaderViewHolder(headerItemView);
        return headerViewHolder;
    }

    protected abstract int getHeaderLayoutId();


    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindHeaderView((HeaderViewHolder) holder, mList.get(position));
    }

    protected abstract void bindHeaderView(HeaderViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ContenViewHolder extends RecyclerView.ViewHolder {

        private View mItemView;

        public ContenViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
        }

        public View getViewById(int viewId) {
            return itemView.findViewById(viewId);
        }

        public void bindContentText(int viewId, String text) {
            View view = getViewById(viewId);
            if (view instanceof TextView) {
                ((TextView) view).setText(text);
            } else if (view instanceof Button) {
                ((Button) view).setText(text);
            }
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        private View mItemView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
        }

        public View getViewById(int viewId) {
            return itemView.findViewById(viewId);
        }

        public void bindContentText(int viewId, String text) {
            View view = getViewById(viewId);
            if (view instanceof TextView) {
                ((TextView) view).setText(text);
            } else if (view instanceof Button) {
                ((Button) view).setText(text);
            }
        }
    }

}
