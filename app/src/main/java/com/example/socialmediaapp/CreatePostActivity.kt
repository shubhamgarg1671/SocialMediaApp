package com.example.socialmediaapp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmediaapp.daos.PostDao

class CreatePostActivity : AppCompatActivity() {
    private lateinit var postDao:PostDao

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        postDao = PostDao()
        val postInput: EditText = findViewById(R.id.postInput)
        val postButton: View = findViewById(R.id.postButton)
        postButton.setOnClickListener {
            val input = postInput.text.toString().trim()
            if (input.isNotEmpty()) {
                postDao.addPost(input)
                finish()
            }

    }
}
}