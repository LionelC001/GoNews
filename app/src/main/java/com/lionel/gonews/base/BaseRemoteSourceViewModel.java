package com.lionel.gonews.base;

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

import static com.lionel.gonews.util.Constants.PAGESIZE;

/**
 * a bridge between View and NewsRemoteSource.
 *
 * <p>
 * you must call {@link #setQueryCondition(QueryNews)} before initNews() or reloadNews().
 * notice that, {@link #initNews()} should be called once at beginning,
 * after that, if u want query new result, u should use {@link #reloadNews()} instead.
 * </p>
 *
 * <p>
 * you will need to observe the states via LiveData to control your view.
 * use these method to get LiveData..
 * {@link #getNewsData()},{@link #getInitLoadingLiveData()},{@link #getLastPageLiveData()}
 * </p>
 */
public abstract class BaseRemoteSourceViewModel extends AndroidViewModel implements INewsSource.LoadNewsCallback {
    private final INewsSource newsRemoteSource;

    private MutableLiveData<List<News>> newsData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isInitLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isMoreLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLastPage = new MutableLiveData<>();

    private QueryNews queryNews;
    private List<News> cachedNewsData = new ArrayList<>();
    private int maxPage = 0;
    private int currentPage = 1;

    public BaseRemoteSourceViewModel(@NonNull Application application) {
        super(application);
        newsRemoteSource = new NewsRemoteSource(application.getApplicationContext());

        isInitLoading.setValue(false);
        isMoreLoading.setValue(false);
        isLastPage.setValue(false);
    }

    public MutableLiveData<List<News>> getNewsData() {
        return newsData;
    }

    public MutableLiveData<Boolean> getInitLoadingLiveData() {
        return isInitLoading;
    }

    public MutableLiveData<Boolean> getLastPageLiveData() {
        return isLastPage;
    }

    public void setQueryCondition(QueryNews queryNews) {
        this.queryNews = queryNews;
    }

    /**
     * start to query news
     * notice: if you need a new result without any cache, call {@link #reloadNews()} instead.
     */
    public void initNews() {
        if (cachedNewsData != null && cachedNewsData.size() > 0) {
            newsData.setValue(cachedNewsData);
        } else {
            if (queryNews != null) {
                queryNews.setPage(1);
                loadNews(queryNews);
                isInitLoading.setValue(true);
            }
        }
    }

    public void loadMoreNews() {
        currentPage += 1;
        queryNews.setPage(currentPage);
        loadNews(queryNews);
        isMoreLoading.setValue(true);
    }

    public void reloadNews() {
        cachedNewsData = new ArrayList<>();
        currentPage = 1;
        isLastPage.setValue(false);
        initNews();
    }

    private void loadNews(QueryNews queryNews) {
        if (!isLastPage.getValue()) {
            newsRemoteSource.queryNews(queryNews, this);
        }
    }

    public boolean getLoadingState() {
        return isInitLoading.getValue() || isMoreLoading.getValue();
    }

    @Override
    public void onSuccess(int totalSize, List<News> newsList) {
        checkIsLastPage(totalSize);  // this line must be called before newsData.setValue()
        closeLoadingState();

        cachedNewsData.addAll(newsList);
        newsData.setValue(cachedNewsData);
    }

    private void checkIsLastPage(int totalSize) {
        getMaxPage(totalSize);
        if (maxPage <= currentPage) {
            isLastPage.setValue(true);
        }
    }

    private void getMaxPage(int totalSize) {
        if (maxPage == 0) {
            int pageSize = Integer.valueOf(PAGESIZE);
            if (totalSize % pageSize != 0) {
                maxPage = totalSize / pageSize + 1;
            } else {
                maxPage = totalSize / pageSize;
            }
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
