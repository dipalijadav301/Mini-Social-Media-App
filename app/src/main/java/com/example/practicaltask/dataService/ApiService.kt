package com.example.practicaltask.dataService

import com.example.practicaltask.viewModel.PostResponse
import com.example.practicaltask.viewModel.model.RequestBodyDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("api/v2/post/getPost")
    suspend fun getPosts(
        @Query("page") page: Int,
        @Body body: RequestBodyDto,
        @Header("Authorization") authHeader: String
    ): Response<PostResponse>
}
