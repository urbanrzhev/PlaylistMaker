package com.example.playlistmaker.new_playlist.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Playlist
import com.example.playlistmaker.common.util.BindingFragment
import com.example.playlistmaker.common.util.MyDisplayMetrics
import com.example.playlistmaker.databinding.FragmentCreateAndModifyPlaylistBinding
import com.example.playlistmaker.new_playlist.ui.view_model.NewPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

open class NewPlaylistFragment : BindingFragment<FragmentCreateAndModifyPlaylistBinding>() {
    internal open val viewModel by viewModel<NewPlaylistViewModel>()
    private var textWatcherName: TextWatcher? = null
    private var textWatcherDescription: TextWatcher? = null
    private lateinit var confirmDialog: MaterialAlertDialogBuilder
    private val backPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            confirmDialog.show()
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreateAndModifyPlaylistBinding {
        attachBackPressedDispatcher()
        return FragmentCreateAndModifyPlaylistBinding.inflate(inflater, container, false)
    }

    open fun attachBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.alert_dialog))
            .setMessage(getString(R.string.alert_dialog_message))
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.destroy)) { _, _ ->
                backPressedCallback.isEnabled = false
                navigateUp()
            }
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.imageCover.setImageURI(uri)
                    viewModel.saveImageToPrivateStorage(uri)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.photo_error),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        viewModel.observePhoto.observe(viewLifecycleOwner) { uri ->
            if (uri.isNotEmpty())
                showPhoto(uri.toUri())
        }
        viewModel.observeButtonCreateEnable.observe(viewLifecycleOwner) { enable ->
            binding.buttonCreate.isEnabled = enable
        }
        viewModel.observeEnablePressedCallback.observe(viewLifecycleOwner) { enable ->
            backPressedCallback.isEnabled = enable
        }
        viewModel.observeCloseFragment.observe(viewLifecycleOwner) { close ->
            if (close)
                navigateUp()
        }
        binding.vectorBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.imageCover.setOnClickListener {
            try {
                pickMedia.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.photo_picker_error),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        binding.buttonCreate.setOnClickListener {
            viewModel.createPlaylist()
        }
        textWatcherName = binding.editTextName.doOnTextChanged { text, _, _, _ ->
            viewModel.setName(text.toString())
        }
        textWatcherDescription = binding.editTextDescription.doOnTextChanged { text, _, _, _ ->
            viewModel.setDescription(text.toString())
        }
        changeConfiguration(viewModel.getPlaylist())
    }

    private fun navigateUp() {
        findNavController().navigateUp()
    }

    private fun changeConfiguration(playlist: Playlist) {
        if (playlist.name.isNotEmpty())
            binding.editTextName.setText(playlist.name)
        if (playlist.description.isNotEmpty())
            binding.editTextName.setText(playlist.description)
        if (playlist.photo.isNotEmpty()) {
            showPhoto(playlist.photo.toUri())
        }
    }

    private fun showPhoto(uri: Uri) {
        Glide.with(requireContext())
            .load(uri)
            .transform(RoundedCorners(MyDisplayMetrics().dpToPx(16f, requireContext())))
            .into(binding.imageCover)
        binding.imageFon.isVisible = false
        binding.imageCover.background = null
    }

    override fun onDestroyView() {
        textWatcherName?.let { binding.editTextName.removeTextChangedListener(it) }
        textWatcherDescription?.let { binding.editTextDescription.removeTextChangedListener(it) }
        super.onDestroyView()
    }
}