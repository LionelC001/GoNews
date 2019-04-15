package com.lionel.gonews.data.remote;

import android.arch.paging.PageKeyedDataSource;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.QueryNews;
import com.lionel.gonews.util.NewsRemoteUrlParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.lionel.gonews.util.Constants.NODE_ARTICLES;
import static com.lionel.gonews.util.Constants.NODE_TOTAL_RESULTS;

public class NewsRemoteSource extends PageKeyedDataSource<Integer, News> {

    private final Context context;
    private QueryNews queryObject;
    private final int initPage = 1;

    public NewsRemoteSource(Context context) {
        this.context = context;

    }

    public void setQueryCondition(QueryNews queryObject) {
        this.queryObject = queryObject;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, News> callback) {
        String url = NewsRemoteUrlParser.getUrl(queryObject);
        Request newsRequest = new Request(url, callback, initPage);
        Volley.newRequestQueue(context).add(newsRequest);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, News> callback) {
        // it is not necessary
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, News> callback) {
        String url = NewsRemoteUrlParser.getUrl(queryObject);
        Request newsRequest = new Request(url, callback, params.key);
        Volley.newRequestQueue(context).add(newsRequest);
    }

    private class Request extends JsonObjectRequest {
        Request(String url, final Object callback, final int page) {
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


                                    // for DataSource of Paging Library
                                    if (page == initPage && callback instanceof LoadInitialCallback) {
                                        ((LoadInitialCallback) callback).onResult(newsList, 0, totalResults, null, page + 1);
                                    } else if (callback instanceof LoadCallback) {
                                        ((LoadCallback) callback).onResult(newsList, page + 1);
                                    }
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
                        }
                    });
        }
    }
}