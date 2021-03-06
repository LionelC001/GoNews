package com.lionel.gonews.base.recyclerview;

import com.lionel.gonews.data.News;

import java.util.List;

public interface IDisplayNewsList {
    interface IDisplayNewsListCallback {
        void onRefreshNews();

        void onLoadMoreNews();

        void onIntentToNewsContent(News news);
    }

    void setCallback(IDisplayNewsListCallback callback);

    void showNews(List<News> data);

    /**
     * for History
     */
    void showNewsWithDateGroup(List<News> data);

    void setIsEnableRefreshLayout(boolean isEnable);

    void setIsShowLoadingAnimAtBeginning(boolean isShowing);

    void showLoadingAnimAgain();

    void setIsShowLoadingNextPageAnim(boolean isLastPage);

    void setIsLoadingState(boolean isLoading);

    void setIsErrorState(boolean isError);
    }
