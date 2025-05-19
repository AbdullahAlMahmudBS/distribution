package com.incepta.msfa.features.post.domain.usecase

import com.incepta.core.base.Result
import com.incepta.msfa.features.post.domain.model.Slider
import com.incepta.msfa.features.post.domain.repository.PostsRepository
import javax.inject.Inject


/**
 * Created by Abdullah on 18/5/25.
 */

class GetAllSlidersUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) {
    suspend operator fun invoke() : Result<List<Slider>> = postsRepository.getSliders()
}