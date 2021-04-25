package com.example.simpleloginappwithfirebase.presentation.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simpleloginappwithfirebase.R
import com.example.simpleloginappwithfirebase.domain.common.StringProvider
import com.example.simpleloginappwithfirebase.domain.entity.user.User
import com.example.simpleloginappwithfirebase.domain.repostiory.UserRepository
import com.example.simpleloginappwithfirebase.presentation.common.ValueWrapper
import com.example.simpleloginappwithfirebase.presentation.common.isEmailValid
import com.example.simpleloginappwithfirebase.presentation.common.isPasswordValid

class SignUpViewModel(
    private val stringProvider: StringProvider,
    private val userRepository: UserRepository
) : ViewModel() {

    val uiState: MutableLiveData<UiState> by lazy {
        MutableLiveData<UiState>()
    }

    val event: MutableLiveData<ValueWrapper<Event>> by lazy {
        MutableLiveData<ValueWrapper<Event>>()
    }

    fun onCreateUser(password: String, user: User) {
        if (user.email.isEmpty()) {
            event.value = ValueWrapper(
                Event.Error(
                    stringProvider.getString(
                        R.string.missing_email
                    )
                )
            )
            return
        }
        if (!isEmailValid(user.email)) {
            uiState.value = UiState.ValidationError(
                stringProvider.getString(
                    R.string.login_error_invalid_email
                )
            )

            return
        } else if (!isPasswordValid(password)) {
            uiState.value = UiState.ValidationError(
                stringProvider.getString(
                    R.string.login_error_invalid_password
                )
            )

            return
        } else {
            uiState.value = UiState.Loading
            userRepository.createUser(email = user.email, password = password, object :
                UserRepository.OnCreateUserListener {
                override fun onSuccess() {
                    addCreatedUser(user = user)
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

                override fun onFail(error: String) {
                    event.value = ValueWrapper(Event.Error(error))
                }
            })
        }
    }

    private fun addCreatedUser(user: User) {
        userRepository.addCreatedUser(
            user = user,
            object : UserRepository.OnAddCreatedUserListener {
                override fun onSuccess() {
                    event.value = ValueWrapper(
                        Event.UserCreatedSuccess(
                            stringProvider.getString(
                                R.string.success_user_create
                            )
                        )
                    )
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

                override fun onFail(error: String) {
                    event.value = ValueWrapper(Event.Error(error))
                }
            })
    }

    sealed class UiState {
        object Loading : UiState()
        data class ValidationError(val message: String) : UiState()
    }

    sealed class Event {
        data class Error(val message: String) : Event()
        data class UserCreatedSuccess(val message: String) : Event()
    }
}