package com.lionel.gonews.data;

import static com.lionel.gonews.util.Constants.NEWS_TYPE_EVERYTHING;
import static com.lionel.gonews.util.Constants.NEWS_TYPE_HEADLINES;

/**
 * Condition object for query news
 */
public abstract class QueryNews {
    public String type;

    public static class QueryHeadlinesNews extends QueryNews {
        public String category;
        public String country;

        public QueryHeadlinesNews(String country, String category) {
            this.type = NEWS_TYPE_HEADLINES;
            this.country = country;
            this.category = category;
        }
    }

    public static class QueryEverythingNews extends QueryNews {
        public String queryWord;
        public String page;
        public String dateFrom;
        public String dateTo;
        public String sortBy;

        public QueryEverythingNews(String queryWord, String page, String sortBy, String dateFrom, String dateTo) {
            this.type = NEWS_TYPE_EVERYTHING;
            this.queryWord = queryWord;
            this.page = page;
            this.sortBy = sortBy;
            this.dateFrom = dateFrom;
            this.dateTo = dateTo;
        }
    }
}
