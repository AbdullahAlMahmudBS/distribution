package com.incepta.msfa.shared.data.remote

import com.incepta.core.base.BaseResponse
import com.incepta.msfa.features.post.data.model.PostDto
import com.incepta.msfa.features.post.data.model.SliderDto
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by Abdullah on 14/5/25.
 */

interface AppApiService {
    @GET("/getPosts")
    suspend fun getPosts(
    ): Response<List<PostDto>>

    @GET("/sliders")
    suspend fun getSliders(
    ): Response<List<SliderDto>>
}