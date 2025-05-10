package com.example.playlistmaker.domain.api

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

interface AdapterRefresh {
    fun refresh(/*a:Adapter<RecyclerView.ViewHolder>*/)
}