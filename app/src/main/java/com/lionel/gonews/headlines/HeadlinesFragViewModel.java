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

    private final String category;
    private final INewsSource newsRemoteSource;

    public MutableLiveData<List<News>> newsData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isInitLoading = new MutableLiveData<>();
    public MutableLiveData<Boolean> isMoreLoading = new MutableLiveData<>();

    private boolean isLastPage = false;

    private List<News> cachedNewsData = new ArrayList<>();
    private int currentPage = 1;


    public HeadlinesFragViewModel(Application application, String category) {
        super();
        this.category = category;
        newsRemoteSource = new NewsRemoteSource(application.getApplicationContext());

        isInitLoading.setValue(false);
        isMoreLoading.setValue(false);
    }

    public void initNews() {
        if (cachedNewsData != null && cachedNewsData.size() > 0) {
            newsData.setValue(cachedNewsData);
        } else {
            loadNews(1);
            isInitLoading.setValue(true);
        }
    }

    public void loadMoreNews() {
        currentPage += 1;
        loadNews(currentPage);
        isMoreLoading.setValue(true);
    }

    public void reloadNews() {
        cachedNewsData = new ArrayList<>();
        currentPage = 1;
        isLastPage = false;
        initNews();
    }

    public void loadNews(int page) {
        if (!isLastPage) {
            newsRemoteSource.queryNews(new QueryNews.QueryHeadlinesNews(category, page), this);
        }
    }

    public boolean getLoadingState() {
        return isInitLoading.getValue() || isMoreLoading.getValue();
    }

    @Override
    public void onSuccess(int totalSize, List<News> newsList) {
        cachedNewsData.addAll(newsList);
        newsData.setValue(cachedNewsData);

        checkIsLastPage(newsList.size());
        closeLoadingState();
    }

    private void checkIsLastPage(int currentSize) {
        int pageSize = Integer.valueOf(PAGESIZE);
        if (currentSize < pageSize) {
            isLastPage = true;
        }
    }

    private void closeLoadingState() {
        if (isInitLoading.getValue()) {
            isInitLoading.setValue(false);
        }
        if (isMoreLoading.getValue()) {
            isMoreLoading.setValue(false);
        }
    }

    @Override
    public void onFailed() {
        closeLoadingState();
        Log.d("<>", "onFailed");
    }
}
