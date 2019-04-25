package com.lionel.gonews.content;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import com.lionel.gonews.R;
import com.lionel.gonews.data.News;

import static com.lionel.gonews.util.Constants.NEWS_CONTENT;

public class ContentAct extends AppCompatActivity implements ImageCompletedCallbackView.IImageViewCompletedCallback {

    private ContentViewModel viewModel;
    private ViewDataBinding binding;
    private CheckBox chkFavorite;
    private News news;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        news = (News)getIntent().getSerializableExtra(NEWS_CONTENT);

        initViewModel();
        initImgCompletedListener();
        initBtnBack();
        initChkBoxFavorite();
        initBtnShare();
        initNewsContent();
        storeLocalNewsHistory();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ContentViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.act_content);
        viewModel.setBinding(binding);
    }

    private void initImgCompletedListener() {
        ImageCompletedCallbackView imgView = findViewById(R.id.imgPhoto);
        imgView.setCallback(this);
    }

    private void initBtnBack() {
        ImageButton btnBack = findViewById(R.id.imgBtnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initChkBoxFavorite() {
        chkFavorite = findViewById(R.id.chkBoxFavorite);
        chkFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              viewModel.updateFavorite(isChecked);
            }
        });
    }

    private void initBtnShare() {
        ImageButton btnShare = findViewById(R.id.imgBtnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, news.url);

                Intent chooser = Intent.createChooser(intent, getString(R.string.share));

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
            }
        });
    }

    private void initNewsContent() {
        viewModel.setNewsContent(news);
    }

    private void storeLocalNewsHistory() {
        viewModel.storeLocalNewsHistory(news);
    }

    @Override
    public void onImageCompleted(Drawable drawable) {
        // save this page content to local db, after Glide fetched the image from url
    }
}
