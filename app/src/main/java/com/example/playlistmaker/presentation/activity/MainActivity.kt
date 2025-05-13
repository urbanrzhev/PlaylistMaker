package com.example.playlistmaker.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.Start
import com.example.playlistmaker.domain.api.StartActivityInteractor

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
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
        selectStartActivity()
        val buttonSearch = findViewById<Button>(R.id.button_search)
        buttonSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        val buttonMediaLibrary = findViewById<Button>(R.id.button_media_library)
        val listenerButtonMediaLibrary: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(this@MainActivity, MediaLibraryActivity::class.java)
                startActivity(intent)
            }
        }
        buttonMediaLibrary.setOnClickListener(listenerButtonMediaLibrary)

        val buttonSettings = findViewById<Button>(R.id.button_settings)
        buttonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun selectStartActivity(){
        /*Creator.provideStartActivityInteractor(this).getStartActivity(StartActivityInteractor.BooleanConsumer{
                if (it) {
                    val intent = Intent(this, MediaLibraryActivity::class.java)
                    startActivity(intent)
                }
            }
        )*/
        Creator.povide(this).getStartActivity( Start.BooleanConsumer{
            if (it) {
                val intent = Intent(this, MediaLibraryActivity::class.java)
                startActivity(intent)
            }
        })
    }
}