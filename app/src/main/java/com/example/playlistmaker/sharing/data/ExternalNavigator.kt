package com.example.playlistmaker.sharing.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigator(
    private val appContext: Context,
    private val intent: Intent
) {
    @SuppressLint("QueryPermissionsNeeded")
    fun shareLink(value: String): String? {
        intent.apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, value)
            setType("text/plain")
        }
        if (intent.resolveActivity(appContext.packageManager) != null) {
            appContext.startActivity(
                Intent.createChooser(
                    intent,
                    appContext.getString(R.string.sharing_select_an_application)
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            return null
        } else {
            return appContext.getString(R.string.sharing_there_are_no_apps)
        }
    }

    fun openEmail(emailData: EmailData):String? {
        intent.apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse(emailData.uriMailto)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.mailAddress))
            putExtra(Intent.EXTRA_SUBJECT, emailData.mailTheme)
            putExtra(Intent.EXTRA_TEXT, emailData.mailMessage)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return startApp(intent)
    }

    fun openTerms(value: String):String? {
        intent.apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(value)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return startApp(intent)
    }

    private fun startApp(intent:Intent):String?{
        try{
            appContext.startActivity(intent)
        }catch (e:Exception){
            return appContext.getString(R.string.sharing_there_are_no_apps)
        }
        return null
    }
}