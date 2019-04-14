package com.lionel.gonews.headlines;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class HeadlinesFragViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final String category;

    public HeadlinesFragViewModelFactory(Application application, String category) {
        this.application = application;
        this.category = category;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HeadlinesFragmentViewModel.class)) {
            return (T) new HeadlinesFragmentViewModel(application, category);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
