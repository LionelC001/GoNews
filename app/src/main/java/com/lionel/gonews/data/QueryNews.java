package com.lionel.gonews.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.lionel.gonews.util.Constants.NEWS_TYPE_EVERYTHING;
import static com.lionel.gonews.util.Constants.NEWS_TYPE_HEADLINES;

/**
 * Condition object for query news
 */
public abstract class QueryNews {
    public String type;
    public String page;

    public void setPage(int page) {
        this.page = String.valueOf(page);
    }

    public static class QueryHeadlinesNews extends QueryNews {
        public String category;

        public QueryHeadlinesNews(@NonNull String category) {
            this.type = NEWS_TYPE_HEADLINES;
            this.category = category;
            this.page = String.valueOf(page);
        }
    }

    public static class QueryEverythingNews extends QueryNews {
        public String queryWord;
        public String dateFrom;
        public String dateTo;
        public String sortBy;

        public QueryEverythingNews(@NonNull String queryWord, @Nullable String sortBy, @Nullable String dateFrom, @Nullable String dateTo) {
            this.type = NEWS_TYPE_EVERYTHING;
            this.queryWord = queryWord;
            this.page = String.valueOf(page);
            this.sortBy = sortBy;
            this.dateFrom = dateFrom;
            this.dateTo = dateTo;
        }
    }
}
