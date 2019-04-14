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
    public MutableLiveData<Boolean> isLastPage = new MutableLiveData<>();

    private List<News> cachedNewsData = new ArrayList<>();
    private final String category;
    private final INewsSource newsRemoteSource;
    private int page ;


    public HeadlinesFragViewModel(Application application, String category) {
        super();
        this.category = category;
        newsRemoteSource = new NewsRemoteSource(application.getApplicationContext());
        isLastPage.setValue(false);
        page = 0;
    }

    public void initNews() {
        if (cachedNewsData != null && cachedNewsData.size() > 0) {
            newsData.setValue(cachedNewsData);
        } else {
            loadNews(1);
        }
    }

    public void loadMoreNews() {
        loadNews(++page);
    }

    public void reloadNews(){
        cachedNewsData  = new ArrayList<>();
        loadNews(1);
    }

    public void loadNews(int page) {
        Log.d("<>", "loadnews: " + category );
        newsRemoteSource.queryNews(new QueryNews.QueryHeadlinesNews(category, page), this);
    }

    @Override
    public void onSuccess(int totalResults, List<News> newsList) {
        if (isLastPage.getValue() != null && !isLastPage.getValue()) {
            newsData.setValue(newsList);
            cachedNewsData.addAll(newsList);

            checkIsLastPage(newsList.size());
        }
    }

    private void checkIsLastPage(int currentSize) {
        if (currentSize < Integer.valueOf(PAGESIZE)) {
            isLastPage.setValue(true);
        }
    }

    @Override
    public void onFailed() {
        Log.d("<>", "onFailed");
    }
}
