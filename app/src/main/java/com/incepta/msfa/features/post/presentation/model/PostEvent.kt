package com.incepta.msfa.features.post.presentation.model

import com.incepta.msfa.features.post.domain.model.Post
import com.incepta.msfa.features.post.domain.model.Slider


/**
 * Created by Abdullah on 18/5/25.
 */

sealed class PostEvent {
    object OnRefresh : PostEvent()
    object OnRetry : PostEvent()
    data class OnPostClick(val post: Post) : PostEvent()
    data class OnSliderClick(val slider: Slider) : PostEvent()

}