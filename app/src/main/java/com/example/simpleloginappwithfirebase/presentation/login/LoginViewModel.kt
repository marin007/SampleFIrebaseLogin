package com.example.simpleloginappwithfirebase.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simpleloginappwithfirebase.R
import com.example.simpleloginappwithfirebase.domain.common.StringProvider
import com.example.simpleloginappwithfirebase.domain.entity.itemnote.Item
import com.example.simpleloginappwithfirebase.domain.repostiory.UserRepository
import com.example.simpleloginappwithfirebase.presentation.common.ValueWrapper
import com.example.simpleloginappwithfirebase.presentation.common.isEmailValid
import com.example.simpleloginappwithfirebase.presentation.common.isPasswordValid
import com.example.simpleloginappwithfirebase.presentation.listdata.ItemsViewModel
import com.example.simpleloginappwithfirebase.presentation.signup.SignUpViewModel

class LoginViewModel(
    private val userRepository: UserRepository,
    private val stringProvider: StringProvider
) : ViewModel() {

    val uiState: MutableLiveData<UiState> by lazy {
        MutableLiveData<UiState>()
    }

    val event: MutableLiveData<ValueWrapper<Event>> by lazy {
        MutableLiveData<ValueWrapper<Event>>()
    }

    init {
        if (userRepository.isUserSignedIn()) {
            event.value = ValueWrapper(
                Event.UserLoggedIn
            )
        }
    }

    fun onLoginPressed(email: String, password: String) {
        if (!isEmailValid(email = email)) {
            uiState.value = UiState.InvalidEmail
        } else if (!isPasswordValid(password = password)) {
            uiState.value = UiState.InvalidPassword
        } else {
            uiState.value = UiState.Loading

            userRepository.logIn(email, password, object : UserRepository.OnUserLoginListener {
                override fun onFail(error: String) {
                    uiState.value = UiState.Error(error)
                }

                override fun onNetworkError() {
                    event.value = ValueWrapper(
                        Event.Error(
                            stringProvider.getString(
                                R.string.error_network
                            )
                        )
                    )
                }

                override fun onSuccess(isNewUser: Boolean?) {
                    event.value = ValueWrapper(
                        Event.LoginSuccess(isNewUser = isNewUser)
                    )
                }
            })
        }
    }





    sealed class UiState {
        object Loading : UiState()
        object InvalidEmail : UiState()
        object InvalidPassword : UiState()
        data class Error(val message: String) : UiState()
    }

    sealed class Event {
        data class LoginSuccess(val isNewUser: Boolean?) : Event()
        object UserLoggedIn : Event()
        data class Error(val message: String) : Event()
    }
}