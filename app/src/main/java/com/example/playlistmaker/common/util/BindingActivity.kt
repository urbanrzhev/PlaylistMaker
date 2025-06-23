package com.example.playlistmaker.common.util

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BindingActivity<T : ViewBinding>(
    layoutId:Int
) : AppCompatActivity(layoutId) {

    private var _binding: T? = null
    protected val binding get() = _binding!!

    abstract fun createBinding(inflater: LayoutInflater): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = createBinding(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}