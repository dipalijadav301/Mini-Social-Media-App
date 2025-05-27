package com.example.practicaltask.viewModel

data class PostResponse(
    val status: String,
    val data: List<PostData>
)

data class PostData(
    val post: PostDetail
)

data class PostDetail(
    val _id: String?,
    val description: String?,
    val media: List<Media>,
    val TotalLike: Int,
    var selfLike: Boolean,
    val userId: User
)

data class Media(
    val url: String,
    val type: String,
    val thumbnail: String?
)

data class User(
    val name: String?,
    val profile: String
)
