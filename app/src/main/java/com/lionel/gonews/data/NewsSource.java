package com.lionel.gonews.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public interface NewsSource {

    interface LoadNewsCallback {
        void onSuccess(List<News> newsList);

        void onFailed();
    }

    void getNews(@NonNull String newsType, @NonNull String country, @Nullable String category, @NonNull LoadNewsCallback callback);
}
