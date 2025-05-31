package com.example.playlistmaker.sharing.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigator(
    private val appContext:Context
){
    @SuppressLint("QueryPermissionsNeeded")
    fun shareLink(value:String): String? {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT,  value)
            setType("text/plain")
        }
        if (intent.resolveActivity(appContext.packageManager) != null) {
            appContext.startActivity(Intent.createChooser(intent, appContext.getString(R.string.share_one)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            return null
        }else{
            return appContext.getString(R.string.share_no)
        }
    }
    fun openEmail(emailData:EmailData){
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(emailData.uriMailto)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.mailAddress))
            putExtra(Intent.EXTRA_SUBJECT, emailData.mailTheme)
            putExtra(Intent.EXTRA_TEXT, emailData.mailMessage)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        appContext.startActivity(intent)
    }
    fun openTerms(value:String){
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(value)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        appContext.startActivity(intent)
    }
}