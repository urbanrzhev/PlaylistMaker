package com.example.playlistmaker.media_library.ui.models

sealed interface RecyclerState<T>{
    class Idle<T>:RecyclerState<T>
    class Success<T>(val data:T):RecyclerState<T>
    class Empty<T>:RecyclerState<T>
}