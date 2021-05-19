package com.example.socialmediaapp.daos

import com.example.socialapp.models.Users
import com.example.socialmediaapp.models.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {
    val db = FirebaseFirestore.getInstance()

    val postCollection = db.collection("posts")
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun addPost(text: String) {
        val currentUserId = auth.currentUser!!.uid
        GlobalScope.launch {
            val userDao = UserDao()
            val users = userDao.getUserById(currentUserId).await().toObject(Users::class.java)!!

            val currentTime = System.currentTimeMillis()
            val post = Post(text, users, currentTime)
            postCollection.document().set(post)

        }

    }
    fun getPostById(postId: String): Task<DocumentSnapshot> {
        return postCollection.document(postId).get()
    }


    fun updateLikes(postId: String) {
        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid
            val post = getPostById(postId).await().toObject(Post::class.java)!!
            val isLiked = post.likedBy.contains(currentUserId)

            if(isLiked) {
                post.likedBy.remove(currentUserId)
            } else {
                post.likedBy.add(currentUserId)
            }
            postCollection.document(postId).set(post)
        }

    }


}