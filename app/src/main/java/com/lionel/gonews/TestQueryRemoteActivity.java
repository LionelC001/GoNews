package com.lionel.gonews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lionel.gonews.data.INewsSource;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.remote.QueryFilter;
import com.lionel.gonews.data.remote.RemoteNewsSource;

import java.util.List;

import static com.lionel.gonews.util.Constants.GENERAL;
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
        RemoteNewsSource remoteNewsSource = new RemoteNewsSource(this);
        INewsSource.IQueryNewsCallback callback = new INewsSource.IQueryNewsCallback() {
            @Override
            public void onSuccess(int totalSize, List<News> newsList) {
                Log.d("<>", "totalResults: " + totalSize + "\n");
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
            public void onFailed(String msg) {
                Log.d("<>", "onFailed");
            }
        };

      remoteNewsSource.queryNews(new QueryFilter.QueryHeadlinesFilter(GENERAL), callback);

//        remoteNewsSource.queryNews(new QueryFilter.QueryEverythingFilter("apple",
//                        PUBLISHEDAT,
//                        "2019-04-08",
//                        "2019-04-09")
//                , callback);
    }
}
