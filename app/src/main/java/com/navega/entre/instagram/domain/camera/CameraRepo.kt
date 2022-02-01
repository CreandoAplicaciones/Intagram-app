package com.navega.entre.instagram.domain.camera

import android.graphics.Bitmap

interface CameraRepo {

    suspend fun uploadPhoto(imaBitmap: Bitmap,description:String)
}