package com.shows.franmaric.showDetailsScreen

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shows.franmaric.MainActivity
import com.shows.franmaric.PREFS_EMAIL_KEY
import com.shows.franmaric.R
import com.shows.franmaric.database.DatabaseViewModelFactory
import com.shows.franmaric.databinding.DialogAddReviewBinding
import com.shows.franmaric.databinding.FragmentShowDetailsBinding
import com.shows.franmaric.models.Review
import com.shows.franmaric.models.ReviewRequest
import com.shows.franmaric.showsScreen.ShowsViewModel


class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null

    private val binding get() = _binding!!

    private val args: ShowDetailsFragmentArgs by navArgs()

    private val viewModel: ShowDetailsViewModel by viewModels {
        DatabaseViewModelFactory((requireActivity() as MainActivity).showsDatabase)
    }

    private var reviewsAdapter: ReviewsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDataContainers()

        initReviewRecycler()

        initSubmitButton()

        initReviewInfo()

        viewModel.initShow(args.showId)
    }

    private fun initReviewInfo() {
        viewModel.getAverageReviewRatingLiveData().observe(requireActivity()) { newRating ->
            val reviewCount = viewModel.reviewsCount()
            binding.reviewInfoTextView.text =
                getString(
                    R.string.review_info,
                    reviewCount,
                    if (reviewCount == 1) "" else "S",
                    String.format("%.2f", newRating)
                )

            binding.ratingBar.rating = newRating.toFloat()
        }
    }

    private fun initDataContainers() {
        binding.toolbar.navigationIcon?.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.collapsingToolbarLayout.setExpandedTitleColor(Color.BLACK)
        binding.collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK)

        viewModel.getShowLiveData().observe(requireActivity()) { show ->
            binding.collapsingToolbarLayout.title = show.title
            Glide.with(requireContext())
                .load(show.imageUrl)
                .into(binding.showImageView)
            binding.descriptionTextView.text = show.description
        }
    }

    private fun initSubmitButton() {
        binding.reviewButton.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())

        val bottomSheetBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.dismissButton.setOnClickListener {
            dialog.dismiss()
        }

        bottomSheetBinding.submitButton.setOnClickListener {
            val rating = bottomSheetBinding.ratingBar.rating.toInt()
            val comment = bottomSheetBinding.commentField.text.toString()

            viewModel.addReview(comment, rating)

            setRecyclerViewVisibility(true)

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setRecyclerViewVisibility(visible: Boolean) {
        // Saving processing power :)
        if (binding.reviewsRecyclerView.isVisible == visible)
            return

        binding.reviewsRecyclerView.isVisible = visible
        binding.reviewInfoTextView.isVisible = visible
        binding.ratingBar.isVisible = visible
        binding.emptyStateLabel.isVisible = !visible
    }

    private fun initReviewRecycler() {
        reviewsAdapter = ReviewsAdapter(emptyList(), requireContext())

        setRecyclerViewVisibility(reviewsAdapter?.getItemCount() != 0)

        viewModel.getReviewsLiveData().observe(requireActivity()) { reviews ->
            reviewsAdapter?.setItems(reviews)
            setRecyclerViewVisibility(reviews.size != 0)
        }

        binding.reviewsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.reviewsRecyclerView.adapter = reviewsAdapter

        binding.reviewsRecyclerView.addItemDecoration(
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