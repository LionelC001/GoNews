package com.lionel.gonews.headlines;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.lionel.gonews.base.BaseRemoteSourceViewModel;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.remote.QueryFilter;
import com.lionel.gonews.data.remote.ErrorInfo;

import java.util.List;

public class HeadlinesFragViewModel extends BaseRemoteSourceViewModel {

    private QueryFilter.QueryHeadlinesFilter queryHeadlinesFilter;

    public HeadlinesFragViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<News>> getNewsDataLiveData() {
        return super.getNewsDataLiveData();
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

    public void setQueryCategory(String category) {
        if (queryHeadlinesFilter == null) {
            queryHeadlinesFilter = new QueryFilter.QueryHeadlinesFilter(category);
        }
        queryHeadlinesFilter.category = category;
        super.setQueryFilter(queryHeadlinesFilter);
    }

    public void initNews() {
        super.initNews();
    }

    public void initNewsWithoutCache() {
        super.initNewsWithoutCache();
    }

    public void loadMoreNews() {
        super.loadMoreNews();
    }
}
