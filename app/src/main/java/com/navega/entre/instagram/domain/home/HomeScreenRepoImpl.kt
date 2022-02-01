package com.navega.entre.instagram.domain.home

import com.navega.entre.instagram.core.Result
import com.navega.entre.instagram.data.model.post
import com.navega.entre.instagram.data.remote.home.HomeScreenDataSource

class HomeScreenRepoImpl(private val dataSource: HomeScreenDataSource) : homeScreenRepo {
    override suspend fun getLatesPosts(): Result<List<post>> =dataSource.getLatestPost()

    override suspend fun registrarLikeButtonState(postId: String, liked: Boolean) =dataSource.registerLikeButtonState(postId,liked)
}