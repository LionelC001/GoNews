package com.lionel.gonews.data.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.lionel.gonews.util.Constants.NEWS_TYPE_EVERYTHING;
import static com.lionel.gonews.util.Constants.NEWS_TYPE_HEADLINES;

/**
 * Filter object for query news
 * <p>
 * notice:
 * {@link QueryEverythingFilter}'s {@param dateFrom} and {@param dateTo},
 * should use ISO-8601 pattern "yyyy-MM-dd".
 */
public abstract class QueryFilter {
    public String type;
    public String page;

    public void setPage(int page) {
        this.page = String.valueOf(page);
    }

    public static class QueryHeadlinesFilter extends QueryFilter {
        public String category;

        public QueryHeadlinesFilter(@NonNull String category) {
            this.type = NEWS_TYPE_HEADLINES;
            this.category = category;
            this.page = String.valueOf(page);
        }
    }

    public static class QueryEverythingFilter extends QueryFilter {
        public String queryWord;
        public String dateFrom;
        public String dateTo;
        public String sortBy;

        public QueryEverythingFilter(@NonNull String queryWord, @Nullable String sortBy, @Nullable String dateFrom, @Nullable String dateTo) {
            this.type = NEWS_TYPE_EVERYTHING;
            this.queryWord = queryWord;
            this.sortBy = sortBy;
            this.dateFrom = dateFrom;
            this.dateTo = dateTo;
        }
    }
}
