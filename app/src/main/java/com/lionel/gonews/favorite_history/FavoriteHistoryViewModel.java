package com.lionel.gonews.favorite_history;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.lionel.gonews.data.News;
import com.lionel.gonews.data.local.LocalNewsSource;

import java.util.List;

public class FavoriteHistoryViewModel extends AndroidViewModel {
    private final LocalNewsSource localNewsSource;
    private LiveData<List<News>> newsData = new MutableLiveData<>();

    public FavoriteHistoryViewModel(@NonNull Application application) {
        super(application);

        localNewsSource = new LocalNewsSource(application.getApplicationContext());
    }

    public LiveData<List<News>> getFavoriteNews() {
//        newsData = localNewsSource.getAllFavoriteNews();
        return localNewsSource.getAllFavoriteNews();
    }

    public LiveData<List<News>> getHistoryNews() {
        return localNewsSource.getAllHistoryNews();
    }

    public LiveData<List<News>> getNewsData() {
        return newsData;
    }

    public void deleteAllHistoryNews() {
        localNewsSource.deleteAllHistory();
    }

}
