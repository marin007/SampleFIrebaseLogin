package com.example.simpleloginappwithfirebase.presentation.listdata

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleloginappwithfirebase.R
import com.example.simpleloginappwithfirebase.databinding.ItemsFragmentBinding
import com.example.simpleloginappwithfirebase.domain.entity.itemnote.Item
import com.example.simpleloginappwithfirebase.presentation.common.*
import org.koin.android.viewmodel.ext.android.getViewModel

class ItemsFragment : BaseFragment<ItemsFragmentBinding, ItemsViewModel>() {

    lateinit var adapter: ItemsAdapter

    override fun viewModel(): ItemsViewModel = getViewModel()

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ItemsFragmentBinding {
        return ItemsFragmentBinding.inflate(inflater, container, false)
    }

    override fun subscribeToUiChanges() {

    }

    override fun subscribeToEvents() {
        val swipeHandler = object : SwipeToDelete(secureContext) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.getItemByPosition(viewHolder.adapterPosition)
                adapter.removeAt(viewHolder.adapterPosition)
                if (viewModel.getDataFromDb) {
                    item?.let { it.id?.let { it1 -> viewModel.deleteItemFromDB(it1) } }
                } else {
                    item?.documentId?.let(viewModel::deleteItemFromRemote)
                }

            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        viewModel.event.observe(this, { event ->
            event.get()?.let {
                when (it) {
                    is ItemsViewModel.Event.Error -> showErrorToast(secureContext, it.message)
                }
            }
        })
    }

    override fun prepareUi() {
        binding.recyclerView.addItemDecoration(getRecycleViewDivider(secureContext))
        binding.recyclerView.setHasFixedSize(false)
        binding.recyclerView.layoutManager = LinearLayoutManager(secureContext)
        binding.textViewProfile.setOnClickListener { moveToProfileScreen() }
        binding.addItemButton.setOnClickListener { showInfoDialog(secureContext, ::getItemDesc) }
        viewModel.itemsFromRemote.observe(viewLifecycleOwner) {
            if(!viewModel.getDataFromDb){
                loadItemsData(it)
            }
        }
        viewModel.itemsFromDB.observe(viewLifecycleOwner) {
            if(viewModel.getDataFromDb){
                loadItemsData(it.map { item -> item.toItem() })
            }
        }
        binding.switchDataSource.setOnCheckedChangeListener { _, isChecked ->
            viewModel.getDataFromDb = !isChecked
            if (viewModel.getDataFromDb) {
                viewModel.itemsFromDB.value?.map { it.toItem() }?.let { loadItemsData(it) }
            } else {
                viewModel.itemsFromRemote.value?.let { loadItemsData(it) }
            }
        }
    }

    private fun loadItemsData(items: List<Item>) {
        val list = mutableListOf<Item>()
        list.addAll(items)
        adapter = ItemsAdapter(list)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun moveToProfileScreen() {
        findNavController().navigate(R.id.action_itemsFragment_to_profileFragment)
    }

    private fun getItemDesc(desc: String) {
        viewModel.insertItem(Item(description = desc))
    }

}