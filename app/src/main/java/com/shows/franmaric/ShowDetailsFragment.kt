package com.shows.franmaric

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shows.franmaric.adapters.ReviewsAdapter
import com.shows.franmaric.databinding.DialogAddReviewBinding
import com.shows.franmaric.databinding.FragmentShowDetailsBinding
import com.shows.franmaric.models.Review
import com.shows.franmaric.models.Show


class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var reviewsAdapter: ReviewsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO: get show from fragment args
        //val show = ShowsResources.shows[intent.getIntExtra(SHOW_INDEX, 0)]
        //initDataContainers(show)

        //TODO: FIX supportActionBar
        //setSupportActionBar(binding.toolbar)


        initReviewRecycler()

        initSubmitButton()
    }

    private fun initDataContainers(show: Show) {
        binding.collapsingToolbarLayout.title = show.name
        binding.toolbar.navigationIcon?.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        binding.toolbar.setNavigationOnClickListener {
            //TODO: pop from stack
            //this.finish()
        }
        binding.collapsingToolbarLayout.setExpandedTitleColor(Color.BLACK)
        binding.collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK)
        binding.showImageView.setImageResource(show.imageResourceId)
        binding.descriptionTextView.text = show.description
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

            val review = Review(rating, comment)
            reviewsAdapter?.addItem(review)

            var newRating = 0.0
            reviewsAdapter?.getItems()?.forEach { review ->
                newRating += review.rating
            }

            var reviewCount = reviewsAdapter?.getItemCount()!!
            newRating = newRating / reviewCount

            binding.reviewInfoTextView.text =
                "${reviewCount} REVIEW${if (reviewCount == 1) "" else "S"}, ${newRating} AVERAGE"

            binding.ratingBar.rating = newRating.toFloat()

            setRecyclerViewVisibility(true)

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setRecyclerViewVisibility(visible: Boolean) {
        // Saving processing power :)
        if (binding.reviewsRecyclerView.isVisible == visible)
            return

        if (visible) {
            binding.reviewsRecyclerView.isVisible = true
            binding.reviewInfoTextView.isVisible = true
            binding.ratingBar.isVisible = true
            binding.emptyStateLabel.isVisible = false
        } else {
            binding.reviewsRecyclerView.isVisible = false
            binding.reviewInfoTextView.isVisible = false
            binding.ratingBar.isVisible = false
            binding.emptyStateLabel.isVisible = true
        }
    }

    private fun initReviewRecycler() {
        reviewsAdapter = ReviewsAdapter(emptyList())

        if (reviewsAdapter?.getItemCount() == 0) {
            setRecyclerViewVisibility(false)
        } else {
            setRecyclerViewVisibility(true)
        }

        binding.reviewsRecyclerView.layoutManager = LinearLayoutManager(context)
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