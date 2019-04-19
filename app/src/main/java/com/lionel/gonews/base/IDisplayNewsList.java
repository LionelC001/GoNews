package com.lionel.gonews.base;

import com.lionel.gonews.data.News;

import java.util.List;

public interface IDisplayNewsList {
    interface IDisplayNewsListCallback {
        void onRefreshNews();

        void onLoadMoreNews();

        void onIntentToNewsContent(News news);
    }

    void setCallback(IDisplayNewsListCallback callback);

    void setIsLoading(boolean isLoading);

    void setIsLastPage(boolean isLastPage);

    void setIsError(boolean isError);

    void showNews(List<News> data);

    void showRefreshingAtBeginning(boolean isShowing);

    void showRefreshingAgain();
}
