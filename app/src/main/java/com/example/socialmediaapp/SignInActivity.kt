package com.example.socialmediaapp

//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.socialapp.models.Users
import com.example.socialmediaapp.daos.UserDao
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext



class SignInActivity : AppCompatActivity() {

    private val RC_SIGN_IN: Int = 123
    private lateinit var googleSignInClient: GoogleSignInClient
    private val TAG = "SignInActivity Tag"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()
//        auth = Firebase.auth

        val signInButton = findViewById<SignInButton>(R.id.signInButton) as SignInButton
        //set listener
        signInButton.setOnClickListener {
            signIn()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)!!
            Log.e(TAG, "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)

        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        GlobalScope.launch(Dispatchers.IO) {

            val signInButton = findViewById<SignInButton>(R.id.signInButton) as SignInButton
            signInButton.visibility = View.GONE

            val progressBar = findViewById<ProgressBar>(R.id.progressBar) as ProgressBar
            progressBar.visibility = View.VISIBLE

            val auth = auth.signInWithCredential(credential).await()

            val firebaseUser = auth.user
            withContext(Dispatchers.Main) {
                Log.e("user", "logged in UI Pending")

                updateUI(firebaseUser)
            }

        }

    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        if(firebaseUser != null) {

            Log.e("user", "logged in")

            val user_ID = firebaseUser.uid
            val user_Name = firebaseUser.displayName
            val user_PhotoUrl = firebaseUser.photoUrl.toString()


            val user = Users(firebaseUser.uid,user_Name , user_PhotoUrl)
            val usersDao = UserDao()
            usersDao.addUser(user)

            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
        } else {

            val signInButton = findViewById<SignInButton>(R.id.signInButton) as SignInButton
            signInButton.visibility = View.VISIBLE
            val progressBar = findViewById<ProgressBar>(R.id.progressBar) as ProgressBar
            progressBar.visibility = View.GONE

        }
    }

}