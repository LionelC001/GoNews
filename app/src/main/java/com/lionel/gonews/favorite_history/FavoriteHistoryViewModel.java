package com.lionel.gonews.favorite_history;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.lionel.gonews.data.News;
import com.lionel.gonews.data.local.news.LocalNewsSource;

import java.util.List;

public class FavoriteHistoryViewModel extends AndroidViewModel {
    private final LocalNewsSource localNewsSource;

    public FavoriteHistoryViewModel(@NonNull Application application) {
        super(application);
        localNewsSource = new LocalNewsSource(application.getApplicationContext());
    }

    public LiveData<List<News>> getFavoriteNews() {
        return localNewsSource.getAllFavoriteNews();
    }

    public LiveData<List<News>> getHistoryNews() {
        return localNewsSource.getAllHistoryNews();
    }

    public void deleteAllHistoryNews() {
        localNewsSource.deleteAllHistory();
    }
}
