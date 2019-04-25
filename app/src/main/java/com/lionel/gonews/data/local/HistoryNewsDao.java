package com.lionel.gonews.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.lionel.gonews.data.News;

import java.util.List;

import static com.lionel.gonews.util.Constants.TABLE_HISTORY_NEWS;

@Dao
public interface HistoryNewsDao {

    @Query("SELECT * FROM " + TABLE_HISTORY_NEWS)
    public LiveData<List<News>> getAllHistoryNews();

    @Insert
    public void insert(News historyNews);

    @Query("DELETE  FROM " + TABLE_HISTORY_NEWS)
    public void delete();
}
