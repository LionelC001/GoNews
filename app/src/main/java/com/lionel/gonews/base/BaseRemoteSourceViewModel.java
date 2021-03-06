package com.lionel.gonews.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.lionel.gonews.data.INewsSource;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.remote.ErrorInfo;
import com.lionel.gonews.data.remote.QueryFilter;
import com.lionel.gonews.data.remote.RemoteNewsSource;

import java.util.ArrayList;
import java.util.List;

import static com.lionel.gonews.util.Constants.PAGESIZE;

/**
 * a bridge between View and RemoteNewsSource.
 *
 * <p>
 * you must call {@link #setQueryFilter(QueryFilter)} before {@link #initNews()} or  {@link #initNewsWithoutCache()}.
 * </p>
 *
 * <p>
 * you will need to observe the states via LiveData to control your view.
 * use these method to get LiveData:
 * {@link #getNewsDataLiveData()},
 * {@link #getTotalSizeLiveData()},
 * {@link #getIsLoadingLiveData()},
 * {@link #getIsLastPageLiveData()},
 * {@link #getErrorInfoLiveData()}.
 * </p>
 */
public abstract class BaseRemoteSourceViewModel extends AndroidViewModel implements INewsSource.IQueryNewsCallback {
    private final INewsSource newsRemoteSource;

    private MutableLiveData<List<News>> newsData = new MutableLiveData<>();
    private MutableLiveData<Integer> newsDataTotalSize = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLastPage = new MutableLiveData<>();
    private MutableLiveData<ErrorInfo> errorInfo = new MutableLiveData<>();

    private QueryFilter queryFilter;
    private List<News> cachedNewsData = new ArrayList<>();
    private int maxPage = 0;
    private int currentPage = 1;

    protected BaseRemoteSourceViewModel(@NonNull Application application) {
        super(application);
        newsRemoteSource = new RemoteNewsSource(application.getApplicationContext());

        isLoading.setValue(false);
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

    protected MutableLiveData<ErrorInfo> getErrorInfoLiveData() {
        return errorInfo;
    }

    protected void setQueryFilter(QueryFilter queryFilter) {
        this.queryFilter = queryFilter;
    }

    /**
     * start to query news.
     * notice: if you need a new result without any cache, call {@link #initNewsWithoutCache()} instead.
     */
    protected void initNews() {
        if (cachedNewsData != null && cachedNewsData.size() > 0) {
            newsData.setValue(cachedNewsData);
        } else {
            if (queryFilter != null) {
                currentPage = 1;
                maxPage = 0;
                queryFilter.setPage(currentPage);
                isLastPage.setValue(false);
                loadNews(queryFilter);
            }
        }
    }

    /**
     * for refresh or search
     */
    protected void initNewsWithoutCache() {
        cachedNewsData = new ArrayList<>();
        initNews();
    }

    protected void loadMoreNews() {
        if (!isLastPage.getValue()) {
            currentPage += 1;
            queryFilter.setPage(currentPage);
            loadNews(queryFilter);
        }
    }

    private void loadNews(QueryFilter queryFilter) {
        if (!isLoading.getValue()) {
            isLoading.setValue(true);
            newsRemoteSource.queryNews(queryFilter, this);
        }
    }

    @Override
    public void onSuccess(int totalSize, List<News> newsList) {
        checkIsLastPage(totalSize);  // this line must be called before newsData.setValue()

        cachedNewsData.addAll(newsList);
        newsData.setValue(cachedNewsData);
        newsDataTotalSize.setValue(totalSize);
        isLoading.setValue(false);
        errorInfo.setValue(handleErrorInfo(false, null));
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
    public void onFailed(String msg) {
        isLoading.setValue(false);
        errorInfo.setValue(handleErrorInfo(true, msg));
    }

    private ErrorInfo handleErrorInfo(boolean isError, String msg) {
        ErrorInfo info = errorInfo.getValue();
        if (info == null) {
            info = new ErrorInfo(false, null);
        }
        info.isError = isError;
        info.msg = msg;
        return info;
    }
}
