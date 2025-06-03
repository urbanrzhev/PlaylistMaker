package com.example.playlistmaker.search.domain.models

sealed interface State<T> {
    class Else<T>: State<T>
    class Empty<T>: State<T>
    class Success<T>(val data:T): State<T>
    class Error<T>(val message:Int): State<T>
}