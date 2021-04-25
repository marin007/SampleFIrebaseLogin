package com.example.simpleloginappwithfirebase.data.common

import java.lang.Exception


fun isNetworkError(exception: Exception?): Boolean {
    return exception?.message.orEmpty().contains("Unable to resolve host")
}
