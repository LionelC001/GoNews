package com.lionel.gonews.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

public class LocalNewsSource {
    private LocalNewsDao localNewsDao;

    public LocalNewsSource(Context context) {
        LocalNewsDatabase db = LocalNewsDatabase.getDatabase(context);
        localNewsDao = db.getLocalNewsDao();
    }

    public LiveData<List<LocalNews>> getAllFavoriteNews() {
        return localNewsDao.getAllFavoriteNews();
    }

    public boolean checkIsFavoriteNewsExist(String title) {
        LiveData<Integer> results = localNewsDao.checkIsFavoriteNewsExist(title);
        return results.getValue() != null && results.getValue() > 0;
    }

    public LiveData<List<LocalNews>> getAllHistoryNews() {
        return localNewsDao.getAllHistoryNews();
    }

    public LiveData<Integer> checkIsHistoryNewsExist(String title) {
        return localNewsDao.checkIsHistoryNewsExist(title);
    }

    public void update(final LocalNews localNews) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                localNewsDao.update(localNews);
            }
        }).start();
    }

    public void insert(final LocalNews localNews) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                localNewsDao.insert(localNews);
            }
        }).start();
    }

    public void delete(final List<LocalNews> listNews) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                localNewsDao.delete(listNews);
            }
        }).start();
    }
}
