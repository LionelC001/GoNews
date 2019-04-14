package com.lionel.gonews.data;

import static com.lionel.gonews.util.Constants.NEWS_TYPE_EVERYTHING;
import static com.lionel.gonews.util.Constants.NEWS_TYPE_HEADLINES;

/**
 * Condition object for query news
 */
public abstract class QueryNews {
    public String type;
    public String page;

    public static class QueryHeadlinesNews extends QueryNews {
        public String category;

        public QueryHeadlinesNews(String category, int page) {
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

        public QueryEverythingNews(String queryWord, int page, String sortBy, String dateFrom, String dateTo) {
            this.type = NEWS_TYPE_EVERYTHING;
            this.queryWord = queryWord;
            this.page = String.valueOf(page);
            this.sortBy = sortBy;
            this.dateFrom = dateFrom;
            this.dateTo = dateTo;
        }
    }
}
