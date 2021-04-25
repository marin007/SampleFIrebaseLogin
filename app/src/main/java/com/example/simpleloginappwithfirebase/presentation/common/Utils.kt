package com.example.simpleloginappwithfirebase.presentation.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.simpleloginappwithfirebase.R
import com.google.android.material.textfield.TextInputEditText

//https://www.rockandnull.com/livedata-observe-once/
class ValueWrapper<T>(private val value: T) {

    private var isConsumed = false

    fun get(): T? =
        if (isConsumed) {
            null
        } else {
            isConsumed = true
            value
        }
}

fun showErrorToast(context: Context, message: String) {
    val toast: Toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
    val view = toast.view
    view?.background?.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN)
    val text = view?.findViewById<TextView>(android.R.id.message)
    text?.setTextColor(Color.WHITE)
    toast.show()
}

fun showSuccessToast(context: Context, message: String) {
    val toast: Toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
    val view = toast.view
    view?.background?.setColorFilter(-0xff6699, PorterDuff.Mode.SRC_IN)
    val text = view?.findViewById<TextView>(android.R.id.message)
    text?.setTextColor(Color.WHITE)
    toast.show()
}

fun showInfoDialog(context: Context, action: (text: String) -> Unit = {}) {
    val dialog = Dialog(context)
    dialog.setContentView(R.layout.item_dialog)

    val window = dialog.window
    window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    val input = dialog.findViewById<View>(R.id.item_input) as TextInputEditText

    val inputCount = dialog.findViewById<View>(R.id.charactersLeft) as TextView

    val saveButton = dialog.findViewById<View>(R.id.save_button) as Button

    input.doOnTextChanged { text, _, _, _ ->
        inputCount.text = text?.length.toString()
    }

    saveButton.setOnClickListener {
        if(!input.text.isNullOrEmpty()) {
            dialog.hide()
            action(input.text.toString())
        }
    }
    dialog.show()
}

fun getRecycleViewDivider(context: Context): DividerItemDecoration {
    val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    ContextCompat.getDrawable(context, R.drawable.row_separator)?.let {
        divider.setDrawable(it)
    }
    return divider
}

fun isEmailValid(email: String): Boolean {
    if (email.length < 6) return false
    if (!email.contains('@')) return false
    if (!email.contains('.')) return false
    return true
}

fun isPasswordValid(password: String): Boolean {
    return password.length in 6..12
}