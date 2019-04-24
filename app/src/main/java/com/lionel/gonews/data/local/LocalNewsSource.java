package com.lionel.gonews.data.local;

import android.arch.lifecycle.LiveData;
import android.content.Context;

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

    public boolean checkIsFavoriteNews(String title) {
        LiveData<Integer> results = localNewsDao.checkIsFavoriteNews(title);
        return results.getValue() != null && results.getValue() > 0;
    }

    public LiveData<List<LocalNews>> getAllHistoryNews() {
        return localNewsDao.getAllHistoryNews();
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
