package com.lionel.gonews.data.local;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.lionel.gonews.data.News;

import java.util.List;

public class LocalNewsSource {
    private LocalNewsDao localNewsDao;

    public LocalNewsSource(Context context) {
        LocalNewsDatabase db = LocalNewsDatabase.getDatabase(context);
        localNewsDao = db.getLocalNewsDao();
    }

    public LiveData<List<News>> getAllHistoryNews() {
        return localNewsDao.getAllHistoryNews();
    }

    public LiveData<List<News>> getAllFavoriteNews() {
        return localNewsDao.getAllFavoriteNews();
    }

    public LiveData<Integer> checkIsFavorite(String title) {
        return localNewsDao.checkIsFavorite(title);
    }


    public void insertOrUpdateHistory(final News localNews) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                localNewsDao.insertOrUpdateHistory(localNews);
            }
        }).start();
    }

    public void updateIsFavorite(final News news) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                localNewsDao.updateIsFavorite(news.title);
            }
        }).start();
    }

    public void deleteAllHistory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                localNewsDao.deleteAllHistory();
            }
        }).start();
    }

    public void deleteFavorite(final String title) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                localNewsDao.deleteFavorite(title);
            }
        }).start();
    }
}
