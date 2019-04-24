package com.lionel.gonews.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface LocalNewsDao {


    public List<LocalNews> getAllFavoriteNews();
}
