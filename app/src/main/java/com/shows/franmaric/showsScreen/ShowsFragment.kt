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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shows.franmaric.*
import com.shows.franmaric.repository.RepositoryViewModelFactory
import com.shows.franmaric.databinding.BottomSheetProfileBinding
import com.shows.franmaric.databinding.FragmentShowsBinding
import com.shows.franmaric.extensions.hasInternetConnection
import com.shows.franmaric.utils.FileUtil
import com.shows.franmaric.utils.preparePermissionsContract

private const val SPAN_COUNT = 2

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null

    private val binding get() = _binding!!

    private val viewModel: ShowsViewModel by viewModels {
        RepositoryViewModelFactory((requireActivity() as MainActivity).showsRepository)
    }

    private val cameraPermissionForTakingPhoto = preparePermissionsContract(onPermissionsGranted = {
        takePhoto()
    })

    private var showsAdapter: ShowsAdapter? = null

    private var bottomSheetBinding: BottomSheetProfileBinding? = null

    private val getCameraImage =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                val file = FileUtil.getImageFile(requireContext())

                if (file != null) {
                    val avatarUri = FileProvider.getUriForFile(
                        requireContext(),
                        activity?.applicationContext?.packageName.toString() + ".fileprovider",
                        file
                    )
                    viewModel.uploadProfilePhoto(
                        file.toString(),
                        avatarUri.toString(),
                        activity?.getPreferences(Context.MODE_PRIVATE) ?: return@registerForActivityResult,
                        requireContext().hasInternetConnection()) {
                        updateProfileAndBottomSheetPhoto()
                    }
                }
            }
        }

    private var isLinear = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initShowsRecycler()

        initProfileButton()

        initTopRatedChip()

        checkForOfflinePhotoToUpload()

        initFAB()
    }

    private fun initFAB() {
        binding.floatingActionButton.setOnClickListener {
            isLinear = !isLinear
            setLayoutManager(isLinear)
        }
    }
    private fun setLayoutManager(isLinear: Boolean) {
        binding.showsRecyclerView.layoutManager =
            if(isLinear) LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            else GridLayoutManager(context, SPAN_COUNT)

        binding.floatingActionButton.setImageResource(if(isLinear) R.drawable.ic_grid else R.drawable.ic_list)
    }

    private fun setState(state: State) {
        binding.showsRecyclerView.isVisible = false
        binding.emptyStateLabel.isVisible = false
        binding.loadingIndicator.isVisible = false

        when(state) {
            State.EMPTY -> {
                binding.emptyStateLabel.isVisible = true
            }
            State.DATA -> {
                binding.showsRecyclerView.isVisible = true
            }
            State.LOADING -> {
                binding.loadingIndicator.isVisible = true
            }
        }
    }

    private fun checkForOfflinePhotoToUpload() {
        val file = FileUtil.getImageFile(requireContext())

        if (file != null) {
            val avatarUri = FileProvider.getUriForFile(
                requireContext(),
                activity?.applicationContext?.packageName.toString() + ".fileprovider",
                file
            )
            viewModel.checkForOfflinePhotoToUpload(
                file.toString(),
                avatarUri.toString(),
                activity?.getPreferences(Context.MODE_PRIVATE) ?: return,
                requireContext().hasInternetConnection())
        }
    }

    private fun initTopRatedChip() {
        binding.topRatedChip.setOnCheckedChangeListener {_, isTopRated ->
            setState(State.LOADING)
            viewModel.getShows(requireContext().hasInternetConnection() ,isTopRated)
        }
    }

    private fun initProfileButton() {
        binding.profileButton.setOnClickListener {
            showProfileBottomSheet()
        }

        updateProfileAndBottomSheetPhoto()
    }

    private fun showProfileBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())

        bottomSheetBinding = BottomSheetProfileBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding!!.root)

        bottomSheetBinding!!.logoutButton.setOnClickListener {
            showAlertDialog {
                viewModel.logout(activity?.getPreferences(Context.MODE_PRIVATE) ?: return@showAlertDialog)

                val action = ShowsFragmentDirections.actionShowsToLogin()
                findNavController().navigate(action)

                dialog.dismiss()
            }
        }

        bottomSheetBinding!!.changeProfilePhotoButton.setOnClickListener {
            changeProfilePhoto()
        }

        updateProfileAndBottomSheetPhoto()

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        bottomSheetBinding!!.mailTextView.text =
            sharedPref.getString(PREFS_EMAIL_KEY, "imenko.prezimenovic@infinum.com")

        dialog.show()
    }

    private fun changeProfilePhoto() {
        cameraPermissionForTakingPhoto.launch(arrayOf(Manifest.permission.CAMERA))
    }

    @SuppressLint("MissingPermission")
    private fun takePhoto() {
        val file =
            FileUtil.getImageFile(requireContext()) ?: FileUtil.createImageFile(requireContext())

        val avatarUri = FileProvider.getUriForFile(
            requireContext(),
            activity?.applicationContext?.packageName.toString() + ".fileprovider",
            file!!
        )

        getCameraImage.launch(avatarUri)
    }

    private fun updateProfileAndBottomSheetPhoto() {
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val avatarUri = prefs.getString(PREFS_PROFILE_PHOTO_URL, "")
        if(avatarUri == "") return

        if (bottomSheetBinding != null) {
            Glide.with(requireContext())
                .load(avatarUri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(bottomSheetBinding!!.profileImageView)
        }

        Glide.with(requireContext())
            .load(avatarUri)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.profileButton)

    }

    private fun showAlertDialog(onPositiveCallback: () -> Unit) {
        val alertDialog = AlertDialog.Builder(requireContext())

        alertDialog.setTitle(getString(R.string.logout))
        alertDialog.setMessage(getString(R.string.are_you_sure_you_want_to_logout))

        alertDialog.setPositiveButton(getString(R.string.logout), { _, _ ->
            onPositiveCallback()
        })

        alertDialog.setNegativeButton(getString(R.string.back), { _, _ ->

        })

        alertDialog.show()
    }

    private fun initShowsRecycler() {
        showsAdapter = ShowsAdapter(emptyList()) { show ->
            val action = ShowsFragmentDirections.actionShowsToShowDetails(
                show.id
            )
            findNavController().navigate(action)
        }

        viewModel.getShowsLiveData().observe(requireActivity()) { shows ->
            showsAdapter?.setItems(shows)

            if(showsAdapter?.getItemCount() != 0)
                setState(State.DATA)
            else
                setState(State.EMPTY)
        }

        setState(State.LOADING)
        viewModel.getShows(requireContext().hasInternetConnection(), isTopRated = false)

        setLayoutManager(isLinear = true)
        binding.showsRecyclerView.adapter = showsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
