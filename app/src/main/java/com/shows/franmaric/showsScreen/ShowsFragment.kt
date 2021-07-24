package com.shows.franmaric.showsScreen

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shows.franmaric.R
import com.shows.franmaric.data.ShowsResources
import com.shows.franmaric.databinding.BottomSheetProfileBinding
import com.shows.franmaric.databinding.FragmentShowsBinding
import com.shows.franmaric.utils.FileUtil
import com.shows.franmaric.utils.preparePrmissionsContract


class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null

    private val binding get() = _binding!!

    private val viewModel: ShowsViewModel by viewModels()

    private val cameraPermissionForTakingPhoto = preparePrmissionsContract(onPermissionsGranted = {
        takePhoto()
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var showsAdapter: ShowsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initShowsRecycler()

        initProfileButton()
    }

    private fun initProfileButton() {
        binding.profileButton.setOnClickListener {
            showProfileBottomSheet()
        }
    }

    private fun showProfileBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())

        val bottomSheetBinding = BottomSheetProfileBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.logoutButton.setOnClickListener {
            showAlertDialog {
                logout()

                val action = ShowsFragmentDirections.actionShowsToLogin()
                findNavController().navigate(action)

                dialog.dismiss()
            }
        }

        bottomSheetBinding.changeProfilePhotoButton.setOnClickListener {
            changeProfilePhoto()

            val file = FileUtil.createImageFile(requireContext())
            val avatarUri = FileProvider.getUriForFile(requireContext(), activity?.applicationContext?.packageName.toString() + ".fileprovider", file!!)

            Glide.with(requireContext())
                .load(avatarUri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(bottomSheetBinding.profileImageView)

            Glide.with(requireContext())
                .load(avatarUri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.profileButton)


        }

        val file = FileUtil.createImageFile(requireContext())
        val avatarUri = FileProvider.getUriForFile(
            requireContext(),
            activity?.applicationContext?.packageName.toString() + ".fileprovider",
            file!!
        )

        Glide.with(requireContext())
            .load(avatarUri)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(bottomSheetBinding.profileImageView)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        bottomSheetBinding.mailTextView.text =
            sharedPref.getString(getString(R.string.prefs_email), "imenko.prezimenovic@infinum.com")

        dialog.show()
    }

    private fun changeProfilePhoto() {
        cameraPermissionForTakingPhoto.launch(arrayOf(Manifest.permission.CAMERA))
    }

    @SuppressLint("MissingPermission")
    private fun takePhoto() {
        val file = FileUtil.createImageFile(requireContext())
        val avatarUri = FileProvider.getUriForFile(
            requireContext(),
            activity?.applicationContext?.packageName.toString() + ".fileprovider",
            file!!
        )

        val cameraContract =
            ActivityResultContracts.TakePicture().createIntent(requireContext(), avatarUri)
        startActivity(cameraContract)
    }

    private fun logout() {
        val sharedPref =
            activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            remove(getString(R.string.prefs_email))
            putBoolean(getString(R.string.prefs_remember_me), false)
            commit()
        }
    }

    private fun showAlertDialog(onPositiveCallback: () -> Unit) {
        val alertDialog = AlertDialog.Builder(requireContext())

        alertDialog.setTitle("LOGOUT")
        alertDialog.setMessage("Are you sure you want to logout?")

        alertDialog.setPositiveButton("LOGOUT", { _, _ ->
            onPositiveCallback()
        })

        alertDialog.setNegativeButton("BACK", { _, _ ->

        })

        alertDialog.show()
    }

    private fun initShowsRecycler() {
        showsAdapter = ShowsAdapter(emptyList(),requireContext()) { show ->
            val showIndex = ShowsResources.shows.indexOf(show)
            val action = ShowsFragmentDirections.actionShowsToShowDetails(
                showIndex
            )
            findNavController().navigate(action)
        }

        viewModel.getShowsLiveData().observe(requireActivity()) { shows ->
            showsAdapter?.setItems(shows)
            binding.showsRecyclerView.isVisible = showsAdapter?.getItemCount() != 0
        }

        viewModel.initShows()

        binding.showsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.showsRecyclerView.adapter = showsAdapter

        binding.showsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
