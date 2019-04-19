package com.lionel.gonews.data.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lionel.gonews.R;
import com.lionel.gonews.data.INewsSource;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.remote.QueryFilter.QueryEverythingFilter;
import com.lionel.gonews.data.remote.QueryFilter.QueryHeadlinesFilter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.lionel.gonews.util.Constants.ENG;
import static com.lionel.gonews.util.Constants.EVERYTHING_ENDPOINT;
import static com.lionel.gonews.util.Constants.HEADLINES_ENDPOINT;
import static com.lionel.gonews.util.Constants.NEWS_API_KEY;
import static com.lionel.gonews.util.Constants.NEWS_TYPE_HEADLINES;
import static com.lionel.gonews.util.Constants.NODE_ARTICLES;
import static com.lionel.gonews.util.Constants.NODE_TOTAL_RESULTS;
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

public class NewsRemoteSource implements INewsSource {

    private final Context context;

    public NewsRemoteSource(Context context) {
        this.context = context;
    }

    @Override
    public void queryNews(@NonNull QueryFilter queryFilter,@NonNull IQueryNewsCallback callback) {
        String url;
        if (queryFilter.type.equals(NEWS_TYPE_HEADLINES)) {
            url = getHeadlinesUrl((QueryHeadlinesFilter) queryFilter);
        } else {
            url = getEverythingUrl((QueryEverythingFilter) queryFilter);
        }
        NewsRequest newsRequest = new NewsRequest(url, callback);
        Volley.newRequestQueue(context).add(newsRequest);
    }

    private String getHeadlinesUrl(QueryHeadlinesFilter queryHeadlines) {
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

    private String getEverythingUrl(QueryEverythingFilter queryEverything) {
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

//        url.append(QUERY_COUNTRY).append(US).append("&");
        url.append(QUERY_LANGUAGE).append(ENG).append("&");
        url.append(QUERY_PAGESIZE).append(PAGESIZE).append("&");
        url.append(NEWS_API_KEY);

        return url.toString();
    }

    private class NewsRequest extends JsonObjectRequest {
        NewsRequest(String url, final IQueryNewsCallback callback) {
            super(url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (callback != null && response != null) {
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
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error != null && error.networkResponse != null) {
                                Log.d("<>", new String(error.networkResponse.data));
                            }

                            String msg = "";

                            if (error instanceof NoConnectionError) {
                                Log.d("<>", "NoConnectionError");
                                msg = context.getString(R.string.no_connection_error);
                            } else if (error instanceof TimeoutError) {
                                Log.d("<>", "TimeoutError");
                                msg = context.getString(R.string.time_error);
                            } else if (error instanceof AuthFailureError) {
                                Log.d("<>", "AuthFailureError");
                            } else if (error instanceof ServerError) {
                                Log.d("<>", "ServerError");
                                msg = context.getString(R.string.server_error);
                            } else if (error instanceof NetworkError) {
                                Log.d("<>", "NetworkError");
                            } else if (error instanceof ParseError) {
                                Log.d("<>", "ParseError");
                            }
                            callback.onFailed(msg);
                        }
                    });
        }
    }
}