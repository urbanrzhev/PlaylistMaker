package com.example.playlistmaker.settings.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.common.util.BindingFragment
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BindingFragment<FragmentSettingsBinding>() {
    private val viewModel: SettingsViewModel by viewModel()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        installThemeSwitcher(binding.themeSwitcher)
        viewModel.observeErrors().observe(viewLifecycleOwner) {
            if (it != null)
                showMessage(it)
        }
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.setTheme(checked)
        }
        binding.buttonTextShareTheApp.setOnClickListener {
            viewModel.share()
        }
        binding.buttonTextWriteToSupport.setOnClickListener {
            viewModel.email()
        }
        binding.buttonUserAgreement.setOnClickListener {
            viewModel.terms()
        }
    }
    private fun installThemeSwitcher(themeSwitcher: SwitchMaterial) {
        themeSwitcher.isChecked = viewModel.getTheme()
    }

    private fun showMessage(string: String) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }
}