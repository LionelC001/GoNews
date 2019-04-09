package com.lionel.gonews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lionel.gonews.data.News;
import com.lionel.gonews.data.NewsSource;
import com.lionel.gonews.data.remote.NewsRemoteSource;

import java.util.List;

import static com.lionel.gonews.Constants.COUNTRY_US;
import static com.lionel.gonews.Constants.NEWS_TYPE_HEADLINES;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        testGetNews();
    }

    private void testGetNews() {
        Log.d("<>", "start test getting news");
        NewsRemoteSource newsRemoteSource = new NewsRemoteSource(this);
        NewsSource.LoadNewsCallback callback = new NewsSource.LoadNewsCallback() {
            @Override
            public void onSuccess(List<News> newsList) {
                Log.d("<>", "onSuccess");
                for (int i = 0; i < newsList.size(); i++) {
                    News news = newsList.get(i);
                    Log.d("<>", news.source.id + "\t" + news.source.name
                            + "\t" + news.author
                            + "\t" + news.title
                            + "\t" + news.description
                            + "\t" + news.url
                            + "\t" + news.urlToImage
                            + "\t" + news.publishedAt
                            + "\t" + news.content
                            + "\n");
                }
            }

            @Override
            public void onFailed() {
                Log.d("<>", "onFailed");
            }
        };

        newsRemoteSource.getNews(NEWS_TYPE_HEADLINES, COUNTRY_US, null, callback);
    }
}
