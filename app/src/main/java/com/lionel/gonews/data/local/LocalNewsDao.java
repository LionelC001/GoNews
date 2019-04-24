package com.lionel.gonews.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.ABORT;
import static com.lionel.gonews.util.Constants.COLUMN_FAVORITE;
import static com.lionel.gonews.util.Constants.COLUMN_HISTORY;
import static com.lionel.gonews.util.Constants.COLUMN_TITLE;
import static com.lionel.gonews.util.Constants.TABLE_LOCAL_NEWS;

@Dao
public interface LocalNewsDao {

    @Query("SELECT * FROM " + TABLE_LOCAL_NEWS + " WHERE " + COLUMN_FAVORITE + " = 1 ")
    public LiveData<List<LocalNews>> getAllFavoriteNews();

    @Query("SELECT COUNT(*) FROM " + TABLE_LOCAL_NEWS +
            " WHERE " + COLUMN_FAVORITE + " = 1 AND " + COLUMN_TITLE + " = :title")
    public LiveData<Integer> checkIsFavoriteNews(String title);

    @Query("SELECT * FROM " + TABLE_LOCAL_NEWS + " WHERE " + COLUMN_HISTORY + " = 1")
    public LiveData<List<LocalNews>> getAllHistoryNews();

    @Update
    public void update(LocalNews localNews);

    @Insert(onConflict = ABORT)
    public void insert(LocalNews localNews);

    @Delete
    public void delete(List<LocalNews> listNews);
}
