package com.lionel.gonews.headlines;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.lionel.gonews.base.BaseRemoteSourceViewModel;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.QueryNews;

import java.util.ArrayList;
import java.util.List;

public class HeadlinesFragViewModel extends BaseRemoteSourceViewModel {

    public HeadlinesFragViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<News>> getNewsData() {
        return super.getNewsData();
    }

    public MutableLiveData<Boolean> getIsLoadingLiveData() {
        return super.getIsLoadingLiveData();
    }

    public MutableLiveData<Boolean> getIsLastPageLiveData() {
        return super.getIsLastPageLiveData();
    }

    public MutableLiveData<Boolean> getIsErrorLiveData(){
        return super.getIsErrorLiveData();
    }

    public void setQueryCondition(String category) {
        QueryNews.QueryHeadlinesNews queryNews = new QueryNews.QueryHeadlinesNews(category, 1);
        super.setQueryCondition(queryNews);
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
