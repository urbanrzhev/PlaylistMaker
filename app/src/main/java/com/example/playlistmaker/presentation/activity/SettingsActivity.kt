package com.example.playlistmaker.presentation.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.App
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.SharedPreferencesInteractor
import com.example.playlistmaker.domain.util.MyConstants
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.setPadding(
                systemBars.left,
                systemBars.top + ime.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
        val buttonTextShareTheApp = findViewById<TextView>(R.id.buttonTextShareTheApp)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        installThemeSwitcher(themeSwitcher)
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }
        buttonTextShareTheApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.practicum_android_link))
            intent.setType("text/plain")
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(Intent.createChooser(intent, getString(R.string.share_one)))
            } else {
                Toast.makeText(this, getString(R.string.share_no), Toast.LENGTH_SHORT).show()
            }
        }
        val buttonTextWriteToSupport = findViewById<TextView>(R.id.buttonTextWriteToSupport)
        buttonTextWriteToSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse(getString(R.string.uri_mailto))
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_mail_adress)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.my_mail_thema))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.my_mail_message))
            }
            startActivity(intent)
        }
        val buttonUserAgreement = findViewById<TextView>(R.id.buttonUserAgreement)
        buttonUserAgreement.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.practicum_offer))
            }
            startActivity(intent)
        }
        val buttonSettingsBack = findViewById<MaterialToolbar>(R.id.toolbar_settings)
        buttonSettingsBack.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun installThemeSwitcher(themeSwitcher:SwitchMaterial){
        Creator.provideSharedPreferencesInteractor(this).getAppDarkTheme(
                SharedPreferencesInteractor.BooleanConsumer {
                    themeSwitcher.isChecked = it
                }
        )
    }
}