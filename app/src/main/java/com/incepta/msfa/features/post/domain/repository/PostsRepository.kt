package com.incepta.msfa.features.post.domain.repository

import com.incepta.msfa.features.post.domain.model.Post
import com.incepta.core.base.Result
import com.incepta.msfa.features.post.domain.model.Slider

/**
 * Created by Abdullah on 14/5/25.
 */

interface PostsRepository {
    suspend fun getPosts(): Result<List<Post>>
    suspend fun getSliders(): Result<List<Slider>>
}