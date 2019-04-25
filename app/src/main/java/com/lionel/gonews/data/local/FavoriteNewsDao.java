package com.lionel.gonews.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import static com.lionel.gonews.util.Constants.TABLE_FAVORITE_NEWS;

@Dao
public interface FavoriteNewsDao {

    @Query("SELECT * FROM " + TABLE_FAVORITE_NEWS)
    public LiveData<List<FavoriteNews>> getAllFavoriteNews();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(FavoriteNews favoriteNews);

    @Delete
    public void delete(FavoriteNews favoriteNews);
}
