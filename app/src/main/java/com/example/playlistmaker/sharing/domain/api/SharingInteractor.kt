package com.example.playlistmaker.sharing.domain.api

interface SharingInteractor {
    fun shareApp():String?
    fun openTerms():String?
    fun openEmail():String?
}