package com.example.ynspo.data.repository

import com.example.ynspo.data.api.UnsplashApiService
import com.example.ynspo.data.model.UnsplashResponse
import javax.inject.Inject

class UnsplashRepository @Inject constructor(
    private val apiService: UnsplashApiService
) {
    suspend fun searchPhotos(query: String, page: Int = 1, perPage: Int = 20): UnsplashResponse {
        return apiService.searchPhotos(query, page, perPage)
    }
}
