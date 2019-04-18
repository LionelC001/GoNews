package com.lionel.gonews.data;

import android.support.annotation.NonNull;

import com.lionel.gonews.data.remote.QueryFilter;

import java.util.List;

public interface INewsSource {

    interface IQueryNewsCallback {
        void onSuccess(int totalSize, List<News> newsList);

        void onFailed(String msg);
    }

    void queryNews(@NonNull QueryFilter queryFilter, @NonNull IQueryNewsCallback callback);
}
