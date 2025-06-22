package com.example.playlistmaker.media_library.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.common.util.BindingActivity
import com.example.playlistmaker.databinding.ActivityMediaLibraryHostBinding
import com.example.playlistmaker.databinding.FragmentMediaLibraryPagerBinding
import com.example.playlistmaker.media_library.ui.adapters.Adapter
import com.example.playlistmaker.media_library.ui.fragments.FragmentPager
import com.google.android.material.tabs.TabLayoutMediator

//class ActivityMediaLibrary : BindingActivity<ActivityMediaLibraryHostBinding>(R.layout.activity_media_library_host) {
class ActivityMediaLibrary : BindingActivity<FragmentMediaLibraryPagerBinding>(R.layout.fragment_media_library_pager) {
    private lateinit var mediator:TabLayoutMediator
    private val adapter:Adapter by lazy {
        Adapter(supportFragmentManager,lifecycle)
    }

    override fun createBinding(inflater: LayoutInflater): FragmentMediaLibraryPagerBinding {
        return FragmentMediaLibraryPagerBinding.inflate(inflater)
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
         mediator = TabLayoutMediator(binding.tabLayout,binding.pager){tab,position->
                when(position){
                    0 -> tab.text = "Избранные треки"
                    1 -> tab.text = "Плейлисты"
                }
        }
        binding.pager.adapter = adapter
        mediator.attach()
        adapter.notifyDataSetChanged()
        /*if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.container,FragmentPager.getInstance())
                .commit()
        }*/
    }

    override fun onDestroy() {
        super.onDestroy()
        mediator.detach()
    }
}