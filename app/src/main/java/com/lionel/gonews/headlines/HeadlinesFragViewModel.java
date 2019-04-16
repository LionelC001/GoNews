package com.lionel.gonews.headlines;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.lionel.gonews.base.BaseRemoteSourceViewModel;
import com.lionel.gonews.data.INewsSource;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.QueryNews;
import com.lionel.gonews.data.remote.NewsRemoteSource;

import java.util.ArrayList;
import java.util.List;

import static com.lionel.gonews.util.Constants.PAGESIZE;

public class HeadlinesFragViewModel extends BaseRemoteSourceViewModel {


    public MutableLiveData<List<News>> newsData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isInitLoading = new MutableLiveData<>();
    public MutableLiveData<Boolean> isMoreLoading = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLastPage = new MutableLiveData<>();


    private List<News> cachedNewsData = new ArrayList<>();
    private int maxPage = 0;
    private int currentPage = 1;


    public HeadlinesFragViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<News>> getNewsData() {
        return super.getNewsData();
    }

    public MutableLiveData<Boolean> getInitLoadingLiveData() {
        return super.getInitLoadingLiveData();
    }

    public MutableLiveData<Boolean> getLastPageLiveData() {
        return super.getLastPageLiveData();
    }

    public void setQueryCondition(String category) {
        QueryNews.QueryHeadlinesNews queryNews = new QueryNews.QueryHeadlinesNews(category, 1);
        super.setQueryCondition(queryNews);
    }

    public void initNews() {
        super.initNews();
    }

    public void loadMoreNews() {
        super.loadMoreNews();
    }

    public void reloadNews() {
        super.reloadNews();
    }

    public boolean getLoadingState() {
        return super.getLoadingState();
    }
}
