package com.lionel.gonews.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import static com.lionel.gonews.util.Constants.TABLE_FAVORITE_NEWS;

@Dao
public interface FavoriteNewsDao {

    @Query("SELECT * FROM " + TABLE_FAVORITE_NEWS)
    LiveData<List<FavoriteNews>> getAllFavoriteNews();

    @Query("SELECT COUNT(title) FROM " + TABLE_FAVORITE_NEWS + " WHERE title is :title ")
    LiveData<Integer> getFavoriteNewsCount(String title);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FavoriteNews favoriteNews);

    @Query("DELETE FROM " + TABLE_FAVORITE_NEWS + " WHERE  title is :title")
    void delete(String title);
}
