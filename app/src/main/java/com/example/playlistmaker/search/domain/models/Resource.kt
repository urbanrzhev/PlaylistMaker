package com.example.playlistmaker.search.domain.models

sealed interface Resource<T>{
    class Success<T>(val data:T): Resource<T>
    class Error<T>(val message: Int): Resource<T>
}