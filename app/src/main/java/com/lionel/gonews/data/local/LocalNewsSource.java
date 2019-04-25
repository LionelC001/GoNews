package com.lionel.gonews.data.local;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.lionel.gonews.data.News;

import java.util.List;

public class LocalNewsSource {
    private HistoryNewsDao historyNewsDao;
    private FavoriteNewsDao favoriteNewsDao;

    public LocalNewsSource(Context context) {
        LocalNewsDatabase db = LocalNewsDatabase.getDatabase(context);
        favoriteNewsDao = db.getFavoriteNewsDao();
        historyNewsDao = db.getHistoryNewsDao();
    }

    public LiveData<List<FavoriteNews>> getAllFavoriteNews() {
        return favoriteNewsDao.getAllFavoriteNews();
    }

    public void insertFavorite(final FavoriteNews favoriteNews) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                favoriteNewsDao.insert(favoriteNews);
            }
        }).start();
    }

    public void deleteFavorite(final FavoriteNews news) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                favoriteNewsDao.delete(news);
            }
        }).start();
    }


    public LiveData<List<News>> getAllHistoryNews() {
        return historyNewsDao.getAllHistoryNews();
    }

    public void insertHistory(final News historyNews) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                historyNewsDao.insert(historyNews);
            }
        }).start();
    }

    public void deleteAllHistory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                historyNewsDao.delete();
            }
        }).start();
    }

}
