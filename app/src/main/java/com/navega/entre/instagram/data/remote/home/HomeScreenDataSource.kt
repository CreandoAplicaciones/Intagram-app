package com.navega.entre.instagram.data.remote.home

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.navega.entre.instagram.core.Result
import com.navega.entre.instagram.data.model.post
import io.grpc.InternalChannelz.id
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception


class HomeScreenDataSource {
    suspend fun getLatestPost(): Result<List<post>> {
        val postList = mutableListOf<post>()

        withContext(Dispatchers.IO) {
            val querySnapshot = FirebaseFirestore.getInstance().collection("posts").orderBy(
                "created_at",
                Query.Direction.DESCENDING
            ).get().await()

            for (postSnap in querySnapshot.documents) {
                postSnap.toObject(post::class.java)?.let { fbPost ->

                    val isLiked=FirebaseAuth.getInstance().currentUser?.let { safeUser->
                        isPostLiked(postSnap.id, safeUser.uid)

                    }

                    fbPost.apply {
                        created_at = postSnap.getTimestamp(
                            "created_at",
                            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                        )?.toDate()

                        id=postSnap.id
                        if(isLiked!=null){
                            liked= isLiked
                        }
                    }

                    postList.add(fbPost)
                }
            }
        }
        return Result.Success(postList)

    }

    private suspend fun isPostLiked(postId: String,uid:String): Boolean {
        val post = FirebaseFirestore.getInstance().collection("postsLikes").document(postId).get().await()

        if(!post.exists()) return false
        val likeArray: List<String> = post.get("likes") as List<String>
        return likeArray.contains(uid)
    }

    fun registerLikeButtonState(postId: String, liked: Boolean) {

        val increment=FieldValue.increment(1)
        val decrement=FieldValue.increment(-1)

        val uid=FirebaseAuth.getInstance().currentUser?.uid
        val postRef=FirebaseFirestore.getInstance().collection("posts").document(postId)
        val postsLikesRef=FirebaseFirestore.getInstance().collection("postsLikes").document(postId)

        val database=FirebaseFirestore.getInstance()

        database.runTransaction { transaction->
            val snapshot=transaction.get(postRef)
            val likeCount=snapshot.getLong("likes")
            if (likeCount != null) {
                if (likeCount >= 0){
                    if(liked){
                        if(transaction.get(postsLikesRef).exists()){
                            transaction.update(postsLikesRef,"likes", FieldValue.arrayUnion(uid))

                        }else{
                            transaction.set(postsLikesRef, hashMapOf("likes" to arrayListOf(uid)),
                                SetOptions.merge())
                        }

                        transaction.update(postRef,"likes",increment)
                    }else{
                        transaction.update(postRef,"likes",decrement)
                        transaction.update(postsLikesRef,"likes", FieldValue.arrayRemove(uid))


                    }
                }
            }
        }.addOnSuccessListener {

        }.addOnFailureListener {
            throw Exception(it.message)
        }

    }
}


