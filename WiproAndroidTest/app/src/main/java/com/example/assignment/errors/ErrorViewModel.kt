package com.example.assignment.errors

import android.app.Application
import android.view.View
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ErrorViewModel(@NonNull application: Application?) :
    AndroidViewModel(application!!) {

    private val closeButtonClick=MutableLiveData<Boolean>()
    private var mCloseButtonClick: MutableLiveData<Boolean>? = null
    init {
        mCloseButtonClick = MutableLiveData()
    }
    fun onErrorClose(view: View?) {
        if (view != null) {
            mCloseButtonClick!!.value = true
        }
    }


    fun getCloseButtonClick(): MutableLiveData<Boolean>? {
        return mCloseButtonClick
    }
}

