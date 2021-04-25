package com.example.simpleloginappwithfirebase.presentation.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding, V : ViewModel> : Fragment() {


    lateinit var viewModel: V

    private var _binding: T? = null

    val binding get() = _binding!!

    lateinit var secureContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = binding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareUi()
        subscribeToUiChanges()
        subscribeToEvents()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        secureContext = context
    }

    fun displayErrorMessage(message: String) {
        showErrorToast(secureContext, message)
    }

    abstract fun viewModel(): V

    abstract fun binding(inflater: LayoutInflater, container: ViewGroup?): T

    abstract fun subscribeToUiChanges()

    abstract fun subscribeToEvents()

    abstract fun prepareUi()

}