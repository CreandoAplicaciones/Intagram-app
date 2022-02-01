package com.navega.entre.instagram.data.model


import com.bumptech.glide.annotation.Excludes
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class post(
    @Exclude @JvmField
    var id: String = "",
    @ServerTimestamp
    var created_at: Date? = null,
    val post_image: String = "",
    val post_description: String = "",
    val poster: Poster? = null,
    val likes: Long = 0,
    @Exclude @JvmField
    var liked: Boolean = false
)

data class Poster(
    val username: String? = "",
    val uid: String? = null,
    val profile_picture: String = ""
)