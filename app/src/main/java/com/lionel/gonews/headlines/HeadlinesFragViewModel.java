package com.lionel.gonews.headlines;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.lionel.gonews.data.INewsSource;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.QueryNews;
import com.lionel.gonews.data.remote.NewsRemoteSource;

import java.util.ArrayList;
import java.util.List;

import static com.lionel.gonews.util.Constants.PAGESIZE;

public class HeadlinesFragViewModel extends ViewModel implements INewsSource.LoadNewsCallback {


    public MutableLiveData<List<News>> newsData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isInitLoading = new MutableLiveData<>();
    public boolean isLastPage = false;

    private List<News> cachedNewsData = new ArrayList<>();
    private final String category;
    private final INewsSource newsRemoteSource;
    private int page =1;


    public HeadlinesFragViewModel(Application application, String category) {
        super();
        this.category = category;
        newsRemoteSource = new NewsRemoteSource(application.getApplicationContext());
    }

    public void initNews() {
        if (cachedNewsData != null && cachedNewsData.size() > 0) {
            newsData.setValue(cachedNewsData);
        } else {
            loadNews(1);
        }
    }

    public void loadMoreNews() {
        page += 1;
        loadNews(page);
    }

    public void reloadNews() {
        cachedNewsData = new ArrayList<>();
        loadNews(1);
    }

    public void loadNews(int page) {
        Log.d("<>", "loadnews at " + page + ": " + category);
        if (!isLastPage) {
            isInitLoading.setValue(true);
            newsRemoteSource.queryNews(new QueryNews.QueryHeadlinesNews(category, page), this);
        }
    }

    @Override
    public void onSuccess(int totalSize, List<News> newsList) {
//        Log.d("<>", "totalResults " + category + ": " + totalSize);
//        Log.d("<>", "newList size: " + newsList.size());
        newsData.setValue(newsList);
        cachedNewsData.addAll(newsList);
        isInitLoading.setValue(false);
        checkIsLastPage(newsList.size());
    }

    private void checkIsLastPage(int currentSize) {
        Log.d("<>", "currentSize: " + currentSize);
        int pageSize = Integer.valueOf(PAGESIZE);
        if (currentSize < pageSize) {
            isLastPage = true;
        }
    }

    @Override
    public void onFailed() {
        isInitLoading.setValue(false);
        Log.d("<>", "onFailed");
    }
}
