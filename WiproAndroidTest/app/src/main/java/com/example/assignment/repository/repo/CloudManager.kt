package com.example.kotlinle

import com.example.assignment.repository.api.CloudAPI
import com.example.assignment.repository.model.FeedData
import retrofit2.Retrofit
import java.net.SocketTimeoutException
import javax.inject.Inject

/*
 * CloudManager.kt
 * Class is responsible to sync data from cloud
 *
 */
class CloudManager @Inject constructor() {

    @Inject
    lateinit var mRetrofit: Retrofit
    private val mTAG: String="CloudManager"

    /**
     * method to retrive Feed data from server and return response to viewmodel
     */
    suspend fun fatchFeeds():FeedData? {
        var cloudResponse=mRetrofit.create(CloudAPI::class.java).fetchFeeds()
        if (cloudResponse.isSuccessful){
            return cloudResponse.body()
        }else{
            return null
        }
    }
}