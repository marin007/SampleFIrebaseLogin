package com.example.simpleloginappwithfirebase.presentation.profile


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.simpleloginappwithfirebase.R
import com.example.simpleloginappwithfirebase.databinding.ProfileFragmentBinding
import com.example.simpleloginappwithfirebase.presentation.common.BaseFragment
import org.koin.android.viewmodel.ext.android.getViewModel

class ProfileFragment : BaseFragment<ProfileFragmentBinding, ProfileViewModel>() {

    override fun viewModel(): ProfileViewModel = getViewModel()

    override fun binding(
            inflater: LayoutInflater,
            container: ViewGroup?
    ): ProfileFragmentBinding {
        return ProfileFragmentBinding.inflate(inflater, container, false)
    }
 
    override fun subscribeToUiChanges() { }

    override fun subscribeToEvents() {
        viewModel.loggedEmail.observe(this) {
            it?.let {
                binding.textViewUserEmail.text = it
            }
        }
    }

    override fun prepareUi() {
        binding.loginOutButton.setOnClickListener { logout() }
        binding.textViewBack.setOnClickListener { findNavController().popBackStack() }
    }

    private fun logout () {
        viewModel.logout()
        moveToLoginScreen()
    }

    private fun moveToLoginScreen() {
        findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
    }
}