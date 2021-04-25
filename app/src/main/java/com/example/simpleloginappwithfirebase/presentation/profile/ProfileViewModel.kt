package com.example.simpleloginappwithfirebase.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simpleloginappwithfirebase.domain.repostiory.UserRepository

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    val loggedEmail: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    init {
        loggedEmail.value = userRepository.getLoggedUserEmail()
    }

    fun logout() {
        userRepository.logOut()
    }
}