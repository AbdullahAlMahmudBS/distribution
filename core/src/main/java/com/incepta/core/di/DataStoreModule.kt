package com.incepta.core.di

import android.content.Context
import com.incepta.core.utils.DataStoreManager
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {

    // Provide DataStoreManager as a singleton
     @Provides
     fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
         return DataStoreManager(context)
     }
}