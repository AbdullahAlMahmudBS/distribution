package com.inceptaiddi.core.presentation.post

import androidx.lifecycle.viewModelScope
import com.incepta.core.utils.Logger
import com.incepta.msfa.features.post.data.model.PostEntity
import com.incepta.msfa.shared.data.local.PostDao
import com.incepta.msfa.features.post.domain.model.Post
import com.incepta.msfa.features.post.domain.usecase.GetAllPostUseCase
import com.incepta.msfa.features.post.domain.usecase.GetAllSlidersUseCase
import com.incepta.msfa.features.post.presentation.model.PostEvent
import com.incepta.msfa.features.post.presentation.model.PostUiState
import com.inceptaiddi.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getPostsUseCase: GetAllPostUseCase,
    private val sliderUseCase: GetAllSlidersUseCase,
    private val postDao: PostDao
) : BaseViewModel() {

    private val _postsState = MutableStateFlow(PostUiState())
    val postsState: StateFlow<PostUiState> = _postsState.asStateFlow()


    init {
         fetchPosts()
        fetchSliders()
    }


    fun onEvent(event: PostEvent) {
        when (event) {
            is PostEvent.OnRefresh -> {
                refreshPosts()
            }
            is PostEvent.OnRetry -> {
                retryFetchPosts()
            }
            is PostEvent.OnPostClick -> {
                //show snackbar on post click

                Logger.info(":::::::::::::::::::::::::::")
                Logger.info("Title: ${event.post.title}")
                Logger.info("Title: ${event.post.description}")

                showSnackbar("Post Clicked")


            }
            is PostEvent.OnSliderClick -> {
                // Handle slider click
            }
        }
    }

    fun fetchPosts() {
        callService(
            serviceCall = { getPostsUseCase.invoke() },
            onStart = { _postsState.update { it.copy(isLoading = true) } },
            onSuccess = { results ->
                _postsState.update {
                    it.copy(
                        isLoading = false,
                        posts = results,
                        error = null
                    )
                }
            },
            onError = { message, canRetry ->
                _postsState.update {
                    it.copy(
                        isLoading = false,
                        error = PostUiState.ErrorState(message, canRetry)
                    )
                }
            },
            onCompleted = {}
        )
    }

    fun fetchSliders() {
        callService(
            serviceCall = { sliderUseCase.invoke() },
            onSuccess = { sliders ->
                _postsState.update {
                    it.copy(
                        sliders = sliders,
                        error = null
                    )
                }
            },
            onError = { message, canRetry ->
                _postsState.update {
                    it.copy(
                        error = PostUiState.ErrorState(message, canRetry)
                    )
                }
            },
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