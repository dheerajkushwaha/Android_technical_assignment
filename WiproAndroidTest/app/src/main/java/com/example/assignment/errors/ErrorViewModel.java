/*
 * ErrorViewModel.java
 * Callisto 2.0
 *
 * Copyright © 2019 Eli Lilly and Company. All rights reserved.
 *
 */
package com.example.assignment.errors;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ErrorViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> mCloseButtonClick;

    public ErrorViewModel(@NonNull Application application) {
        super(application);
        mCloseButtonClick = new MutableLiveData<>();
    }

    public void onErrorClose(View view) {
        if (view != null) {
            mCloseButtonClick.setValue(true);
        }
    }

    public MutableLiveData<Boolean> getCloseButtonClick() {
        return mCloseButtonClick;
    }
}
