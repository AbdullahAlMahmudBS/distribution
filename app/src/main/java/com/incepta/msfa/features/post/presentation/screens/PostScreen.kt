package com.incepta.msfa.features.post.presentation.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.incepta.core.base.BaseResponse
import com.incepta.core.base.UiEventHandler
import com.incepta.msfa.features.post.data.model.PostDto
import com.incepta.msfa.features.post.data.model.PostEntity
import com.incepta.msfa.features.post.data.model.SliderDto
import com.incepta.msfa.features.post.data.repository.PostRepositoryImpl
import com.incepta.msfa.shared.data.local.PostDao
import com.incepta.msfa.features.post.domain.usecase.GetAllPostUseCase
import com.incepta.msfa.features.post.domain.usecase.GetAllSlidersUseCase
import com.incepta.msfa.features.post.presentation.components.PostList
import com.incepta.msfa.features.post.presentation.components.ShimmerLoadingEffect
import com.incepta.msfa.features.post.presentation.components.ShowcaseSliders
import com.incepta.msfa.features.post.presentation.model.PostEvent
import com.incepta.msfa.shared.data.remote.AppApiService
import com.inceptaiddi.core.presentation.post.PostViewModel
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(viewModel: PostViewModel = hiltViewModel()) {
    val state by viewModel.postsState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val uiEvent by viewModel.uiEvent.collectAsState()

    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(
                message = if (error.canRetry) "Failed. ${error.message}. Tap to retry." else error.message
            )
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Posts") }) },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Button(
                        onClick = { viewModel.insertSamplePosts() },
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        Text("Insert Sample Posts", fontSize = 16.sp)
                    }

                    if (state.isLoading) {
                        ShimmerLoadingEffect()
                    } else {
                        ShowcaseSliders(
                            sliders = state.sliders,
                            onSliderClick = { viewModel.onEvent(PostEvent.OnSliderClick(it)) }
                        )

                        PostList(
                            posts = state.posts,
                            onPostClick = { viewModel.onEvent(PostEvent.OnPostClick(it)) }
                        )

                        if (state.posts.isEmpty() && state.sliders.isEmpty() && state.error == null) {
                            EmptyState("No content available")
                        }
                    }
                }

                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.align(Alignment.BottomCenter),
                    snackbar = {
                        Snackbar(
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.padding(16.dp),
                            action = {
                                if (state.error?.canRetry == true) {
                                    TextButton(onClick = { viewModel.refreshPosts() }) {
                                        Text("Retry")
                                    }
                                }
                            },
                            content = { Text(it.visuals.message) }
                        )
                    }
                )
                UiEventHandler(
                    uiEvent = uiEvent,
                    onEventConsumed = { viewModel.clearUiEvent() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    )

}


@Composable
fun EmptyState(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ErrorDialog(
    message: String,
    canRetry: Boolean,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Error",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            if (canRetry) {
                TextButton(onClick = onRetry) {
                    Text("Retry")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        },
        modifier = Modifier.animateContentSize()
    )
}

@Preview(showBackground = true)
@Composable
fun PostScreenPreview() {
    MaterialTheme {
        PostScreen(
            viewModel = PostViewModel(
                GetAllPostUseCase(
                    PostRepositoryImpl(
                        object : AppApiService {
                            override suspend fun getPosts(): Response<List<PostDto>> {
                                return Response.success(
                                    listOf(
                                        PostDto(
                                            id = 1,
                                            title = "Sample Post",
                                            description = "This is a sample post description."
                                        ),
                                        PostDto(
                                            id = 2,
                                            title = "Sample Post 2",
                                            description = "This is a sample post description."
                                        ),
                                        PostDto(
                                            id = 3,
                                            title = "Sample Post 3",
                                            description = "This is a sample post description."
                                        )
                                    ),
                                )

                            }

                            override suspend fun getSliders(): Response<List<SliderDto>> {
                                return Response.success(
                                    listOf(
                                        SliderDto(
                                            id = 1,
                                            title = "Sample Slider",
                                            url = "https://example.com/slider1.jpg"
                                        ),
                                        SliderDto(
                                            id = 2,
                                            title = "Sample Slider 2",
                                            url = "https://example.com/slider2.jpg"
                                        )
                                    )
                                )
                            }
                        },
                        object : PostDao {
                            override suspend fun getPosts(): List<PostEntity> {
                                return listOf(
                                    PostEntity(
                                        id = 1,
                                        title = "Sample Post",
                                        description = "This is a sample post description."
                                    )
                                )
                            }

                            override suspend fun insertPosts(posts: List<PostEntity>) {}
                            override suspend fun clearPosts() {}
                        },

                        )
                ),

                GetAllSlidersUseCase(
                    PostRepositoryImpl(
                        object : AppApiService {
                            override suspend fun getPosts(): Response<List<PostDto>> {
                                return Response.success(
                                    listOf(
                                        PostDto(
                                            id = 1,
                                            title = "Sample Post",
                                            description = "This is a sample post description."
                                        ),
                                        PostDto(
                                            id = 2,
                                            title = "Sample Post 2",
                                            description = "This is a sample post description."
                                        ),
                                        PostDto(
                                            id = 3,
                                            title = "Sample Post 3",
                                            description = "This is a sample post description."
                                        )
                                    ),
                                )

                            }

                            override suspend fun getSliders(): Response<List<SliderDto>> {
                                return Response.success(
                                    listOf(
                                        SliderDto(
                                            id = 1,
                                            title = "Sample Slider",
                                            url = "https://example.com/slider1.jpg"
                                        ),
                                        SliderDto(
                                            id = 2,
                                            title = "Sample Slider 2",
                                            url = "https://example.com/slider2.jpg"
                                        )
                                    )
                                )
                            }
                        },
                        postDao = TODO()
                    )
                ),
                postDao = TODO(),
            )
        )
    }
}