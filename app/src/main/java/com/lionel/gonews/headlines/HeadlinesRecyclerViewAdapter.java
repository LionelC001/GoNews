package com.lionel.gonews.headlines;

import android.arch.paging.PagedListAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lionel.gonews.BR;
import com.lionel.gonews.R;
import com.lionel.gonews.data.News;

import java.util.ArrayList;
import java.util.List;

public class HeadlinesRecyclerViewAdapter extends PagedListAdapter<News, HeadlinesRecyclerViewAdapter.NewsHolder> {

    private List<News> data = new ArrayList<>();

    public HeadlinesRecyclerViewAdapter() {
        super(DIFF_CALLBACK);
//        setHasStableIds(true);  // avoid blink after call notifyDataSetChanged()
    }

//    public void setData(List<News> data) {
//        this.data = data;
//        notifyDataSetChanged();
//    }

    public class NewsHolder extends RecyclerView.ViewHolder {

        private final ViewDataBinding binding;

        public NewsHolder(ViewDataBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_recyclerview_headlines, viewGroup, false);
        return new NewsHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder newsHolder, int i) {
//        newsHolder.getBinding().setVariable(BR.itemDataNews, data.get(i));
        newsHolder.getBinding().setVariable(BR.itemDataNews, getItem(i));
    }
//
//    @Override
//    public int getItemCount() {
//        return data != null ? data.size() : 0;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return data.get(position).title.hashCode();  // a unique id for call setHasStableIds()
//    }


    private static final DiffUtil.ItemCallback<News> DIFF_CALLBACK = new DiffUtil.ItemCallback<News>() {

        // Check if items represent the same thing.
        @Override
        public boolean areItemsTheSame(News oldItem, News newItem) {
            return oldItem.title.equals(newItem.title);
        }

        // Checks if the item contents have changed.
        @Override
        public boolean areContentsTheSame(News oldItem, News newItem) {
            return true; // Assume Repository details don't change
        }
    };
}
