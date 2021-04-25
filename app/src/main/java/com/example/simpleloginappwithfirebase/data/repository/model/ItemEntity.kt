package com.example.simpleloginappwithfirebase.data.repository.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simpleloginappwithfirebase.domain.entity.itemnote.Item
import java.util.*

@Entity
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "description") val description: String

) {
    fun toItem(): Item {
        return Item(id = id, description = description)
    }

    companion object {
        fun fromItem(item: Item) : ItemEntity {
            return ItemEntity(description = item.description)
        }
    }
}