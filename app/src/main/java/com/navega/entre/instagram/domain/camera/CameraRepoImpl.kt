package com.navega.entre.instagram.domain.camera

import android.graphics.Bitmap
import com.navega.entre.instagram.data.remote.camera.CameraDataSource

class CameraRepoImpl (private val dataSource: CameraDataSource):CameraRepo{

    override suspend fun uploadPhoto(imageBitmap: Bitmap, description: String) {
dataSource.uploadPhoto(imageBitmap, description)
    }
}