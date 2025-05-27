package com.example.practicaltask.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practicaltask.R
import com.example.practicaltask.viewModel.PostDetail
import com.example.practicaltask.widgets.CircleImageView


class PostAdapter(
    private val posts: MutableList<PostDetail?>,
    private val onLikeClick: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_POST = 0
    private val VIEW_TYPE_LOADING = 1

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCaption = view.findViewById<TextView>(R.id.tvCaption)
        val tvUserName = view.findViewById<TextView>(R.id.tvUserName)
        val ivImage = view.findViewById<ImageView>(R.id.ivImage)
        val tvLikeCount = view.findViewById<TextView>(R.id.tvLikeCount)
        val ivLike = view.findViewById<ImageView>(R.id.ivLike)
        val ivProfile = view.findViewById<CircleImageView>(R.id.ivProfile)
    }

    inner class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemViewType(position: Int): Int {
        return if (posts[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_POST
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_POST) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_feed, parent, false)
            PostViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PostViewHolder) {
            val post = posts[position]
            try {
                val user = post?.userId
                if (user != null) {
                    holder.tvUserName.text = user.name ?: " "
                    Glide.with(holder.ivProfile)
                        .load("https://d3b13iucq1ptzy.cloudfront.net/${user.profile}")
                        .error(R.drawable.ic_user).into(holder.ivProfile)
                }
                if (post != null) {
                    if (post.description != null) {
                        holder.tvCaption.text = post.description ?: " "
                    }
                }
                val imageUrl =
                    "https://d3b13iucq1ptzy.cloudfront.net/uploads/posts/image/${post!!.media[0].url}"
                Glide.with(holder.ivImage).load(imageUrl).into(holder.ivImage)
                holder.tvLikeCount.text = "${post.TotalLike} Likes"
                holder.ivLike.setImageResource(
                    if (post.selfLike) R.drawable.heart_fill else R.drawable.heart_unfill
                )
                holder.ivLike.setOnClickListener {
                    if (post._id != null) {
                        onLikeClick(post._id)
                    }
                }
            } catch (e: Exception) {
                Log.e("FEED_VALUE", "onBindViewHolder: exception ${e.message}")
            }
        }
    }

    override fun getItemCount(): Int = posts.size

    fun showLoading() {
        if (!posts.contains(null)) {
            posts.add(null)
            notifyItemInserted(posts.size - 1)
        }
    }

    fun hideLoading() {
        if (posts.contains(null)) {
            val index = posts.indexOf(null)
            posts.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun updatePosts(newPosts: List<PostDetail>) {
        val currentSize = posts.size
        posts.addAll(newPosts)
        notifyItemRangeInserted(currentSize, newPosts.size)
    }

    fun setPosts(newPosts: List<PostDetail>) {
        posts.clear()
        posts.addAll(newPosts)
        notifyDataSetChanged()
    }

}
