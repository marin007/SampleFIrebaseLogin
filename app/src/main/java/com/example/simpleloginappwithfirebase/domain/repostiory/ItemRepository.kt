package com.example.simpleloginappwithfirebase.domain.repostiory

import com.example.simpleloginappwithfirebase.domain.entity.itemnote.Item

interface ItemRepository {
    fun getAllItemsFromRemote(onGetAllItemsListener: OnGetAllItemsListener)

    fun addItemToRemote(item: Item, onAddItemListener: OnAddItemListener)

    fun deleteItemFromRemote(documentId: String, onRemoveItemListener: OnRemoveItemListener)

    suspend fun insertItemInDb(item: Item)

    suspend fun getAllItemsFromDb() : List<Item>

    suspend fun deleteItemFromDb(itemId: Int)

    interface OnGetAllItemsListener {
        fun onSuccess(items: List<Item>)

        fun onNetworkError()

        fun onFail(error: String)
    }

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