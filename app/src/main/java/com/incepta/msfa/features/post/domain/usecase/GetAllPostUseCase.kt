package com.incepta.msfa.features.post.domain.usecase

import com.incepta.core.base.Result
import com.incepta.msfa.features.post.domain.model.Post
import com.incepta.msfa.features.post.domain.repository.PostsRepository
import javax.inject.Inject



/**
 * Created by Abdullah on 14/5/25.
 */

class GetAllPostUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) {
    suspend operator fun invoke() : Result<List<Post>> = postsRepository.getPosts()
}