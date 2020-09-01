package com.example.assignment.repository.api

import com.example.assignment.repository.model.FeedData
import retrofit2.Response
import retrofit2.http.*

interface CloudAPI {

    @GET(".")
    suspend fun fetchFeeds() : Response<FeedData>

}