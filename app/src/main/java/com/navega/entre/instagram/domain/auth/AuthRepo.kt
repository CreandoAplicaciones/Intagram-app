package com.navega.entre.instagram.domain.auth

import android.graphics.Bitmap
import android.provider.ContactsContract
import android.widget.ImageView
import com.google.firebase.auth.FirebaseUser

interface AuthRepo {
    suspend fun signIn(email: String,password:String):FirebaseUser?
    suspend fun signUp(email: String, password: String, userName: String): FirebaseUser?
    suspend fun updateProfile(imageBitmap: Bitmap,userName: String)


}