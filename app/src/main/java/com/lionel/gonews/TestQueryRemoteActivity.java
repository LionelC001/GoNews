package com.lionel.gonews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lionel.gonews.data.News;
import com.lionel.gonews.data.INewsSource;
import com.lionel.gonews.data.QueryNews;
import com.lionel.gonews.data.remote.NewsRemoteSource;

import java.util.List;

import static com.lionel.gonews.util.Constants.PUBLISHEDAT;

public class TestQueryRemoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_test_query_remote);


        testGetNews();
    }

    private void testGetNews() {
        Log.d("<>", "test: starting get news");
        NewsRemoteSource newsRemoteSource = new NewsRemoteSource(this);
        INewsSource.LoadNewsCallback callback = new INewsSource.LoadNewsCallback() {
            @Override
            public void onSuccess(int totalResults, List<News> newsList) {
                Log.d("<>", "totalResults: " + totalResults + "\n");
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

//       newsRemoteSource.queryNews(new QueryNews.QueryHeadlinesNews(US, GENERAL), callback);

        newsRemoteSource.queryNews(new QueryNews.QueryEverythingNews("apple",
                        "1",
                        PUBLISHEDAT,
                        "2019-04-08",
                        "2019-04-09")
                , callback);
    }
}
