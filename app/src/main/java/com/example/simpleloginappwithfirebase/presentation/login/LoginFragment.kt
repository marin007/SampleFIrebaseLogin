package com.example.simpleloginappwithfirebase.presentation.login


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.fragment.findNavController
import com.example.simpleloginappwithfirebase.R
import com.example.simpleloginappwithfirebase.databinding.LoginFragmentBinding
import com.example.simpleloginappwithfirebase.presentation.common.BaseFragment
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.getViewModel

class LoginFragment : BaseFragment<LoginFragmentBinding, LoginViewModel> () {


    override fun viewModel(): LoginViewModel = getViewModel()

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): LoginFragmentBinding {
        return LoginFragmentBinding.inflate(inflater, container, false)
    }

    override fun subscribeToUiChanges() {
        viewModel.uiState.observe(viewLifecycleOwner, { uiState ->
            when(uiState) {
                is LoginViewModel.UiState.InvalidEmail -> showInvalidEmail()
                is LoginViewModel.UiState.InvalidPassword -> showInvalidPassword()
                is LoginViewModel.UiState.Error -> showErrorSnackBar(uiState.message)
                is LoginViewModel.UiState.Loading -> showLoading()
            }
        })
    }

    override fun subscribeToEvents() {

        viewModel.event.observe(viewLifecycleOwner, { event ->
            event.get()?.let {
                when (it) {
                    is LoginViewModel.Event.LoginSuccess -> if(it.isNewUser == true) moveToWelcomeScreen() else moveToItemsScreen()
                    is LoginViewModel.Event.UserLoggedIn -> moveToItemsScreen()
                }
            }
        })
    }

    override fun prepareUi() {
        binding.loginButton.setOnClickListener {
            clearErrors()
            viewModel.onLoginPressed(
                binding.emailInput.text.toString(),
                binding.passwordInput.text.toString()
            )
        }
        binding.textViewSignUp.setOnClickListener {
           moveToSignUpScreen()
        }
    }

    private fun showInvalidEmail() {
        binding.emailInput.error = getString(R.string.login_error_invalid_email)
    }

    private fun showInvalidPassword() {
        binding.emailInput.error = getString(R.string.login_error_invalid_password)
    }

    private fun showErrorSnackBar(message: String) {
        dismissLoading()
        Snackbar.make(binding.snackbarContainer, message, Snackbar.LENGTH_LONG).let {
            it.setBackgroundTint(getColor(secureContext, R.color.red))
            it.show()
        }
    }

    private fun moveToWelcomeScreen() {
        dismissLoading()
        findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)
    }

    private fun moveToItemsScreen() {
        dismissLoading()
        findNavController().navigate(R.id.action_loginFragment_to_itemsFragment)
    }

    private fun moveToSignUpScreen() {
        findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
    }

    private fun clearErrors() {
        binding.emailLayoutInput.error = ""
        binding.passwordLayoutInput.error = ""
    }

    private fun showLoading() {
        binding.progress.visibility = View.VISIBLE
        binding.loginButton.isEnabled = false
    }

    private fun dismissLoading() {
        binding.progress.visibility = View.GONE
        binding.loginButton.isEnabled = true
    }
}