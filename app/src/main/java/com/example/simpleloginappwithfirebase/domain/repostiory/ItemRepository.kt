package com.example.simpleloginappwithfirebase.domain.repostiory

import com.example.simpleloginappwithfirebase.domain.entity.itemnote.Item

interface ItemRepository {
    fun getAllItems(onGetAllItemsListener: OnGetAllItemsListener)

    fun addItemToList(item: Item, onAddItemListener: OnAddItemListener)

    fun removeItemFromList(documentId: String, onRemoveItemListener: OnRemoveItemListener)

    suspend fun insertItemInCache(item: Item)

    suspend fun getAllItemsFromCache() : List<Item>

    suspend fun removeItemFromCache(itemId: Int)

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