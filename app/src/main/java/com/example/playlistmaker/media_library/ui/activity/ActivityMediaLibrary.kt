package com.example.playlistmaker.media_library.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.example.playlistmaker.R
import com.example.playlistmaker.common.util.BindingActivity
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.playlistmaker.media_library.ui.adapters.MediaLibraryAdapter
import com.example.playlistmaker.media_library.ui.fragments.FragmentContainer
import com.example.playlistmaker.media_library.ui.view_model.MediaLibraryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityMediaLibrary : BindingActivity<ActivityMediaLibraryBinding>(R.layout.activity_media_library) {
    private val viewModel:MediaLibraryViewModel by viewModel()
    override fun createBinding(inflater: LayoutInflater): ActivityMediaLibraryBinding {
        return ActivityMediaLibraryBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.container,FragmentContainer.getInstance())
                .commit()
        }
    }

    //override fun getLifeCycle() = lifecycle
}