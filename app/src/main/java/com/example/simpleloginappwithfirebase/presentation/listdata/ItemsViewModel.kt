package com.example.simpleloginappwithfirebase.presentation.listdata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleloginappwithfirebase.R
import com.example.simpleloginappwithfirebase.domain.common.StringProvider
import com.example.simpleloginappwithfirebase.domain.entity.itemnote.Item
import com.example.simpleloginappwithfirebase.domain.repostiory.ItemRepository
import com.example.simpleloginappwithfirebase.presentation.common.ValueWrapper
import kotlinx.coroutines.*

class ItemsViewModel(
    private val itemRepository: ItemRepository,
    private val stringProvider: StringProvider
) : ViewModel() {

    var getDataFromDb: Boolean = false

    val event: MutableLiveData<ValueWrapper<Event>> by lazy {
        MutableLiveData<ValueWrapper<Event>>()
    }

    val items: MutableLiveData<List<Item>> by lazy {
        MutableLiveData<List<Item>>()
    }

    init {
        fetchItems()
    }

    private fun insertItemInDB(item: Item) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                itemRepository.insertItemInDb(
                    item = item
                )
            }
            getAllItemsFromDB()
        }
    }

    private fun getAllItemsFromDB() {
        viewModelScope.launch {
            items.value = itemRepository.getAllItemsFromDb()
        }
    }

    private fun addItemToRemote (item: Item) {
        itemRepository.addItemToRemote(item = item, object : ItemRepository.OnAddItemListener {
            override fun onFail(error: String) {
                event.value = ValueWrapper(
                    Event.Error(
                        error
                    )
                )
            }

            override fun onSuccess() {
                fetchItems()
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
        })
    }

    private fun getAllItemsFromRemote() {
        itemRepository.getAllItemsFromRemote(object : ItemRepository.OnGetAllItemsListener {
            override fun onFail(error: String) {
                event.value = ValueWrapper(
                    Event.Error(
                        error
                    )
                )
            }

            override fun onSuccess(items: List<Item>) {
                this@ItemsViewModel.items.value = items
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
        })
    }

    fun deleteItemFromDB(itemId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                itemRepository.deleteItemFromDb(itemId = itemId)
            }
            getAllItemsFromDB()
        }
    }

    fun insertItem(item: Item) {
        if (getDataFromDb) {
            insertItemInDB(item = item)
        } else {
            addItemToRemote(item = item)
        }
    }

    fun fetchItems() {
        if (getDataFromDb) {
            getAllItemsFromDB()
        } else {
            getAllItemsFromRemote()
        }
    }

    fun deleteItemFromRemote(documentId: String) {
        itemRepository.deleteItemFromRemote(
            documentId = documentId,
            object : ItemRepository.OnRemoveItemListener {
                override fun onFail(error: String) {
                    fetchItems()
                }

                override fun onSuccess() {
                    fetchItems()
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
            })
    }

    sealed class Event {
        data class Error(val message: String) : Event()
    }
}