package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val buttonTextShareTheApp = findViewById<TextView>(R.id.buttonTextShareTheApp)
        buttonTextShareTheApp.setOnClickListener {
            Log.v("vasa","buttonShare")
            //val intent = Intent(Intent.ACTION_VIEW)
            val intent = Intent(Intent.ACTION_SEND)

            //intent.data = Uri.parse("https://ya.ru")
            //intent.putExtra(Intent.EXTRA_SUBJECT,"https://practicum.yandex.ru/learn/android-developer/courses/43395269-905d-4d7a-81c5-fb123dcaf2c1/sprints/389796/topics/65a33fc6-5f85-46ff-8bf6-a4bde748fa13/lessons/e6dda1bb-2cfe-4f5f-a115-4592c3a6fa21/")
                intent.putExtra(Intent.EXTRA_TEXT,"https://practicum.yandex.ru/profile/android-developer/")
                intent.setType("text/plain")
            if(intent.resolveActivity(packageManager)!= null) {
                Log.v("vasa","YES")
                startActivity(Intent.createChooser(intent, "выберите приложение"))
            }else{
                Toast.makeText(this,"Нет доступных приложений", Toast.LENGTH_SHORT).show()
            }
        }
        val buttonTextWriteToSupport = findViewById<TextView>(R.id.buttonTextWriteToSupport)
        buttonTextWriteToSupport.setOnClickListener {
            Log.v("vasa","buttonWrite")
            val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(getString(R.string.uri_mailto))
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_mail_adress)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.my_mail_thema))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.my_mail_message))
            }
            startActivity(intent)
        }
        val buttonUserAgreement = findViewById<TextView>(R.id.buttonUserAgreement)
        buttonUserAgreement.setOnClickListener{
            Log.v("vasa","buttoUser")
            val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(getString(R.string.practicum_offer))
            }
            startActivity(intent)
        }

        val buttonSettingsBack = findViewById<ImageView>(R.id.settings_back)
        buttonSettingsBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}