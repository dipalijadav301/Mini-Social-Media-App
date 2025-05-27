package com.example.practicaltask.viewModel.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaltask.dataService.PostRepository
import com.example.practicaltask.viewModel.PostDetail
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PostViewModel(private val repository: PostRepository) : ViewModel() {

    private val _posts = MutableLiveData<List<PostDetail>>()
    val posts: LiveData<List<PostDetail>> get() = _posts

    var currentPage = 1
        private set

    private var isLoadingMore = false

    val loading = MutableLiveData<Boolean>()

    fun loadPosts() {
        if (isLoadingMore) return
        isLoadingMore = true
        loading.value = true

        viewModelScope.launch {
            delay(2000) // just to visible shimmer effect
            try {
                val newPosts = repository.fetchPosts(currentPage)
                Log.e("Feed Data", "loadPosts:post list size ${newPosts.size}")

                val currentList = _posts.value ?: emptyList()
                _posts.value = currentList + newPosts
                currentPage++ // ⬅️ INCREMENT HERE
                Log.e("Feed Data", "currentPage: $currentPage")
            } catch (e: Exception) {
                Log.e("PostViewModel", "Error: ${e.message}")
            } finally {
                isLoadingMore = false
                loading.value = false
            }
        }
    }

    fun toggleLike(postId: String) {
        _posts.value = _posts.value?.map {
            run {
                if (it != null) {
                    Log.e("Feed Data", "toggleLike: ${(it._id == postId)}")
                    if (it._id == postId) {
                        it.copy(
                            selfLike = !it.selfLike,
                            TotalLike = if (it.selfLike) it.TotalLike - 1 else it.TotalLike + 1
                        )
                    } else it
                } else it
            }
        }
    }
}
