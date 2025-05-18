package com.incepta.msfa.features.post.data.repository

import android.util.Log
import com.incepta.core.base.BaseRepository
import com.incepta.msfa.features.post.domain.model.Post
import com.incepta.msfa.features.post.domain.repository.PostsRepository
import com.incepta.msfa.shared.data.remote.AppApiService
import javax.inject.Inject
import com.incepta.core.base.Result
import com.incepta.core.base.mapSuccess
import com.incepta.msfa.features.post.data.mapper.toDomain
import com.incepta.msfa.features.post.data.model.PostDto
import com.incepta.msfa.features.post.data.model.toDomain
import com.incepta.msfa.features.post.data.model.toEntity
import com.incepta.msfa.shared.data.local.PostDao
import okio.IOException
import retrofit2.HttpException


/**
 * Created by Abdullah on 14/5/25.
 */

class PostRepositoryImpl @Inject constructor(
    private val apiService: AppApiService,
    private val postDao: PostDao
) : BaseRepository(), PostsRepository {

    override suspend fun getPosts(): Result<List<Post>> {
        val apiResult = safeApiCall { apiService.getPosts() }
            .mapSuccess { it-> it.data?.map { postDto ->
                postDto.toDomain()
            } ?: emptyList()
            }
        // Cache successful API response
        if (apiResult is Result.Success) {
            postDao.insertPosts(apiResult.data.map { it.toEntity() })
            return apiResult
        }

        // Return cached data if API fails and cache is non-empty
        val cachedPosts = postDao.getPosts().map { it.toDomain() }
        return if (cachedPosts.isNotEmpty()) {
            Result.Success(cachedPosts)
        } else {
            apiResult // Return API error if no cache
        }
    }

}
