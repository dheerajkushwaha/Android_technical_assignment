package com.example.kotlinle

import com.example.assignment.repository.api.CloudAPI
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
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
    fun fatchFeeds() = flow {
        emit(getFeedsFromAPI())
    }

    /**
     * method to retrive Feed data from server and return response to viewmodel
     */
    private suspend fun getFeedsFromAPI() =
        mRetrofit.create(CloudAPI::class.java).fetchFeeds()
            .run {
                if(isSuccessful && body()!=null){
                    body()
                }else{
                    null
                }
            }
}