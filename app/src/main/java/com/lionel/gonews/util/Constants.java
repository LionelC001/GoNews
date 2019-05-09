package com.lionel.gonews.util;

public class Constants {

    //About news API
    public static final String NEWS_TYPE_HEADLINES = "headlines";
    public static final String NEWS_TYPE_EVERYTHING = "everything";
    public static final String HEADLINES_ENDPOINT = "https://newsapi.org/v2/top-headlines";
    public static final String EVERYTHING_ENDPOINT = "https://newsapi.org/v2/everything";

    //Request params
    public static final String NEWS_API_KEY = "apiKey=";
    public static final String QUERY_COUNTRY = "country=";
    public static final String QUERY_LANGUAGE = "language=";
    public static final String QUERY_CATEGORY = "category=";
    public static final String QUERY_WORD = "q=";
    public static final String QUERY_PAGESIZE = "pageSize=";
    public static final String QUERY_PAGE = "page=";
    public static final String QUERY_SORTBY = "sortBy=";
    public static final String QUERY_DATEFROM = "from=";
    public static final String QUERY_DATETO = "to=";

    //Country
    public static final String US = "us";

    //Language
    public static final String ENG = "en";

    //Category
    public static final String GENERAL = "general";
    public static final String BUSINESS = "business";
    public static final String ENTERTAINMENT = "entertainment";
    public static final String HEALTH = "health";
    public static final String SCIENCE = "science";
    public static final String SPORTS = "sports";
    public static final String TECHNOLOGY = "technology";
    public static final String[] CATEGORYS = {GENERAL, BUSINESS, ENTERTAINMENT, HEALTH, SCIENCE, SPORTS, TECHNOLOGY};

    //Default page size =10
    public static final String PAGESIZE = "10";

    //SortBy
    public static final String RELEVANCY = "relevancy";
    public static final String POPULARITY = "popularity";
    public static final String PUBLISHEDAT = "publishedAt";

    //Parsing json from response body
    public static final String NODE_TOTAL_RESULTS = "totalResults";
    public static final String NODE_ARTICLES = "articles";

    //SwipeRefreshLayout
    public static final int DISTANCE_TO_SYNC = 400;
    public static final int END_POSITION = 300;

    //Date format pattern
    public static final String DATE_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_EEEE_MMMM_DD_YY = "EEEE, MMMM dd, yyyy";

    //Intent params
    public static final String NEWS_CONTENT = "news_content";
    public static final String TYPE_ACT = "type_act";
    public static final String TYPE_FAVORITE = "favorite_act";
    public static final String TYPE_HISTORY = "history_act";

    //SQLite
    public static final String DB_LOCAL_NEWS = "local_news.db";
    public static final String TABLE_LOCAL_NEWS = "local_news_table";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_HISTORY = "isHistory";
    public static final String COLUMN_FAVORITE = "isFavorite";
    public static final String COLUMN_BROWSE_DATE = "browseDate";
    public static final String COLUMN_FAVORITE_DATE = "favoriteDate";

    public static final String DB_QUERY_WORD = "query_word.db";
    public static final String TABLE_QUERY_WORD = "query_word_table";
    public static final String COLUMN_ID = "id";

    //savedInstanceState
    public static final String SEARCH_BOX_DROP_DOWN_STATE = "search_box_drop_down";
    public static final String POPUP_WINDOW_DATE_RANGE_STATE = "popup_window_date_range";
    public static final String POPUP_WINDOW_SORT_BY_STATE = "popup_window_sort_by";
}
