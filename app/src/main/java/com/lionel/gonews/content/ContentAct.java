package com.lionel.gonews.content;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lionel.gonews.R;
import com.lionel.gonews.data.News;

import static com.lionel.gonews.util.Constants.NEWS_CONTENT;

public class ContentAct extends AppCompatActivity {

    private News news;
    private ViewDataBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.act_content);

        initNewsContent();

    }

    private void initNewsContent() {
        news = getIntent().getParcelableExtra(NEWS_CONTENT);
        binding.setVariable()
    }

    private void getNewsContent() {

    }
}
