package com.example.notesapp.repository


import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class AuthRepository(
    val context:Context
) {
    lateinit var googleSignInClient:GoogleSignInClient
    val currentUser: FirebaseUser? = Firebase.auth.currentUser


    // A function to check if user exist or not
    fun hasUser(): Boolean = Firebase.auth.currentUser != null

    // function for getting uid
    fun getUser(): String = Firebase.auth.currentUser?.uid.orEmpty()

    // function for creating a user
    suspend fun createUser(
        email: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ) = withContext(Dispatchers.IO){
        Firebase.auth
            .createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    onComplete.invoke(true)

                }else{
                    onComplete.invoke(false)
                }
            }.await()
    }
    suspend fun signIn(
        email: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ) = withContext(Dispatchers.IO){
        Firebase.auth
            .signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    onComplete.invoke(true)

                }else{
                    onComplete.invoke(false)
                }
            }.await()
    }
    fun signOut(){
        Firebase.auth.signOut()
    }
    fun forgotPassword(
        email: String,
        onComplete: (Boolean) -> Unit
    ){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete.invoke(true)
                }else{
                    onComplete.invoke(false )
                }
            }
    }

}