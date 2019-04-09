package com.lionel.gonews.data.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.NewsSource;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.lionel.gonews.Constants.EVERYTHING_END_POINT;
import static com.lionel.gonews.Constants.HEADLINES_END_POINT;
import static com.lionel.gonews.Constants.NEWS_API_KEY;
import static com.lionel.gonews.Constants.NEWS_TYPE_HEADLINES;
import static com.lionel.gonews.Constants.NODE_ARTICLES;
import static com.lionel.gonews.Constants.SEARCH_CATEGORY;
import static com.lionel.gonews.Constants.SEARCH_COUNTRY;

public class NewsRemoteSource implements NewsSource {

    private final Context context;

    public NewsRemoteSource(Context context) {
        this.context = context;
    }


    @Override
    public void getNews(@NonNull String newsType, @NonNull String country, @Nullable String category, @NonNull LoadNewsCallback callback) {
        String url = getUrl(newsType, country, category);
        NewsRequest newsRequest = new NewsRequest(url, callback);
        Volley.newRequestQueue(context).add(newsRequest);
    }

    private String getUrl(String newsType, String country, String category) {
        StringBuilder url = new StringBuilder();

        if (newsType.equals(NEWS_TYPE_HEADLINES)) {
            url.append(HEADLINES_END_POINT);
        } else {
            url.append(EVERYTHING_END_POINT);
        }

        if (country != null) {
            url.append(SEARCH_COUNTRY).append(country);
        }

        if (category != null) {
            url.append(SEARCH_CATEGORY).append(category);
        }

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
                                JSONObject newsJsonObject = response.getJSONObject(NODE_ARTICLES);
                                String news = gson.toJson(newsJsonObject);
                                List<News> newsList = gson.fromJson(news, new TypeToken<List<News>>() {
                                }.getType());

                                callback.onSuccess(newsList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            callback.onFailed();
                        }
                    });
        }
    }
}
