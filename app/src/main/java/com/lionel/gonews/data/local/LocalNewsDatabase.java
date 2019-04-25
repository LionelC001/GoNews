package com.lionel.gonews.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.lionel.gonews.data.News;

import static com.lionel.gonews.util.Constants.DB_LOCAL_NEWS;

@Database(entities = {FavoriteNews.class, News.class}, version = 1, exportSchema = false)
public abstract class LocalNewsDatabase extends RoomDatabase {
    private static LocalNewsDatabase INSTANCE;
    public static synchronized LocalNewsDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room
                    .databaseBuilder(context, LocalNewsDatabase.class, DB_LOCAL_NEWS)
                    .build();
        }
        return INSTANCE;
    }

    public abstract FavoriteNewsDao getFavoriteNewsDao();

    public abstract HistoryNewsDao getHistoryNewsDao();
}
