package com.lionel.gonews.headlines;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.lionel.gonews.data.INewsSource;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.QueryNews;
import com.lionel.gonews.data.remote.NewsRemoteSource;

import java.util.ArrayList;
import java.util.List;

public class HeadlinesFragmentViewModel extends AndroidViewModel implements INewsSource.LoadNewsCallback {

    public MutableLiveData<List<News>> newsData = new MutableLiveData<>();
    private List<News> cachedNewsData = new ArrayList<>();
    private final NewsRemoteSource newsRemoteSource;


    public HeadlinesFragmentViewModel(@NonNull Application application) {
        super(application);
        newsRemoteSource = new NewsRemoteSource(application.getApplicationContext());
    }

    public void getNews(String category, int page) {
        if (cachedNewsData != null && cachedNewsData.size() > 0) {
            newsData.setValue(cachedNewsData);
        } else {
            newsRemoteSource.queryNews(new QueryNews.QueryHeadlinesNews(category, page), this);
        }
    }

    @Override
    public void onSuccess(int totalResults, List<News> newsList) {
        newsData.setValue(newsList);
        cachedNewsData.addAll(newsList);
    }

    @Override
    public void onFailed() {
        Log.d("<>", "onFailed");
    }
}
