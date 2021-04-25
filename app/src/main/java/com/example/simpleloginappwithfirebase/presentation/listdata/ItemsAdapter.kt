package com.example.simpleloginappwithfirebase.presentation.listdata

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleloginappwithfirebase.R
import com.example.simpleloginappwithfirebase.domain.entity.itemnote.Item

class ItemsAdapter(private val items: MutableList<Item>) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val text: String = items[position].description
        holder.textViewDesc.text = text
    }

    override fun getItemCount(): Int = items.size

    fun removeAt(position: Int) {
        if(position < itemCount) {
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
            notifyDataSetChanged()
        }
    }

    fun getItemByPosition(position: Int): Item? {
        return if(position < itemCount) {
            items[position]
        } else {
            null
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewDesc: TextView =
            view.findViewById(R.id.textViewDescription)
    }
}