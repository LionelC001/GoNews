package com.lionel.gonews.util;

import com.lionel.gonews.data.QueryNews;

import static com.lionel.gonews.util.Constants.ENG;
import static com.lionel.gonews.util.Constants.EVERYTHING_ENDPOINT;
import static com.lionel.gonews.util.Constants.HEADLINES_ENDPOINT;
import static com.lionel.gonews.util.Constants.NEWS_API_KEY;
import static com.lionel.gonews.util.Constants.NEWS_TYPE_HEADLINES;
import static com.lionel.gonews.util.Constants.PAGESIZE;
import static com.lionel.gonews.util.Constants.QUERY_CATEGORY;
import static com.lionel.gonews.util.Constants.QUERY_COUNTRY;
import static com.lionel.gonews.util.Constants.QUERY_DATEFROM;
import static com.lionel.gonews.util.Constants.QUERY_DATETO;
import static com.lionel.gonews.util.Constants.QUERY_LANGUAGE;
import static com.lionel.gonews.util.Constants.QUERY_PAGE;
import static com.lionel.gonews.util.Constants.QUERY_PAGESIZE;
import static com.lionel.gonews.util.Constants.QUERY_SORTBY;
import static com.lionel.gonews.util.Constants.QUERY_WORD;
import static com.lionel.gonews.util.Constants.US;

public class NewsRemoteUrlParser {

    public static String getUrl(QueryNews queryObject){
        String url;
        if (queryObject.type.equals(NEWS_TYPE_HEADLINES)) {
            url = getHeadlinesUrl((QueryNews.QueryHeadlinesNews) queryObject);
        } else {
            url = getEverythingUrl((QueryNews.QueryEverythingNews) queryObject);
        }

        return url;
    }

    private static String getHeadlinesUrl(QueryNews.QueryHeadlinesNews queryHeadlines) {
        StringBuilder url = new StringBuilder();

        url.append(HEADLINES_ENDPOINT).append("?");

        if (queryHeadlines.category != null) {
            url.append(QUERY_CATEGORY).append(queryHeadlines.category).append("&");
        }

        if (queryHeadlines.page != null) {
            url.append(QUERY_PAGE).append(queryHeadlines.page).append("&");
        }

        url.append(QUERY_COUNTRY).append(US).append("&");
        url.append(QUERY_LANGUAGE).append(ENG).append("&");
        url.append(QUERY_PAGESIZE).append(PAGESIZE).append("&");
        url.append(NEWS_API_KEY);

        return url.toString();
    }

    private static String getEverythingUrl(QueryNews.QueryEverythingNews queryEverything) {
        StringBuilder url = new StringBuilder();

        url.append(EVERYTHING_ENDPOINT).append("?");

        if (queryEverything.queryWord != null) {
            url.append(QUERY_WORD).append(queryEverything.queryWord).append("&");
        }

        if (queryEverything.page != null) {
            url.append(QUERY_PAGE).append(queryEverything.page).append("&");
        }

        if (queryEverything.sortBy != null) {
            url.append(QUERY_SORTBY).append(queryEverything.sortBy).append("&");
        }

        if (queryEverything.dateFrom != null) {
            url.append(QUERY_DATEFROM).append(queryEverything.dateFrom).append("&");
        }

        if (queryEverything.dateTo != null) {
            url.append(QUERY_DATETO).append(queryEverything.dateTo).append("&");
        }

        url.append(QUERY_COUNTRY).append(US).append("&");
        url.append(QUERY_LANGUAGE).append(ENG).append("&");
        url.append(QUERY_PAGESIZE).append(PAGESIZE).append("&");
        url.append(NEWS_API_KEY);

        return url.toString();
    }
}
