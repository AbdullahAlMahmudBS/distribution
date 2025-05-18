package com.incepta.msfa.features.post.data.model


/**
 * Created by Abdullah on 18/5/25.
 */
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.incepta.msfa.features.post.domain.model.Post

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String
)

fun PostEntity.toDomain() = Post(id, title, description)
fun Post.toEntity() = PostEntity(id, title, description)