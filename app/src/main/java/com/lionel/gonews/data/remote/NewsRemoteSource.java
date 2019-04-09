package com.lionel.gonews.data.remote;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.NewsSource;
import com.lionel.gonews.data.QueryNews;
import com.lionel.gonews.data.QueryNews.QueryEverythingNews;
import com.lionel.gonews.data.QueryNews.QueryHeadlinesNews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.lionel.gonews.Constants.DEFAULT_PAGESIZE;
import static com.lionel.gonews.Constants.EVERYTHING_ENDPOINT;
import static com.lionel.gonews.Constants.HEADLINES_ENDPOINT;
import static com.lionel.gonews.Constants.NEWS_API_KEY;
import static com.lionel.gonews.Constants.NEWS_TYPE_HEADLINES;
import static com.lionel.gonews.Constants.NODE_ARTICLES;
import static com.lionel.gonews.Constants.NODE_TOTAL_RESULTS;
import static com.lionel.gonews.Constants.QUERY_CATEGORY;
import static com.lionel.gonews.Constants.QUERY_COUNTRY;
import static com.lionel.gonews.Constants.QUERY_DATEFROM;
import static com.lionel.gonews.Constants.QUERY_DATETO;
import static com.lionel.gonews.Constants.QUERY_PAGE;
import static com.lionel.gonews.Constants.QUERY_PAGESIZE;
import static com.lionel.gonews.Constants.QUERY_SORTBY;
import static com.lionel.gonews.Constants.QUERY_WORD;

public class NewsRemoteSource implements NewsSource {

    private final Context context;

    public NewsRemoteSource(Context context) {
        this.context = context;
    }

    @Override
    public void queryNews(QueryNews queryObject, LoadNewsCallback callback) {
        String url;
        if (queryObject.type.equals(NEWS_TYPE_HEADLINES)) {
            url = getHeadlinesUrl((QueryHeadlinesNews) queryObject);
        } else {
            url = getEverythingUrl((QueryEverythingNews) queryObject);
        }
        NewsRequest newsRequest = new NewsRequest(url, callback);
        Volley.newRequestQueue(context).add(newsRequest);
    }

    private String getHeadlinesUrl(QueryHeadlinesNews queryHeadlines) {
        StringBuilder url = new StringBuilder();

        url.append(HEADLINES_ENDPOINT).append("?");

        if (queryHeadlines.country != null) {
            url.append(QUERY_COUNTRY).append(queryHeadlines.country).append("&");
        }

        if (queryHeadlines.category != null) {
            url.append(QUERY_CATEGORY).append(queryHeadlines.category).append("&");
        }

        url.append(NEWS_API_KEY);

        return url.toString();
    }

    private String getEverythingUrl(QueryEverythingNews queryEverything) {
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

        url.append(QUERY_PAGESIZE).append(DEFAULT_PAGESIZE).append("&");
        url.append(NEWS_API_KEY);

        return url.toString();
    }

    private class NewsRequest extends JsonObjectRequest {
        NewsRequest(String url, final LoadNewsCallback callback) {
            super(url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Gson gson = new Gson();
                            try {
                                int totalResults = response.getInt(NODE_TOTAL_RESULTS);
                                JSONArray newsJsonArray = response.getJSONArray(NODE_ARTICLES);
                                List<News> newsList = gson.fromJson(newsJsonArray.toString(), new TypeToken<List<News>>() {
                                }.getType());

                                callback.onSuccess(totalResults, newsList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("<>", new String(error.networkResponse.data));
                            callback.onFailed();
                        }
                    });
        }
    }
}