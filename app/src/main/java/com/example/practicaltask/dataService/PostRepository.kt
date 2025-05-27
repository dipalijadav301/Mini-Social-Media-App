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
            authHeader = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyRGF0YSI6eyJfaWQiOiI1NzgiLCJuYW1lIjoiVHVzaGFyIE0iLCJjb3VudHJ5Q29kZSI6Iis5MSIsImVtYWlsIjoiIiwicGhvbmUiOjg5ODA5NjkxNzUsImRlc2NyaXB0aW9uIjoiSGV5LCBJJ20gaW4gVGVwbm90IiwidGFncyI6IiIsInByb2ZpbGUiOiJ1cGxvYWRzL3Byb2ZpbGUvMzY2Mzk2M0YtMkVDMC00MDhCLUFBODctMDU4QTRFRUMxRjg4LTIwODEtMDAwMDAyNkU3RkJCOTVBRC5qcGVnIiwib3RwU2VuZENvdW50IjoxLCJsYXN0T3RwU2VuZERhdGUiOiIyMDI0LTEyLTMxVDEyOjQzOjM1LjAwMloiLCJpc1VzZXJWZXJpZmllZCI6dHJ1ZSwiaXNCYW5uZWQiOmZhbHNlLCJkYXRlT2ZCaXJ0aCI6IjE5OTYtMDEtMDFUMDA6MDA6MDAuMDAwWiIsImxpbmsiOiIiLCJpc0RlbGV0ZWQiOmZhbHNlLCJvbGRQaG9uZSI6bnVsbCwidXNlclR5cGUiOiJVc2VyIiwiY3JlYXRlQXQiOiIyMDI0LTA5LTMwVDEwOjAwOjA0LjQ2NVoiLCJ1cGRhdGVBdCI6IjIwMjUtMDUtMTlUMDc6MjQ6MDMuNTQ0WiIsImJpb1VwZGF0ZSI6IjIwMjUtMDQtMTJUMDk6Mzk6MTkuNzE2WiIsIl9fdiI6MCwiaXNPdHBWZXJpZmllZCI6dHJ1ZX0sImlhdCI6MTc0ODMyNDc4M30.sP_YP4_C23WB2zsRYy-4qs96dDmyKql45ebhIV1K1lE"
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
