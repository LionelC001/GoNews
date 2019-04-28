package com.lionel.gonews.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.lionel.gonews.data.News;

import java.util.List;

import static com.lionel.gonews.util.Constants.COLUMN_BROWSE_DATE;
import static com.lionel.gonews.util.Constants.COLUMN_FAVORITE;
import static com.lionel.gonews.util.Constants.COLUMN_FAVORITE_DATE;
import static com.lionel.gonews.util.Constants.COLUMN_HISTORY;
import static com.lionel.gonews.util.Constants.COLUMN_TITLE;
import static com.lionel.gonews.util.Constants.TABLE_LOCAL_NEWS;

@Dao
public abstract class LocalNewsDao {

    @Query("SELECT * FROM " + TABLE_LOCAL_NEWS + " WHERE " + COLUMN_HISTORY + " IS 1 ORDER BY " + COLUMN_BROWSE_DATE + " DESC ")
    public abstract LiveData<List<News>> getAllHistoryNews();

    @Query("SELECT * FROM " + TABLE_LOCAL_NEWS + " WHERE " + COLUMN_FAVORITE + " IS 1 ORDER BY " + COLUMN_FAVORITE_DATE + " DESC ")
    public abstract LiveData<List<News>> getAllFavoriteNews();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract long insertHistory(News localNews);

    @Query("UPDATE " + TABLE_LOCAL_NEWS + " SET " + COLUMN_HISTORY + " = 1, " + COLUMN_BROWSE_DATE + " = :browseDate  WHERE " + COLUMN_TITLE + " = :title ")
    abstract void updateISHistory(String title, String browseDate);

    @Transaction
    public void insertOrUpdateHistory(News localNews) {
        if (insertHistory(localNews) == -1) {
            updateISHistory(localNews.title, localNews.browseDate);
        }
    }

    @Query("UPDATE " + TABLE_LOCAL_NEWS + " SET " + COLUMN_FAVORITE + " = 1, " + COLUMN_FAVORITE_DATE + " = :date WHERE " + COLUMN_TITLE + " = :title AND " + COLUMN_FAVORITE + " = 0")
    public abstract void updateIsFavorite(String title, String date);

    @Query("SELECT COUNT(" + COLUMN_TITLE + ") FROM " + TABLE_LOCAL_NEWS + " WHERE " + COLUMN_TITLE + " = :title  AND " + COLUMN_FAVORITE + " = 1")
    public abstract LiveData<Integer> checkIsFavorite(String title);

    @Query("UPDATE " + TABLE_LOCAL_NEWS + " SET " + COLUMN_HISTORY + " = 0 ")
    abstract void updateAllNotHistory();

    @Query("UPDATE " + TABLE_LOCAL_NEWS + " SET " + COLUMN_FAVORITE + " = 0 WHERE " + COLUMN_TITLE + " = :title")
    abstract void updateNotFavorite(String title);

    @Query("DELETE FROM " + TABLE_LOCAL_NEWS + " WHERE " + COLUMN_HISTORY + " = 0 AND " + COLUMN_FAVORITE + " = 0")
    abstract void deleteAll();

    @Transaction
    public void deleteAllHistory() {
        updateAllNotHistory();
        deleteAll();
    }

    @Transaction
    public void deleteFavorite(String title) {
        updateNotFavorite(title);
        deleteAll();
    }
}
