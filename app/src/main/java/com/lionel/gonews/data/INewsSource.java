package com.lionel.gonews.data;

import android.support.annotation.NonNull;

import java.util.List;

public interface INewsSource {

    interface LoadNewsCallback {
        void onSuccess(int totalSize, List<News> newsList);

        void onFailed();
    }

    void queryNews(@NonNull QueryNews queryObject, @NonNull LoadNewsCallback callback);
}
