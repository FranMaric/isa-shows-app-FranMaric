package com.shows.franmaric.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shows.franmaric.R
import com.shows.franmaric.adapters.ShowsAdapter
import com.shows.franmaric.data.ShowsResources
import com.shows.franmaric.databinding.BottomSheetProfileBinding
import com.shows.franmaric.databinding.FragmentShowsBinding


class ShowsFragment : Fragment()  {

    private var _binding: FragmentShowsBinding? = null

    private val binding get() = _binding!!

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
        binding.profileButton.setOnClickListener{
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

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        bottomSheetBinding.mailTextView.text = sharedPref.getString(getString(R.string.prefs_email),"imenko.prezimenovic@infinum.com")

        dialog.show()
    }

    private fun logout() {
        val sharedPref =
            activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            remove(getString(R.string.prefs_email))
            putBoolean(getString(R.string.prefs_remember_me), false)
            apply()
        }
    }

    private fun showAlertDialog(onPositiveCallback: () -> Unit) {
        val alertDialog = AlertDialog.Builder(requireContext())

        alertDialog.setTitle("LOGOUT")
        alertDialog.setMessage("Are you sure you want to logout?")

        alertDialog.setPositiveButton("LOGOUT",{_,_->
            onPositiveCallback()
        })

        alertDialog.setNegativeButton("BACK",{_,_->

        })

        alertDialog.show()
    }

    private fun initShowsRecycler() {
        showsAdapter = ShowsAdapter(ShowsResources.shows) { show ->
            val showIndex = ShowsResources.shows.indexOf(show)
            val action = ShowsFragmentDirections.actionShowsToShowDetails(showIndex)
            findNavController().navigate(action)
        }

        if(showsAdapter?.getItemCount() == 0){
            binding.showsRecyclerView.visibility = GONE
        }

        binding.showsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.showsRecyclerView.adapter = showsAdapter

        binding.showsRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
