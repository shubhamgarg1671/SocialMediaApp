package com.example.socialmediaapp.models

import com.example.socialapp.models.Users

/*class Post(text1: String, user1: Users, currentTime1: Long) {
    val text: String = text1
    val createdBy: Users = user1
    val createdAt: Long = currentTime1
    val likedBy: ArrayList<String> = ArrayList()
    Post() {
         text: String = ""
         createdBy: Users = Users()
         createdAt: Long = 0L
         likedBy: ArrayList<String> = ""

    }

    constructor(
        messageText: String,
        messageUser: String,
        messageUserId: String) {
        this.messageText = messageText
        this.messageUser = messageUser
        this.messageUserId = messageUserId
    }

    constructor()

}

*/


data class Post (
    val text: String = "",
    val createdBy: Users = Users(),
    val createdAt: Long = 0L,
    val likedBy: ArrayList<String> = ArrayList())