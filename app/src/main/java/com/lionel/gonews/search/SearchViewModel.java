package com.lionel.gonews.search;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lionel.gonews.base.BaseRemoteSourceViewModel;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.remote.ErrorInfo;
import com.lionel.gonews.data.remote.QueryFilter;

import java.util.List;

public class SearchViewModel extends BaseRemoteSourceViewModel {

    private QueryFilter.QueryEverythingFilter queryEverythingFilter;

    public SearchViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<News>> getNewsDataLiveData() {
        return super.getNewsDataLiveData();
    }

    public MutableLiveData<Integer> getTotalCountLiveData() {
        return super.getTotalSizeLiveData();
    }

    public MutableLiveData<Boolean> getIsLoadingLiveData() {
        return super.getIsLoadingLiveData();
    }

    public MutableLiveData<Boolean> getIsLastPageLiveData() {
        return super.getIsLastPageLiveData();
    }

    public MutableLiveData<ErrorInfo> getErrorInfoLiveData() {
        return super.getErrorInfoLiveData();
    }

    public void setQueryWord(String queryWord) {
        if (queryEverythingFilter == null) {
            queryEverythingFilter = new QueryFilter.QueryEverythingFilter(queryWord, null, null, null);
        }
        queryEverythingFilter.queryWord = queryWord;
        super.setQueryFilter(queryEverythingFilter);
    }

    public void setFilter(@Nullable String sortBy, @Nullable String dateFrom, @Nullable String dateTo) {
        if (queryEverythingFilter != null) {
            queryEverythingFilter.sortBy = sortBy;
            queryEverythingFilter.dateFrom = dateFrom;
            queryEverythingFilter.dateTo = dateTo;

            super.setQueryFilter(queryEverythingFilter);
        }
    }

    /**
     * every query should without cache
     */
    public void initNewsWithoutCache() {
        super.initNewsWithoutCache();
    }

    public void loadMoreNews() {
        super.loadMoreNews();
    }
}
