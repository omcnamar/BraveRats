package com.olegmcnamara.braverats.remote.auth

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.lang.Exception

class Authentication private constructor(){

    // singleton design
    private object Holder { val INSTANCE = Authentication() }
    companion object {
        val instance: Authentication by lazy { Holder.INSTANCE }
    }

    private lateinit var apiKey: String
    private lateinit var databaseUrl: String
    private lateinit var projectId: String
    private lateinit var applicationId: String
    private var hasBeenInitializedForAdmin = false

    fun init(apiKey: String, databaseUrl: String, projectId: String, applicationId: String){
        this.apiKey = apiKey
        this.databaseUrl = databaseUrl
        this.projectId = projectId
        this.applicationId = applicationId
        hasBeenInitializedForAdmin = true
    }

    private val mAuth = FirebaseAuth.getInstance()
    var currentUser: FirebaseUser? = mAuth.currentUser
        private set

    fun createUserWithAdmin(email: String,
                            password: String,
                            context: Context,
                            success: () -> Unit,
                            failure: (Exception) -> Unit) {

        if (hasBeenInitializedForAdmin) {
            val firebaseOptionsBuilder = FirebaseOptions.Builder()
            firebaseOptionsBuilder.setApiKey(apiKey)
            firebaseOptionsBuilder.setDatabaseUrl(databaseUrl)
            firebaseOptionsBuilder.setProjectId(projectId)
            firebaseOptionsBuilder.setApplicationId(applicationId) //not sure if this one is needed
            val firebaseOptions = firebaseOptionsBuilder.build()


            val newAuth = FirebaseApp.initializeApp(context, firebaseOptions, "secondDbName")
            FirebaseAuth.getInstance(newAuth).createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {

                            // DELETE SECONDARY AUTH! thanks, Jimmy :D
                            newAuth.delete()
                            success()

                        } else {
                            task.exception?.let { exception ->
                                failure(exception)
                            }
                        }

                    }

        } else {
            failure(Exception("instance function init(apiKey: String, databaseUrl: String, projectId: String, applicationId: String) must be called prior use of this function"))
        }
    }

    fun createUser(email: String,
                   password: String,
                   success: (FirebaseUser) -> Unit,
                   failure: (Exception) -> Unit) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        success(currentUser!!)
                    } else {
                        task.exception?.let { exception ->
                            failure(exception)
                        }
                    }
                }
    }

    fun loginUser(email: String,
                  password: String,
                  success: (FirebaseUser) -> Unit,
                  failure: (Exception) -> Unit) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        currentUser = mAuth.currentUser
                        success(currentUser!!)
                    } else {
                        task.exception?.let { exception ->
                            failure(exception)
                        }
                    }
                }
    }

    fun logoutUser(success: () -> Unit) {
        mAuth.signOut()
        currentUser = null
        success()
    }

}