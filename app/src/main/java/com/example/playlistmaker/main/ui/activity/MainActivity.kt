package com.example.playlistmaker.main.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.common.util.BindingActivity
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.main.ui.view_model.MainViewModel
import com.example.playlistmaker.media_library.ui.activity.MediaLibraryActivity
import com.example.playlistmaker.search.ui.activity.SearchActivity
import com.example.playlistmaker.settings.ui.activity.SettingsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BindingActivity<ActivityMainBinding>() {
    private lateinit var viewBinding: ActivityMain2Binding
    private val viewModel: MainViewModel by viewModel()
    override fun createBinding(inflater: LayoutInflater): ActivityMain2Binding {
        return ActivityMain2Binding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.main) { v, insets ->
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
        viewModel.initActivity()
        viewBinding.buttonSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            }
            startActivity(intent)
        }
        val listenerButtonMediaLibrary: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(this@MainActivity, MediaLibraryActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                }
                startActivity(intent)
            }
        }
        viewBinding.buttonAudioPlayer.setOnClickListener(listenerButtonMediaLibrary)
        viewBinding.buttonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        setStartActivity()
    }

    private fun setStartActivity() {
        viewModel.setStartActivity()
    }
}