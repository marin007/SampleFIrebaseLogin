package com.example.simpleloginappwithfirebase.presentation.signup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.simpleloginappwithfirebase.R
import com.example.simpleloginappwithfirebase.databinding.SignUpFragmentBinding
import com.example.simpleloginappwithfirebase.domain.entity.user.User
import com.example.simpleloginappwithfirebase.presentation.common.BaseFragment
import com.example.simpleloginappwithfirebase.presentation.common.showErrorToast
import com.example.simpleloginappwithfirebase.presentation.common.showSuccessToast
import org.koin.android.viewmodel.ext.android.getViewModel

class SignUpFragment : BaseFragment<SignUpFragmentBinding, SignUpViewModel>() {


    override fun viewModel(): SignUpViewModel = getViewModel()

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SignUpFragmentBinding {
        return SignUpFragmentBinding.inflate(inflater, container, false)
    }

    override fun subscribeToUiChanges() {
        viewModel.uiState.observe(viewLifecycleOwner, { uiState ->
            when (uiState) {
                is SignUpViewModel.UiState.Loading -> showLoading()
                is SignUpViewModel.UiState.ValidationError -> showErrorToast(
                    secureContext,
                    uiState.message
                )
            }
        })
    }

    override fun subscribeToEvents() {

        viewModel.event.observe(viewLifecycleOwner, { event ->
            event.get()?.let {
                when (it) {
                    is SignUpViewModel.Event.Error -> {
                        dismissLoading()
                        showErrorToast(secureContext, it.message)
                    }

                    is SignUpViewModel.Event.UserCreatedSuccess -> {
                        showSuccessToast(secureContext, it.message)
                        dismissLoading()
                        moveToWelcomeScreen()
                    }
                }
            }
        })
    }

    override fun prepareUi() {
        binding.signUpButton.setOnClickListener {
            val user = User(
                email = binding.emailInput.text.toString(),
            )
            viewModel.onCreateUser(
                binding.passwordInput.text.toString(),
                user
            )
        }
        binding.textViewLabelSingIn.setOnClickListener { findNavController().popBackStack() }
    }

    private fun showLoading() {
        binding.progress.visibility = View.VISIBLE
        binding.signUpButton.isEnabled = false
    }

    private fun dismissLoading() {
        binding.progress.visibility = View.GONE
        binding.signUpButton.isEnabled = true
    }

    private fun moveToWelcomeScreen() {
        findNavController().navigate(R.id.action_signUpFragment_to_welcomeFragment)
    }

}