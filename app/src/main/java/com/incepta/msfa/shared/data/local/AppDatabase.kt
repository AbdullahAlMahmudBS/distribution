package com.incepta.msfa.shared.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.incepta.msfa.features.post.data.model.PostEntity

/**
 * Created by Abdullah on 18/5/25.
 */

@Database(entities = [PostEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}