package com.lionel.gonews.data.local.query_word;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import static com.lionel.gonews.util.Constants.COLUMN_ID;
import static com.lionel.gonews.util.Constants.TABLE_QUERY_WORD;

@Dao
public interface QueryWordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(QueryWord queryWord);

    @Query("SELECT * FROM " + TABLE_QUERY_WORD + " ORDER BY " + COLUMN_ID + " DESC")
    LiveData<List<QueryWord>> getAll();

    @Query("DELETE FROM " + TABLE_QUERY_WORD)
    void deleteAll();
}
