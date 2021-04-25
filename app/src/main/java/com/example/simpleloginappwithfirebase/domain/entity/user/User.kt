package com.example.simpleloginappwithfirebase.domain.entity.user

import java.util.*

data class User(
    val uid: String = UUID.randomUUID().toString(),
    val email: String = ""
)