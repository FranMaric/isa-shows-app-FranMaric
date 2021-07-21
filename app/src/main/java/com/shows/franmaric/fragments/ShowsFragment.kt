package com.shows.franmaric.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.shows.franmaric.adapters.ShowsAdapter
import com.shows.franmaric.data.ShowsResources
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
