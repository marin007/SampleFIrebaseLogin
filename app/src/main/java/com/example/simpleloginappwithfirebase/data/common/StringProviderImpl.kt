package com.example.simpleloginappwithfirebase.data.common

import android.content.Context
import com.example.simpleloginappwithfirebase.domain.common.StringProvider

class StringProviderImpl(private val context: Context): StringProvider {

    override fun getString(id: Int): String {
        return context.getString(id)
    }
}