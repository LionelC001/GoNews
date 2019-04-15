package com.lionel.gonews.headlines;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.lionel.gonews.data.News;
import com.lionel.gonews.data.QueryNews;
import com.lionel.gonews.data.remote.NewsRemoteSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import static com.lionel.gonews.util.Constants.PAGESIZE;

public class HeadlinesFragViewModel extends ViewModel {


    public MutableLiveData<List<News>> newsData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLastPage = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private List<News> cachedNewsData = new ArrayList<>();
    private final String category;
    private final NewsRemoteSource newsRemoteSource;
    private int page;


    public HeadlinesFragViewModel(Application application, String category) {
        super();
        this.category = category;
        newsRemoteSource = new NewsRemoteSource(application.getApplicationContext());
        isLastPage.setValue(false);
        page = 0;
    }

    public PagedList<News> initNews() {
        newsRemoteSource.setQueryCondition(new QueryNews.QueryHeadlinesNews(category, page));

        int pageSize = Integer.parseInt(PAGESIZE);
        PagedList.Config config = new PagedList.Config.Builder()
                // Number of items to fetch at once. [Required]
                .setPageSize(pageSize)
//                .setEnablePlaceholders(true) // Show empty views until data is available
                .build();

        MainThreadExecutor executor = new MainThreadExecutor();

        PagedList<News> list =
                new PagedList.Builder<>(newsRemoteSource, config) // Can pass `pageSize` directly instead of `config`
                        // Do fetch operations on the main thread. We'll instead be using Retrofit's
                        // built-in enqueue() method for background api calls.
                        .setFetchExecutor(executor)
                        // Send updates on the main thread
                        .setNotifyExecutor(executor)
                        .build();
        return list;
    }

//    public void initNews() {
//        if (cachedNewsData != null && cachedNewsData.size() > 0) {
//            newsData.setValue(cachedNewsData);
//        } else {
//            loadNews(1);
//        }
//    }
//
//    public void loadMoreNews() {
//        loadNews(++page);
//    }
//
//    public void reloadNews(){
//        cachedNewsData  = new ArrayList<>();
//        loadNews(1);
//    }
//
//    public void loadNews(int page) {
//        Log.d("<>", "loadnews: " + category );
//        isLoading.setValue(true);
//        newsRemoteSource.queryNews(new QueryNews.QueryHeadlinesNews(category, page), this);
//    }

//    @Override
//    public void onSuccess(int totalResults, List<News> newsList) {
//        if (isLastPage.getValue() != null && !isLastPage.getValue()) {
//            newsData.setValue(newsList);
//            cachedNewsData.addAll(newsList);
//            isLoading.setValue(false);
//
//            checkIsLastPage(newsList.size());
//        }
//    }

    private void checkIsLastPage(int currentSize) {
        if (currentSize < Integer.valueOf(PAGESIZE)) {
            isLastPage.setValue(true);
        }
    }

//    @Override
//    public void onFailed() {
//        isLoading.setValue(false);
//        Log.d("<>", "onFailed");
//    }

    private class MainThreadExecutor implements Executor {
        private final Handler mHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mHandler.post(command);
        }
    }
}


