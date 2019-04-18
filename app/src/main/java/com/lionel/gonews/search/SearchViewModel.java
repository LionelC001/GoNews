package com.lionel.gonews.search;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.lionel.gonews.base.BaseRemoteSourceViewModel;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.remote.QueryFilter;
import com.lionel.gonews.data.remote.ErrorInfo;

import java.util.List;

public class SearchViewModel extends BaseRemoteSourceViewModel {

    private QueryFilter.QueryHeadlinesFilter queryNews;

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

//    public void setQueryWord(String queryWord) {
//        if (queryNews == null) {
//            queryNews = new QueryFilter.QueryEverythingFilter(queryWord);
//        }
//        super.setQueryFilter(queryNews);
//    }

//    public void setFilter(String ){
//
//    }
    

    /**
     * every query do not need cache
     */
    public void initNewsWithoutCache() {
        super.initNewsWithoutCache();
    }

    public void loadMoreNews() {
        super.loadMoreNews();
    }
}
