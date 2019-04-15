package com.lionel.gonews.headlines;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lionel.gonews.BR;
import com.lionel.gonews.R;
import com.lionel.gonews.data.News;

import java.util.ArrayList;
import java.util.List;

public class HeadlinesRecyclerViewAdapter extends RecyclerView.Adapter<HeadlinesRecyclerViewAdapter.NewsHolder> {

    private List<News> data = new ArrayList<>();

    public HeadlinesRecyclerViewAdapter() {
        setHasStableIds(true);  // avoid blink after call notifyDataSetChanged()
    }

    public void setData(List<News> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

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
        newsHolder.getBinding().setVariable(BR.itemDataNews, data.get(i));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).title.hashCode();  // a unique id for call setHasStableIds()
    }
}
