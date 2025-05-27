package com.example.practicaltask.viewModel.model

data class RequestBodyDto(
    val postIdList: List<String> = emptyList(),
    val shots: Boolean = false
)
