package com.example.playlistmaker.common.ui.view_model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedViewModel : ViewModel() {
    private val _likeState = MutableStateFlow<List<Pair<Int, Boolean>>>(listOf())
    internal val likeState: StateFlow<List<Pair<Int, Boolean>>> = _likeState
    private val favoriteList: MutableList<Pair<Int, Boolean>> = mutableListOf()

    internal fun setChange(trackId: Int, like: Boolean) {
        var check: Int? = null
        favoriteList.forEachIndexed { i, pair ->
            if (pair.first == trackId) {
                check = i
            }
        }
        check?.let {
            favoriteList.removeAt(it)
        }
        favoriteList.add(Pair(trackId, like))
        _likeState.value = favoriteList
    }

    internal fun clearFavoriteList() {
        favoriteList.clear()
    }
}