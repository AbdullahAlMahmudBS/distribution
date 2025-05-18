package com.inceptaiddi.core.presentation.post

import androidx.lifecycle.viewModelScope
import com.incepta.core.base.UiState
import com.incepta.msfa.features.post.data.model.PostEntity
import com.incepta.msfa.shared.data.local.PostDao
import com.incepta.msfa.features.post.domain.model.Post
import com.incepta.msfa.features.post.domain.usecase.GetAllPostUseCase
import com.inceptaiddi.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getPostsUseCase: GetAllPostUseCase,
    private val postDao: PostDao
) : BaseViewModel() {

    private val _postsState = MutableStateFlow<UiState<List<Post>>>(UiState.Idle)
    val postsState: StateFlow<UiState<List<Post>>> = _postsState.asStateFlow()


    init {
        fetchPosts()
    }

    fun fetchPosts() {
        callService(
            serviceCall = { getPostsUseCase.invoke() },
            onStart = { isInitial ->
                _postsState.value = UiState.Loading(isInitial)
            },
            onSuccess = { posts ->
                _postsState.value = if (posts.isEmpty()) {
                    UiState.Empty("No posts available")
                } else {
                    UiState.Success(posts)
                }
            },
            onError = { message, canRetry -> _postsState.value = UiState.Error(message, canRetry) },
            onCompleted = { /* Optional logging */ },
        )
    }

    fun refreshPosts() {
        fetchPosts()
    }

    fun retryFetchPosts() {
        fetchPosts()
    }

    fun insertSamplePosts() {
        viewModelScope.launch {
            val samplePosts = listOf(
                PostEntity(
                    id = 1,
                    title = "Local Post 1",
                    description = "This post is coming from Local DB 1."
                ),
                PostEntity(
                    id = 2,
                    title = "Sample Post 2",
                    description = "This post is coming from Local DB 2."
                ),
                PostEntity(
                    id = 3,
                    title = "Sample Post 3",
                    description = "This post is coming from Local DB 3."
                )
            )
            postDao.insertPosts(samplePosts)
            fetchPosts()
        }
    }
}