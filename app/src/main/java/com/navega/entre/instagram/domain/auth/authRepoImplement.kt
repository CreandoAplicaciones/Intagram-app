package com.navega.entre.instagram.domain.auth

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseUser
import com.navega.entre.instagram.data.remote.auth.AuthDataSource


class authRepoImplement(private val dataSource: AuthDataSource) : AuthRepo {

    override suspend fun signIn(email: String, password: String): FirebaseUser? =
        dataSource.singIn(email, password)

    override suspend fun signUp(email: String, password: String, userName: String): FirebaseUser? =
       dataSource.singUp(email,password,userName)

    override suspend fun updateProfile(imageBitmap: Bitmap, userName: String) =dataSource.updateUserProfile(imageBitmap,userName)
}