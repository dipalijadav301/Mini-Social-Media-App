package com.example.practicaltask

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practicaltask.dataService.PostRepository
import com.example.practicaltask.databinding.ActivityFeedBinding
import com.example.practicaltask.viewModel.model.PostViewModel
import com.example.practicaltask.viewModel.ViewModelFactory
import com.example.practicaltask.adapter.PostAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.delay

class FeedActivity : AppCompatActivity() {

    private val bd by lazy { ActivityFeedBinding.inflate(layoutInflater) }
    private lateinit var viewModel: PostViewModel
    private var isLoading = false
    private var isLastPage = false
    private lateinit var adapter: PostAdapter
    var totalPosts = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(bd.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        adapter = PostAdapter(mutableListOf()) { postId ->
            viewModel.toggleLike(postId)
        }

        bd.recyclerView.layoutManager = LinearLayoutManager(this)
        bd.recyclerView.adapter = adapter

        val factory = ViewModelFactory(PostRepository())
        viewModel = ViewModelProvider(this, factory)[PostViewModel::class.java]

        observeViewModel()
        setupPagination()

        viewModel.loadPosts() // load first page
    }

    private fun observeViewModel() {
        viewModel.posts.observe(this) { posts ->
            Log.e("Feed Data", "posts: ${posts.size}")
            totalPosts = posts.size
            bd.tvPostsCount.text = "$totalPosts Posts"
            Log.e("Feed Data", "viewModel.currentPage: ${viewModel.currentPage}")
            if (viewModel.currentPage == 2) {
                bd.shimmerViewContainer.shimmerLayout.stopShimmer()
                adapter.hideLoading()
                bd.shimmerViewContainer.shimmerLayout.visibility = View.GONE
                adapter.setPosts(posts.toMutableList())
            } else {
                bd.shimmerViewContainer.shimmerLayout.stopShimmer()
                bd.shimmerViewContainer.shimmerLayout.visibility = View.GONE
                adapter.hideLoading()
                adapter.updatePosts(posts.takeLast(posts.size - adapter.itemCount + 1))
            }
        }

        viewModel.loading.observe(this) { loading ->
            isLoading = loading
            if (isLoading && adapter.itemCount == 0) {
                bd.shimmerViewContainer.shimmerLayout.visibility = View.VISIBLE
                bd.shimmerViewContainer.shimmerLayout.startShimmer()
            } else {
                bd.shimmerViewContainer.shimmerLayout.stopShimmer()
                bd.shimmerViewContainer.shimmerLayout.visibility = View.GONE
            }
        }
    }

    private fun setupPagination() {
        bd.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = rv.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 2 && firstVisibleItemPosition >= 0) {
                        adapter.showLoading()
                        viewModel.loadPosts()
                    }
                }
            }
        })
    }
}