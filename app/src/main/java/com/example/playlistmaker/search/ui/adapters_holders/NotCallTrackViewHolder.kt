package com.example.playlistmaker.search.ui.adapters_holders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.playlistmaker.R

class NotCallViewHolder(
    private val parent: ViewGroup,
    private val text:String
) : ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.load_error_for_search, parent, false)
) {
    init {
        val textNotFound = itemView.findViewById<TextView>(R.id.textNotFound)
        textNotFound.text = text
    }
}