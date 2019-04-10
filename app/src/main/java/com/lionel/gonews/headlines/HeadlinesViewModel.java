package com.lionel.gonews.headlines;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ViewDataBinding;

public class HeadlinesViewModel {
    public final ObservableBoolean isQueryFocus = new ObservableBoolean();

    public HeadlinesViewModel(Context context, ViewDataBinding binding) {

    }
}
