package com.navega.entre.instagram.data.remote.auth

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.navega.entre.instagram.data.model.user
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import com.google.firebase.auth.ktx.userProfileChangeRequest
import java.lang.reflect.Array.get


class AuthDataSource {

    suspend fun singIn(email: String, password: String): FirebaseUser? {
        val authResult =
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
        return authResult.user
    }

    suspend fun singUp(email: String, password: String, userName: String): FirebaseUser? {
        val authResult =
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
        authResult.user?.uid?.let { uid ->
            FirebaseFirestore.getInstance().collection("users").document(authResult.user!!.uid)
                .set(user(email, userName, "photo url")).await()

        }
        return authResult.user


    }

    suspend fun updateUserProfile(imageBitmap: Bitmap, userName: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val imageRef = FirebaseStorage.getInstance().reference.child("${user?.uid}/prifile_picture")

        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val downloadUrl =
            imageRef.putBytes(baos.toByteArray()).await().storage.downloadUrl.await().toString()

        val profileUpdates = userProfileChangeRequest {
            displayName = userName
            photoUri = Uri.parse(downloadUrl)
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }


    }










}