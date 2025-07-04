package com.example.playlistmaker.settings.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.common.util.BindingActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : BindingActivity<ActivitySettingsBinding>() {
    private val viewModel: SettingsViewModel by viewModel()

    override fun createBinding(inflater: LayoutInflater): ActivitySettingsBinding {
        return ActivitySettingsBinding.inflate(inflater)
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
        installThemeSwitcher(binding.themeSwitcher)
        viewModel.observeErrors().observe(this) {
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
        binding.toolbarBack.setNavigationOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun installThemeSwitcher(themeSwitcher: SwitchMaterial) {
        themeSwitcher.isChecked = viewModel.getTheme()
    }

    private fun showMessage(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }
}
