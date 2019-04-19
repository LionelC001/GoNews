package com.lionel.gonews.base;

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

import static com.lionel.gonews.util.Constants.TYPE_BACKGROUND;
import static com.lionel.gonews.util.Constants.TYPE_BOTTOM;
import static com.lionel.gonews.util.Constants.TYPE_NEWS;

public class BaseRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private IDisplayNewsList.IDisplayNewsListCallback callback;
    private List<News> data = new ArrayList<>();
    private boolean isLastPage = false;

    public BaseRecyclerViewAdapter() {
        setHasStableIds(true);  // avoid blink after call notifyDataSetChanged()
    }

    public void setItemNewsClickCallback(IDisplayNewsList.IDisplayNewsListCallback callback) {
        this.callback = callback;
    }

    public void setData(List<News> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
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

    private class BackgroundHolder extends RecyclerView.ViewHolder {

        BackgroundHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_NEWS) {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_recyclerview_news, viewGroup, false);
            return new NewsHolder(binding);
        } else if (viewType == TYPE_BOTTOM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerview_bottom, viewGroup, false);
            return new BottomHolder(view);
        } else {  //show background as there is no news
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerview_background, viewGroup, false);
            return new BackgroundHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder newsHolder, int position) {
        if (getItemViewType(position) == TYPE_NEWS) {
            ((NewsHolder) newsHolder).getBinding().setVariable(BR.itemDataNews, data.get(position));
            ((NewsHolder) newsHolder).getBinding().getRoot().setOnClickListener(new OnItemClickListener(data.get(position)));
        } else if (getItemViewType(position) == TYPE_BACKGROUND) {
            // nothing to bind for background
        } else if (getItemViewType(position) == TYPE_BOTTOM) {
            // nothing to bind for bottom
        }
    }

    @Override
    public int getItemCount() {
        if (data.size() == 0) {
            return 1;  //  for background view
        }
        return (!isLastPage && data.size() != 0) ? data.size() + 1 : data.size();  // +1 for show loading progress
    }

    @Override
    public int getItemViewType(int position) {
        if (data.size() == 0) {
            return TYPE_BACKGROUND;
        }
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
                callback.onIntentToNewsContent(item);
            }
        }
    }
}
