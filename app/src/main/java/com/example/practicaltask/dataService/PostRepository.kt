package com.example.practicaltask.dataService

import android.util.Log
import com.example.practicaltask.viewModel.PostDetail
import com.example.practicaltask.viewModel.model.RequestBodyDto

class PostRepository {

    suspend fun fetchPosts(page: Int): List<PostDetail> {
        Log.e("Feed Data", "page: $page")
        val response = RetrofitClient.apiService.getPosts(
            page = page,
            body = RequestBodyDto(),
            authHeader = "your Auth key"
        )
        if (!response.isSuccessful) {
            Log.e(
                "API_ERROR",
                "Code: ${response.code()}, ErrorBody: ${response.errorBody()?.string()}"
            )

        }
        Log.e("Feed Data", "response: ${response.isSuccessful}")
        if (response.isSuccessful) {
            Log.e("Feed Data", "response: ${response.body()}")
            return response.body()?.data
                ?.map { it.post } ?: emptyList()
        } else throw Exception("API Error")
    }
}
