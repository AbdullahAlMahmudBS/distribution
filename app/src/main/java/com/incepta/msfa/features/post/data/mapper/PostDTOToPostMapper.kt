package com.incepta.msfa.features.post.data.mapper

import com.incepta.msfa.features.post.data.model.PostDto
import com.incepta.msfa.features.post.domain.model.Post


/**
 * Created by Abdullah on 14/5/25.
 */

fun PostDto.toDomain() = Post(id, title, description)