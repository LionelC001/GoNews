package com.lionel.gonews.headlines;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ViewDataBinding;
import android.util.Log;

import com.lionel.gonews.BR;
import com.lionel.gonews.data.INewsSource;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.QueryNews;
import com.lionel.gonews.data.remote.NewsRemoteSource;

import java.util.ArrayList;
import java.util.List;

import static com.lionel.gonews.util.Constants.US;

public class HeadlinesFragmentViewModel implements INewsSource.LoadNewsCallback {

    private List<News> cachedNews = new ArrayList<>();

    private final ViewDataBinding binding;
    private final NewsRemoteSource newsRemoteSource;

    public HeadlinesFragmentViewModel(Context context, ViewDataBinding binding) {
        this.binding = binding;
        this.newsRemoteSource = new NewsRemoteSource(context);
    }


    public void start(String category) {
        newsRemoteSource.queryNews(new QueryNews.QueryHeadlinesNews(US, category), this);
    }

    @Override
    public void onSuccess(int totalResults, List<News> newsList) {
        binding.setVariable(BR.dataNews, newsList);
    }

    @Override
    public void onFailed() {
        Log.d("<>", "onFailed");
    }
}
