package com.incepta.msfa.di

import com.incepta.msfa.features.post.data.repository.PostRepositoryImpl
import com.incepta.msfa.features.post.domain.repository.PostsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * Created by Abdullah on 14/5/25.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideMovieRepository(
        postRepositoryImpl: PostRepositoryImpl
    ): PostsRepository
}