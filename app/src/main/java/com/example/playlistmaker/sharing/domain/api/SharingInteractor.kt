package com.example.playlistmaker.sharing.domain.api

interface SharingInteractor {
    fun shareApp():String?
    fun openTerms()
    fun openSupport()
}