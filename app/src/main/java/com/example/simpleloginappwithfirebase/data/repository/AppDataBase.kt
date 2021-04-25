package com.example.simpleloginappwithfirebase.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.simpleloginappwithfirebase.data.repository.model.ItemDao
import com.example.simpleloginappwithfirebase.data.repository.model.ItemEntity

@Database(entities = [ItemEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun itemDao() : ItemDao

    companion object {
        const val DATABASE_NAME = "database-sample-app"
    }
}