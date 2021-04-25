package com.example.simpleloginappwithfirebase.presentation.welcomescreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.simpleloginappwithfirebase.R
import com.example.simpleloginappwithfirebase.databinding.WelcomeFragmentBinding
import com.example.simpleloginappwithfirebase.presentation.common.BaseFragment
import org.koin.android.viewmodel.ext.android.getViewModel

class WelcomeFragment : BaseFragment<WelcomeFragmentBinding, WelcomeViewModel>() {


    override fun viewModel(): WelcomeViewModel = getViewModel()

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): WelcomeFragmentBinding {
        return WelcomeFragmentBinding.inflate(inflater, container, false)
    }

    override fun subscribeToUiChanges() {}

    override fun subscribeToEvents() {}

    override fun prepareUi() {
        binding.listButton.setOnClickListener { moveToItemsScreen() }
    }

    private fun moveToItemsScreen() {
        findNavController().navigate(R.id.action_welcomeFragment_to_itemsFragment)
    }

    companion object {
        fun newInstance() = WelcomeFragment()
    }

}