package com.lionel.gonews.content;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
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

public class ContentAct extends AppCompatActivity {

    private ContentViewModel viewModel;
    private CheckBox chkFavorite;
    private News news;
    private ContentFavoritePopupWindow popupWindowFavorite;
    private boolean isPopupWindowAllowed = false; // to tell that activity is already prepared, so we can show the popup window anytime

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        news = (News) getIntent().getSerializableExtra(NEWS_CONTENT);

        initViewModel();
        initBtnBack();
        initBtnShare();
        initChkBoxFavorite();
        storeLocalNewsHistory();
    }

    @Override
    protected void onResume() {
        isPopupWindowAllowed = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        // avoid memory leak
        if (popupWindowFavorite != null && popupWindowFavorite.isShowing()) {
            popupWindowFavorite.dismiss();
            popupWindowFavorite = null;
        }
        super.onPause();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ContentViewModel.class);
        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.act_content);
        viewModel.setBindingNews(binding, news);
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

    private void initChkBoxFavorite() {
        chkFavorite = findViewById(R.id.chkBoxFavorite);

        final LiveData<Integer> favoriteNewsCount = viewModel.checkIsFavoriteNews(news);
        favoriteNewsCount.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer count) {
                if (count != null && count > 0) {
                    viewModel.setFavoriteClickedFromObserve();
                    favoriteNewsCount.removeObserver(this);
                    chkFavorite.setChecked(true);
                }
            }
        });

        chkFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    viewModel.updateFavorite();
                    showPopupWindowFavorite();
                } else {
                    viewModel.deleteFavorite();
                }
            }
        });
    }

    private void showPopupWindowFavorite() {
        if (popupWindowFavorite == null) {
            popupWindowFavorite = new ContentFavoritePopupWindow(this);
        }
        if (isPopupWindowAllowed) {
            popupWindowFavorite.show(chkFavorite);
        }
    }

    private void storeLocalNewsHistory() {
        viewModel.storeNewsHistory();
    }
}
