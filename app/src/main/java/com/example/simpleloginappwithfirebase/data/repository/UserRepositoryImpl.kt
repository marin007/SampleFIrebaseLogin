package com.example.simpleloginappwithfirebase.data.repository

import com.example.simpleloginappwithfirebase.R
import com.example.simpleloginappwithfirebase.domain.entity.user.User
import com.example.simpleloginappwithfirebase.domain.repostiory.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.simpleloginappwithfirebase.data.common.isNetworkError
import com.example.simpleloginappwithfirebase.domain.common.StringProvider

class UserRepositoryImpl(
    private val stringProvider: StringProvider
) : UserRepository {

    private val auth: FirebaseAuth = Firebase.auth

    private val fireStore = Firebase.firestore

    override fun logOut() {
        auth.signOut()
    }

    override fun getLoggedUserEmail(): String? {
        return auth.currentUser?.email
    }

    override fun isUserSignedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun logIn(
        email: String,
        password: String,
        onLoginListenerOn: UserRepository.OnUserLoginListener
    ) {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener({ command -> command.run() }) { task ->
                    when {
                        task.isSuccessful -> onLoginListenerOn.onSuccess(task.result?.additionalUserInfo?.isNewUser)
                        isNetworkError(task.exception) -> {
                            onLoginListenerOn.onFail(stringProvider.getString(R.string.error_network))
                        }
                        else -> onLoginListenerOn.onFail(task.exception?.message.orEmpty())
                    }
                }
        } catch (e: java.lang.Exception) {
            if (isNetworkError(e)) onLoginListenerOn.onNetworkError()
            else onLoginListenerOn.onFail(e.message.orEmpty())
        }

    }

    override fun createUser(
        email: String,
        password: String,
        onCreateUserListener: UserRepository.OnCreateUserListener
    ) {
        try {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    when {
                        task.isSuccessful -> onCreateUserListener.onSuccess()
                        isNetworkError(task.exception) -> {
                            onCreateUserListener.onFail(stringProvider.getString(R.string.error_network))
                        }
                        else -> onCreateUserListener.onFail(task.exception?.message.orEmpty())
                    }
                }

        } catch (e: Exception) {
            if (isNetworkError(e)) onCreateUserListener.onNetworkError()
            else onCreateUserListener.onFail(e.message.orEmpty())
        }
    }

    override fun addCreatedUser(
        user: User,
        onAddCreatedUserListener: UserRepository.OnAddCreatedUserListener
    ) {
        try {

            fireStore.collection(USER_COLLECTION_NAME).add(user)
                .addOnSuccessListener { onAddCreatedUserListener.onSuccess() }
                .addOnFailureListener { error ->
                    if (isNetworkError(error)) onAddCreatedUserListener.onNetworkError()
                    else onAddCreatedUserListener.onFail(error.message.orEmpty())
                }
        } catch (e: Exception) {
            if (isNetworkError(e)) onAddCreatedUserListener.onNetworkError()
            else onAddCreatedUserListener.onFail(e.message.orEmpty())
        }
    }


    companion object {
        const val USER_COLLECTION_NAME = "users"
        const val ITEMS_COLLECTION_NAME = "items"
        const val KEY_ITEM_DESCRIPTION = "description"
    }
}