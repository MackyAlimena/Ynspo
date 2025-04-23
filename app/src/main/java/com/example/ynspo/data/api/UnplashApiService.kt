package com.example.ynspo.data.api

import com.example.ynspo.data.model.UnsplashResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApiService {

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): UnsplashResponse
}
