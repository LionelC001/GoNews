package com.lionel.gonews.data.local.query_word;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;

public class QueryWordSource {
    private final QueryWordDao queryWordDao;

    public QueryWordSource(Context context) {
        QueryWordDatabase db = QueryWordDatabase.getDatabase(context);
        queryWordDao = db.getQueryWordDao();
    }

    public void insert(final QueryWord queryWord) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                queryWordDao.insert(queryWord);
            }
        }).start();
    }

    public LiveData<List<QueryWord>> getAll() {
        return queryWordDao.getAll();
    }

    public void deleteAll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                queryWordDao.deleteAll();
            }
        }).start();
    }
}
