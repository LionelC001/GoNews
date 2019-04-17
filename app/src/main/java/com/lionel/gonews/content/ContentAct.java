package com.lionel.gonews.content;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.lionel.gonews.BR;
import com.lionel.gonews.R;
import com.lionel.gonews.data.News;

import static com.lionel.gonews.util.Constants.NEWS_CONTENT;

public class ContentAct extends AppCompatActivity {

    private ViewDataBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.act_content);

        initToolbar();
        initNewsContent();
    }

    private void initToolbar() {

    }

    private void initNewsContent() {
        News news = getIntent().getParcelableExtra(NEWS_CONTENT);
        binding.setVariable(BR.contentActNews, news);
    }


    @Override
    protected void onDestroy() {
        Log.d("<>", "onDestory");
        super.onDestroy();
    }
}
