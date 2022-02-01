package com.navega.entre.instagram.presentation.camera

import android.graphics.Bitmap
import android.graphics.DiscretePathEffect
import androidx.browser.customtabs.CustomTabsService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.navega.entre.instagram.core.Result
import com.navega.entre.instagram.domain.auth.AuthRepo
import com.navega.entre.instagram.domain.camera.CameraRepo
import com.navega.entre.instagram.presentation.auth.AuthViewModel
import kotlinx.coroutines.Dispatchers

class cameraViewModel (private val repo: CameraRepo):ViewModel() {

    fun uploadPhoto(imaBitmap: Bitmap, description: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        try {
            emit(Result.Success(repo.uploadPhoto(imaBitmap,description)))

        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    class CameraViewModelFactory(private val repo: CameraRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(CameraRepo::class.java).newInstance(repo)

        }
    }
}