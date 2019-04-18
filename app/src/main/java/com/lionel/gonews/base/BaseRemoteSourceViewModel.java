package com.lionel.gonews.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

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
 * you must call {@link #setQueryCondition(QueryNews)} before initNews() or initNewsWithoutCache().
 * </p>
 *
 * <p>
 * you will need to observe the states via LiveData to control your view.
 * use these method to get LiveData:
 * {@link #getNewsDataLiveData()},
 * {@link #getTotalSizeLiveData()},
 * {@link #getIsLoadingLiveData()},
 * {@link #getIsLastPageLiveData()},
 * {@link #getIsErrorLiveData()}.
 * </p>
 */
public abstract class BaseRemoteSourceViewModel extends AndroidViewModel implements INewsSource.LoadNewsCallback {
    private final INewsSource newsRemoteSource;

    private MutableLiveData<List<News>> newsData = new MutableLiveData<>();
    private MutableLiveData<Integer> newsDataTotalSize = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLastPage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isError = new MutableLiveData<>();

    private QueryNews queryNews;
    private List<News> cachedNewsData = new ArrayList<>();
    private int maxPage = 0;
    private int currentPage = 1;

    protected BaseRemoteSourceViewModel(@NonNull Application application) {
        super(application);
        newsRemoteSource = new NewsRemoteSource(application.getApplicationContext());

        isLoading.setValue(false);
        isLastPage.setValue(false);
        isError.setValue(false);
    }

    protected MutableLiveData<List<News>> getNewsDataLiveData() {
        return newsData;
    }

    protected MutableLiveData<Integer> getTotalSizeLiveData() {
        return newsDataTotalSize;
    }

    protected MutableLiveData<Boolean> getIsLoadingLiveData() {
        return isLoading;
    }

    protected MutableLiveData<Boolean> getIsLastPageLiveData() {
        return isLastPage;
    }

    protected MutableLiveData<Boolean> getIsErrorLiveData() {
        return isError;
    }

    protected void setQueryCondition(QueryNews queryNews) {
        this.queryNews = queryNews;
    }

    /**
     * start to query news.
     * notice: if you need a new result without any cache, call {@link #initNewsWithoutCache()} instead.
     */
    protected void initNews() {
        if (cachedNewsData != null && cachedNewsData.size() > 0) {
            newsData.setValue(cachedNewsData);
        } else {
            if (queryNews != null) {
                currentPage = 1;
                queryNews.setPage(currentPage);
                isLastPage.setValue(false);
                loadNews(queryNews);
            }
        }
    }

    /**
     * for refresh and query
     */
    protected void initNewsWithoutCache() {
        cachedNewsData = new ArrayList<>();
        initNews();
    }

    protected void loadMoreNews() {
        if (!isLastPage.getValue()) {
            currentPage += 1;
            queryNews.setPage(currentPage);
            loadNews(queryNews);
        }
    }

    private void loadNews(QueryNews queryNews) {
        isLoading.setValue(true);
        newsRemoteSource.queryNews(queryNews, this);
    }

    @Override
    public void onSuccess(int totalSize, List<News> newsList) {
        checkIsLastPage(totalSize);  // this line must be called before newsData.setValue()

        cachedNewsData.addAll(newsList);
        newsData.setValue(cachedNewsData);
        newsDataTotalSize.setValue(totalSize);
        isLoading.setValue(false);
        isError.setValue(false);
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

    @Override
    public void onFailed() {
        isLoading.setValue(false);
        isError.setValue(true);
    }
}
