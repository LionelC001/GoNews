package com.lionel.gonews.content;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lionel.gonews.data.News;

import static com.lionel.gonews.util.Constants.NEWS_CONTENT;

public class ContentAct extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getNewsContent();
    }

    private void getNewsContent() {
        News news = (News)getIntent().getSerializableExtra(NEWS_CONTENT);
        Log.d("<>", "getNewsContent: " + news.source.name);
    }
}
