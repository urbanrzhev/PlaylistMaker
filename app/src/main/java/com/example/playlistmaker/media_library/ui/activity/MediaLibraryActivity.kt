package com.example.playlistmaker.media_library.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.common.util.BindingActivity
import com.example.playlistmaker.databinding.ActivityMediaLibraryPagerBinding
import com.example.playlistmaker.media_library.ui.adapters.MediaLibraryAdapter
import com.example.playlistmaker.media_library.ui.view_model.MediaLibraryViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaLibraryActivity :
    BindingActivity<ActivityMediaLibraryPagerBinding>(R.layout.activity_media_library_pager) {
    private val viewModel: MediaLibraryViewModel by viewModel()
    private val mediator: TabLayoutMediator by lazy {
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = "Избранные треки"
                1 -> tab.text = "Плейлисты"
            }
        }
    }
    private val adapter: MediaLibraryAdapter by lazy {
        MediaLibraryAdapter(supportFragmentManager, lifecycle)
    }

    override fun createBinding(inflater: LayoutInflater): ActivityMediaLibraryPagerBinding {
        return ActivityMediaLibraryPagerBinding.inflate(inflater)
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
        binding.toolbarMediaLibrary.setNavigationOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
        viewModel.observeStartFragment().observe(this) {
            binding.pager.setCurrentItem(it)
        }
        binding.pager.adapter = adapter
        viewModel.getStartFragment()
        mediator.attach()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStartActivity()
    }

    override fun onStop() {
        super.onStop()
        viewModel.setStartFragment(binding.pager.currentItem)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediator.detach()
    }
}