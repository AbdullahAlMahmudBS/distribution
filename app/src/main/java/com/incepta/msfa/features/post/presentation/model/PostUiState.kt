package com.incepta.msfa.features.post.presentation.model

import com.incepta.msfa.features.post.domain.model.Post
import com.incepta.msfa.features.post.domain.model.Slider


/**
 * Created by Abdullah on 18/5/25.
 */
/**
 * Represents the UI state for the post screen, including loading status, posts, sliders, and errors.
 */
data class PostUiState (
    /** Whether data is currently being loaded. */
    val isLoading: Boolean = false,
    /** List of posts to display. Empty if no posts are available. */
    val posts: List<Post> = emptyList(),
    /** List of sliders to display. Empty if no sliders are available. */
    val sliders: List<Slider> = emptyList(),
    /** Error state, if any, containing the message and retry option. */
    val error: ErrorState? = null
) {
    /**
     * Represents an error state with a message and retry option.
     */
    data class ErrorState(
        /** The error message to display. */
        val message: String,
        /** Whether the user can retry the failed operation. */
        val canRetry: Boolean = false
    )
}