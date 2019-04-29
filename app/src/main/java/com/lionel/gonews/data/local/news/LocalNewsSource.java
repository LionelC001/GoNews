package com.lionel.gonews.data.local.news;

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

    public void insertOrUpdateHistory(final News news) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                localNewsDao.insertOrUpdateHistory(news);
            }
        }).start();
    }

    public void updateIsFavorite(final News news) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                localNewsDao.updateIsFavorite(news.title, news.favoriteDate);
            }
        }).start();
    }

    public LiveData<Integer> checkIsFavorite(News news) {
        return localNewsDao.checkIsFavorite(news.title);
    }

    public void deleteAllHistory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                localNewsDao.deleteAllHistory();
            }
        }).start();
    }

    public void deleteFavorite(final News news) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                localNewsDao.deleteFavorite(news.title);
            }
        }).start();
    }
}
