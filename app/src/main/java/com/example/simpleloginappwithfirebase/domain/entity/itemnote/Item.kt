package com.example.simpleloginappwithfirebase.domain.entity.itemnote

import com.google.firebase.firestore.Exclude


data class Item(
    @get:Exclude
    val id: Int? = null,
    @get:Exclude
    val documentId: String? = null,
    val description: String
)