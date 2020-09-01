package com.example.assignment.app

import android.app.Application
import com.example.assignment.di.APIModule
import com.example.assignment.di.AppComponent
import com.example.assignment.di.DaggerAppComponent
import com.example.assignment.utils.AppURL

class MyApplication: Application() {

    companion object {
        var mAppComponent: AppComponent?=null
        val context=this
    }
    override fun onCreate() {
        super.onCreate()
        initDaggerComponent()
    }

    /**
     * initialized dagger
     */
    fun initDaggerComponent():AppComponent?{
        mAppComponent =   DaggerAppComponent
            .builder()
            .aPIModule(APIModule(AppURL.BASE_URL))
            .build()
        return mAppComponent
    }

}