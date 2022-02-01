package com.navega.entre.instagram.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.navega.entre.instagram.core.Result
import com.navega.entre.instagram.domain.home.homeScreenRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class homeSceenViewModel(private val repo: homeScreenRepo) : ViewModel() {

    fun fetLatesPosts() = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        kotlin.runCatching {
            repo.getLatesPosts()
        }.onSuccess { postList ->
                emit(postList)

        }.onFailure { throwable ->
            emit(Result.Failure(Exception(throwable.message)))
        }

    }
    fun registrarLikeButtonState(postId: String, liked:Boolean)= liveData(viewModelScope.coroutineContext + Dispatchers.Main) {
        emit(Result.Loading())
        kotlin.runCatching {
            repo.registrarLikeButtonState(postId, liked)
        }.onSuccess {
emit(Result.Success(Unit))
        }.onFailure {throwable ->
            emit(Result.Failure(Exception(throwable.message)))

        }
    }
}

class HomeScreenViewModeFactory(private val repo: homeScreenRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(homeScreenRepo::class.java).newInstance(repo)
    }
}