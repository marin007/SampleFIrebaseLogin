package com.example.simpleloginappwithfirebase.data.repository

import com.example.simpleloginappwithfirebase.data.common.isNetworkError
import com.example.simpleloginappwithfirebase.data.repository.model.ItemEntity
import com.example.simpleloginappwithfirebase.domain.entity.itemnote.Item
import com.example.simpleloginappwithfirebase.domain.repostiory.ItemRepository
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

class ItemRepositoryImpl(
    private val dataBase: AppDataBase
) : ItemRepository {

    private val fireStore = Firebase.firestore

    @ExperimentalCoroutinesApi
    override fun getAllItemsFromRemote(): Flow<List<Item>> = callbackFlow {

        // Reference to use in Firestore
        var eventsCollection: CollectionReference? = null
        try {
            eventsCollection = fireStore
                .collection(UserRepositoryImpl.ITEMS_COLLECTION_NAME)
        } catch (e: Throwable) {
            // If Firebase cannot be initialized, close the stream of data
            // flow consumers will stop collecting and the coroutine will resume
            close(e)
        }

        // Registers callback to firestore, which will be called on new events
        val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->
            if (snapshot == null) {
                return@addSnapshotListener
            }
            // Sends events to the flow! Consumers will get the new events
            try {
                if (!snapshot.isEmpty) {
                    val items = arrayListOf<Item>()

                    for (document in snapshot.documents) {
                        items.add(fromQuery(document))
                    }
                    offer(items)
                }

            } catch (e: Throwable) {
                // Event couldn't be sent to the flow
            }
        }
        awaitClose { subscription?.remove() }
    }

    override fun addItemToRemote(item: Item, onAddItemListener: ItemRepository.OnAddItemListener) {
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

    override fun deleteItemFromRemote(
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

    override suspend fun insertItemInDb(item: Item) = withContext(Dispatchers.IO) {
        dataBase.itemDao().insertItem(
            ItemEntity(
                description = item.description
            )
        )
    }

    override fun getAllItemsFromDb(): Flow<List<ItemEntity>> {
        return dataBase.itemDao().getAll()
    }

    override suspend fun deleteItemFromDb(itemId: Int) = withContext(Dispatchers.IO) {
        dataBase.itemDao().deleteItem(
            itemId
        )
    }

    private fun fromQuery(document: DocumentSnapshot): Item {
        return Item(
            documentId = document.id,
            description = (document.data?.get(UserRepositoryImpl.KEY_ITEM_DESCRIPTION) as String)
        )
    }
}