package com.lionel.gonews.util;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ImageOpenHelper {

    @BindingAdapter("imageUrl")
    public static void setImageFromUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).into(imageView);
    }
}
