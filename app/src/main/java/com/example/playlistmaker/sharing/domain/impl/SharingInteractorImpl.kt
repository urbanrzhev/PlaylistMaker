package com.example.playlistmaker.sharing.domain.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val context: Context,
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp(): String? {
        return externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openTerms(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.practicum_android_link)
    }

    private fun getSupportEmailData(): EmailData = EmailData(
        uriMailto = context.getString(R.string.uri_mailto),
        mailAddress = context.getString(R.string.my_mail_adress),
        mailTheme = context.getString(R.string.my_mail_thema),
        mailMessage = context.getString(R.string.my_mail_message)
    )

    private fun getTermsLink(): String {
        return context.getString(R.string.practicum_offer)
    }
}