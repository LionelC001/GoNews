package com.lionel.gonews.content;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;


public class ImageCompletedCallbackView extends AppCompatImageView {

    private IImageViewCompletedCallback callback;

    public interface IImageViewCompletedCallback {
        void onImageCompleted(Drawable drawable);
    }

    public ImageCompletedCallbackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCallback(IImageViewCompletedCallback callback){
        this.callback = callback;
    }

    public void onGlideImageComplete(Drawable drawable){
        if(callback!=null){
            callback.onImageCompleted(drawable);
        }
    }
}
