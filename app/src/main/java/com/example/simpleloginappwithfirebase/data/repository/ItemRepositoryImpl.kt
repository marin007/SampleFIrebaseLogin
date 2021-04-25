package com.example.simpleloginappwithfirebase.data.repository

import com.example.simpleloginappwithfirebase.R
import com.example.simpleloginappwithfirebase.data.common.isNetworkError
import com.example.simpleloginappwithfirebase.data.repository.model.ItemEntity
import com.example.simpleloginappwithfirebase.domain.common.StringProvider
import com.example.simpleloginappwithfirebase.domain.entity.itemnote.Item
import com.example.simpleloginappwithfirebase.domain.repostiory.ItemRepository
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemRepositoryImpl(
    private val stringProvider: StringProvider,
    private val dataBase: AppDataBase
) : ItemRepository {

    private val fireStore = Firebase.firestore

    override fun getAllItems(onGetAllItemsListener: ItemRepository.OnGetAllItemsListener) {
        fireStore.collection(UserRepositoryImpl.ITEMS_COLLECTION_NAME)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    onGetAllItemsListener.onFail(stringProvider.getString(R.string.empty_collection))
                } else {
                    val items = arrayListOf<Item>()

                    for (document in documents) {
                        items.add(fromQuery(document))
                    }
                    onGetAllItemsListener.onSuccess(items)
                }
            }
            .addOnFailureListener { exception ->
                onGetAllItemsListener.onFail(exception.message.orEmpty())
            }
    }

    override fun addItemToList(item: Item, onAddItemListener: ItemRepository.OnAddItemListener) {
        try {
            fireStore.collection(UserRepositoryImpl.ITEMS_COLLECTION_NAME).add(item)
                .addOnSuccessListener { onAddItemListener.onSuccess() }
                .addOnFailureListener { error ->
                    if (isNetworkError(error)) onAddItemListener.onNetworkError()
                    else onAddItemListener.onFail(error.message.orEmpty())
                }
        } catch (e: Exception) {
            if (isNetworkError(e)) onAddItemListener.onNetworkError()
            else onAddItemListener.onFail(e.message.orEmpty())
        }
    }

    override fun removeItemFromList(
        documentId: String,
        onRemoveItemListener: ItemRepository.OnRemoveItemListener
    ) {
        try {
            fireStore.collection(UserRepositoryImpl.ITEMS_COLLECTION_NAME).document(documentId)
                .delete()
                .addOnSuccessListener { onRemoveItemListener.onSuccess() }
                .addOnFailureListener { error ->
                    if (isNetworkError(error)) onRemoveItemListener.onNetworkError()
                    else onRemoveItemListener.onFail(error.message.orEmpty())
                }
        } catch (e: Exception) {
            if (isNetworkError(e)) onRemoveItemListener.onNetworkError()
            else onRemoveItemListener.onFail(e.message.orEmpty())
        }
    }

    override suspend fun insertItemInCache(item: Item) {
        return withContext(Dispatchers.IO) {
            dataBase.itemDao().insertItem(
                ItemEntity(
                    description = item.description
                )
            )
        }
    }

    override suspend fun getAllItemsFromCache(): List<Item> {
        return withContext(Dispatchers.IO) {
            val list = dataBase.itemDao().getAll()
            list.map { it.toItem() }
        }
    }

    override suspend fun removeItemFromCache(itemId: Int) {
        return withContext(Dispatchers.IO) {
            dataBase.itemDao().deleteItem(
                itemId
            )
        }
    }

    private fun fromQuery(document: QueryDocumentSnapshot): Item {
        return Item(
            documentId = document.id,
            description = (document.data[UserRepositoryImpl.KEY_ITEM_DESCRIPTION] as String)
        )
    }
}