package com.example.playlistmaker.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.playlistmaker.R

class NotCallViewHolder(
    val parent: ViewGroup,
    val text: String
) : ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.load_error_for_search, parent, false)
) {
    init {
        val textNotFound = itemView.findViewById<TextView>(R.id.textNotFound)
        textNotFound.text = text
    }
}