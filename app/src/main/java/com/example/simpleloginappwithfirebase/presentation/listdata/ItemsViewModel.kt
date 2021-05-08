package com.example.simpleloginappwithfirebase.presentation.listdata

import androidx.lifecycle.*
import com.example.simpleloginappwithfirebase.R
import com.example.simpleloginappwithfirebase.data.repository.model.ItemEntity
import com.example.simpleloginappwithfirebase.domain.common.StringProvider
import com.example.simpleloginappwithfirebase.domain.entity.itemnote.Item
import com.example.simpleloginappwithfirebase.domain.repostiory.ItemRepository
import com.example.simpleloginappwithfirebase.presentation.common.ValueWrapper
import kotlinx.coroutines.launch

class ItemsViewModel(
    private val itemRepository: ItemRepository,
    private val stringProvider: StringProvider
) : ViewModel() {

    var getDataFromDb: Boolean = false

    val event: MutableLiveData<ValueWrapper<Event>> by lazy {
        MutableLiveData<ValueWrapper<Event>>()
    }

    val itemsFromRemote: LiveData<List<Item>> = itemRepository.getAllItemsFromRemote().asLiveData()

    val itemsFromDB: LiveData<List<ItemEntity>> = itemRepository.getAllItemsFromDb().asLiveData()

    private fun insertItemInDB(item: Item) {
        viewModelScope.launch {
            itemRepository.insertItemInDb(
                item = item
            )
        }
    }

    private fun addItemToRemote(item: Item) {
        itemRepository.addItemToRemote(item = item, object : ItemRepository.OnAddItemListener {
            override fun onFail(error: String) {
                event.value = ValueWrapper(
                    Event.Error(
                        error
                    )
                )
            }

            override fun onSuccess() {}

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
            itemRepository.deleteItemFromDb(itemId = itemId)
        }
    }

    fun insertItem(item: Item) {
        if (getDataFromDb) {
            insertItemInDB(item = item)
        } else {
            addItemToRemote(item = item)
        }
    }

    fun deleteItemFromRemote(documentId: String) {
        itemRepository.deleteItemFromRemote(
            documentId = documentId,
            object : ItemRepository.OnRemoveItemListener {
                override fun onFail(error: String) {
                    //fetchItems()
                }

                override fun onSuccess() {
                    //fetchItems()
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