package com.navega.entre.instagram.domain.home

import com.navega.entre.instagram.core.Result
import com.navega.entre.instagram.data.model.post
import kotlinx.coroutines.flow.Flow

interface homeScreenRepo {

    suspend fun getLatesPosts(): Result<List<post>>
suspend fun registrarLikeButtonState(postId: String, liked:Boolean)
}