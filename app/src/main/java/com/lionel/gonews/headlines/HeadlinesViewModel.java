package com.lionel.gonews.headlines;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ViewDataBinding;

import com.lionel.gonews.BR;

public class HeadlinesViewModel {
    public final ObservableBoolean is = new ObservableBoolean();

    public HeadlinesViewModel(Context context, ViewDataBinding binding) {
        binding.setVariable(BR.viewModel, this);
    }




}
