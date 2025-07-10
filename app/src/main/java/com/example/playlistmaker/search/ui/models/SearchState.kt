package com.example.playlistmaker.search.ui.models

sealed interface SearchState<T> {
    class History<T>: SearchState<T>
    class Loaded<T>: SearchState<T>
    class Empty<T>: SearchState<T>
    class Success<T>(val data:T): SearchState<T>
    class Error<T>(val message:Int): SearchState<T>
}