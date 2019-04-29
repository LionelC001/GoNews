package com.lionel.gonews.data.local.query_word;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import static com.lionel.gonews.util.Constants.DB_QUERY_WORD;

@Database(entities = {QueryWord.class}, version = 1, exportSchema = false)
public abstract class QueryWordDatabase extends RoomDatabase {
    private static QueryWordDatabase INSTANCE;

    public static synchronized QueryWordDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room
                    .databaseBuilder(context, QueryWordDatabase.class, DB_QUERY_WORD)
                    .build();
        }
        return INSTANCE;
    }

    public abstract QueryWordDao getQueryWordDao();
}
