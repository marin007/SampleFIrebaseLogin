package com.example.simpleloginappwithfirebase.domain.repostiory

import com.example.simpleloginappwithfirebase.domain.entity.itemnote.Item
import com.example.simpleloginappwithfirebase.domain.entity.user.User
import java.util.*

interface UserRepository {

    fun logIn(email: String, password: String, onLoginListenerOn: OnUserLoginListener)

    fun logOut()

    fun getLoggedUserEmail() : String?

    fun isUserSignedIn(): Boolean

    fun createUser(email: String, password: String, onCreateUserListener: OnCreateUserListener)

    fun addCreatedUser(user: User, onAddCreatedUserListener: OnAddCreatedUserListener)

    interface OnCreateUserListener {
        fun onSuccess()

        fun onNetworkError()

        fun onFail(error: String)
    }

    interface OnAddCreatedUserListener {
        fun onSuccess()

        fun onNetworkError()

        fun onFail(error: String)
    }

    interface OnUserLoginListener {
        fun onSuccess(isNewUser: Boolean?)

        fun onFail(error: String)

        fun onNetworkError()
    }
}