package com.incepta.msfa.di

import com.incepta.msfa.features.post.domain.repository.PostsRepository
import com.incepta.msfa.features.post.domain.usecase.GetAllPostUseCase
import com.incepta.msfa.features.post.domain.usecase.GetAllSlidersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Abdullah on 14/5/25.
 */

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetAllMovieListUseCase(
        postsRepository: PostsRepository
    ): GetAllPostUseCase {
        return GetAllPostUseCase(postsRepository)
    }

    @Provides
    @Singleton
    fun provideGetAllSlidersUseCase(
        postsRepository: PostsRepository
    ): GetAllSlidersUseCase {
        return GetAllSlidersUseCase(postsRepository)
    }
}