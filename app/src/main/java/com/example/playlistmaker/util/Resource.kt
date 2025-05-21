package com.example.playlistmaker.util

sealed class Resource<T>(val data:T?=null,val message:Int?=null) {
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(data:T? = null, message: Int?): Resource<T>(data, message)
}