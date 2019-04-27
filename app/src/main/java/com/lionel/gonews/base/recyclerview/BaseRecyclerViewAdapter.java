package com.lionel.gonews.base.recyclerview;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lionel.gonews.BR;
import com.lionel.gonews.R;
import com.lionel.gonews.data.News;

import java.util.ArrayList;
import java.util.List;

import static com.lionel.gonews.util.Constants.TYPE_BOTTOM;
import static com.lionel.gonews.util.Constants.TYPE_NEWS;

/**
 * An adapter for {@link BaseDisplayNewsListView}.
 * manage tasks like:
 * (1) show background wallpaper when there is no items.
 * (2) show/hide loading next page animation.
 * (3) call callback as items be clicked.
 */
public class BaseRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface IRecyclerViewAdapterCallback {
        void onItemClick(News news);
    }

    private IRecyclerViewAdapterCallback callback;
    private List<News> data = new ArrayList<>();
    private boolean isShowLoadingNextPageAnim = false;

    public BaseRecyclerViewAdapter() {
        setHasStableIds(true);  // avoid blink after call notifyDataSetChanged()
    }

    public void setCallback(IRecyclerViewAdapterCallback callback) {
        this.callback = callback;
    }

    public void setData(List<News> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setIsShowLoadingNextPageAnim(boolean isShow) {
        this.isShowLoadingNextPageAnim = isShow;
    }

    private class NewsHolder extends RecyclerView.ViewHolder {

        private final ViewDataBinding binding;

        NewsHolder(ViewDataBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        ViewDataBinding getBinding() {
            return binding;
        }
    }

    private class BottomHolder extends RecyclerView.ViewHolder {

        BottomHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_NEWS) {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_recyclerview_news, viewGroup, false);
            return new NewsHolder(binding);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerview_bottom, viewGroup, false);
            return new BottomHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder newsHolder, int position) {
        if (getItemViewType(position) == TYPE_NEWS) {
            ((NewsHolder) newsHolder).getBinding().setVariable(BR.newsData, data.get(position));
            ((NewsHolder) newsHolder).getBinding().getRoot().setOnClickListener(new OnItemClickListener(data.get(position)));
        } else if (getItemViewType(position) == TYPE_BOTTOM) {
            // nothing to bind for bottom
        }
    }

    @Override
    public int getItemCount() {
        return (!isShowLoadingNextPageAnim && data.size() != 0) ? data.size() + 1 : data.size();  // +1 for show loading progress
    }

    @Override
    public int getItemViewType(int position) {
        if (position < data.size()) {
            return TYPE_NEWS;
        } else {
            return TYPE_BOTTOM;
        }
    }

    @Override
    public long getItemId(int position) {
        if (position < data.size()) {
            return data.get(position).title.hashCode();  // a unique id for call setHasStableIds()
        } else {
            return position;    // the last view, loading progress
        }
    }

    private class OnItemClickListener implements View.OnClickListener {

        private final News item;

        OnItemClickListener(News item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            if (callback != null) {
                callback.onItemClick(item);
            }
        }
    }
}
