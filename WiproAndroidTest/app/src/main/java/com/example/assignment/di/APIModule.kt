package com.example.assignment.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class APIModule constructor(baseURL:String) {
    private var baseURL:String?=""
    init {
        this.baseURL = baseURL
    }
@Singleton
@Provides
 fun provideOKHttpClient():OkHttpClient{
     return  OkHttpClient.Builder()
         .readTimeout(15,TimeUnit.SECONDS)
         .connectTimeout(15,TimeUnit.SECONDS)
         .build()

 }
@Singleton
@Provides
fun provideGSON(): GsonConverterFactory {

   return  GsonConverterFactory.create()

}
@Singleton
@Provides
fun provideRetrofit(gsonConverterFactory: GsonConverterFactory,okHttpClient: OkHttpClient):Retrofit{
    return Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build()
}

}