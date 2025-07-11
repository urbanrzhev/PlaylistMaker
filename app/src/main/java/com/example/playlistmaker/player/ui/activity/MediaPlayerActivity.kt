package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.common.util.BindingActivity
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.player.ui.view_model.MediaPlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaPlayerActivity : BindingActivity<ActivityAudioPlayerBinding>() {
    private val viewModel: MediaPlayerViewModel by viewModel()

    override fun createBinding(inflater: LayoutInflater): ActivityAudioPlayerBinding {
        return ActivityAudioPlayerBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
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
        viewModel.observeTimeProgressLiveData().observe(this) { time ->
            binding.textProgress.text = time
        }
        viewModel.observeBackgroundPlayButton().observe(this) { background ->
            binding.buttonPause.setBackgroundResource(background)
        }
        viewModel.observeEnablePlayButton().observe(this) { enabled ->
            binding.buttonPause.isEnabled = enabled
        }
        binding.buttonPause.setOnClickListener {
            viewModel.control()
        }
        binding.vectorBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
        loadTrack(viewModel.getActiveTrack())
    }

    override fun onPause() {
        super.onPause()
        binding.buttonPause.setBackgroundResource(R.drawable.play_play)
        viewModel.pause()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStartActivity()
    }

    private fun loadTrack(model: Track) {
        ShowActiveTrack(this, binding, model)
            .show()
    }
}