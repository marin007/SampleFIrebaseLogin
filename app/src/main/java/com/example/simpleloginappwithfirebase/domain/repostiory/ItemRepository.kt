package com.example.simpleloginappwithfirebase.domain.repostiory

import androidx.lifecycle.LiveData
import com.example.simpleloginappwithfirebase.data.repository.model.ItemEntity
import com.example.simpleloginappwithfirebase.domain.entity.itemnote.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {

    fun getAllItemsFromRemote() : Flow<List<Item>>

    fun addItemToRemote(item: Item, onAddItemListener: OnAddItemListener)

    fun deleteItemFromRemote(documentId: String, onRemoveItemListener: OnRemoveItemListener)

    suspend fun insertItemInDb(item: Item)

    fun getAllItemsFromDb(): Flow<List<ItemEntity>>

    suspend fun deleteItemFromDb(itemId: Int)

    interface OnRemoveItemListener {
        fun onSuccess()

        fun onNetworkError()

        fun onFail(error: String)
    }

    interface OnAddItemListener {
        fun onSuccess()

        fun onNetworkError()

        fun onFail(error: String)
    }
}