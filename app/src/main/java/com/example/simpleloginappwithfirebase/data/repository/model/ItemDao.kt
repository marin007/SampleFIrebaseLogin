package com.example.simpleloginappwithfirebase.data.repository.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM itementity")
    fun getAll(): Flow<List<ItemEntity>>

    @Insert
    fun insertItem(vararg item: ItemEntity)

    @Query("DELETE FROM itementity WHERE id = :itemId")
    fun deleteItem(itemId: Int)
}