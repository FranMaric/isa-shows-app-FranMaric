package com.shows.franmaric

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shows.franmaric.adapters.ReviewsAdapter
import com.shows.franmaric.data.ShowsResources
import com.shows.franmaric.databinding.ActivityShowDetailsBinding
import com.shows.franmaric.databinding.DialogAddReviewBinding
import com.shows.franmaric.models.Review
import com.shows.franmaric.models.Show


class ShowDetailsActivity : AppCompatActivity() {
    companion object {
        private const val SHOW_INDEX = "SHOW_INDEX"

        fun buildIntent(originActivity: Activity,showIndex: Int): Intent {
            val intent = Intent(originActivity, ShowDetailsActivity::class.java)
            intent.putExtra(SHOW_INDEX, showIndex)

            return intent
        }
    }

    private lateinit var binding: ActivityShowDetailsBinding
    private var reviewsAdapter: ReviewsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val show = ShowsResources.shows[intent.getIntExtra(SHOW_INDEX,0)]

        setSupportActionBar(binding.toolbar)

        initDataContainers(show)

        initReviewRecycler()

        initSubmitButton()
    }

    private fun initDataContainers(show: Show) {
        binding.collapsingToolbarLayout.title = show.name
        binding.toolbar.navigationIcon?.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        binding.toolbar.setNavigationOnClickListener {
            this.finish()
        }
        binding.collapsingToolbarLayout.setExpandedTitleColor(Color.BLACK)
        binding.collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK)
        binding.showImageView.setImageResource(show.imageResourceId)
        binding.descriptionTextView.text = show.description
    }

    private fun initSubmitButton() {
        binding.reviewButton.setOnClickListener{
            showBottomSheet()
        }
    }

    private fun showBottomSheet() {
        val dialog = BottomSheetDialog(this)

        val bottomSheetBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.dismissButton.setOnClickListener{
            dialog.dismiss()
        }

        bottomSheetBinding.submitButton.setOnClickListener {
            val rating = bottomSheetBinding.ratingBar.rating.toInt()
            val comment = bottomSheetBinding.commentField.text.toString()

            val review = Review(rating, comment)
            reviewsAdapter?.addItem(review)

            var newRating = 0.0
            reviewsAdapter?.getItems()?.forEach{review->
                newRating += review.rating
            }

            var reviewCount = reviewsAdapter?.getItemCount()!!
            newRating = newRating / reviewCount

            binding.reviewInfoTextView.text = "${reviewCount} REVIEW${if(reviewCount == 1) "" else "S"}, ${newRating} AVERAGE"

            binding.ratingBar.rating = newRating.toFloat()

            setRecyclerViewVisibility(true)

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setRecyclerViewVisibility(visible:Boolean){
        // Saving processing power :)
        if(binding.reviewsRecyclerView.isVisible == visible)
            return

        if(visible){
            binding.reviewsRecyclerView.isVisible = true
            binding.reviewInfoTextView.isVisible = true
            binding.ratingBar.isVisible = true
            binding.emptyStateLabel.isVisible = false
        }
        else {
            binding.reviewsRecyclerView.isVisible = false
            binding.reviewInfoTextView.isVisible = false
            binding.ratingBar.isVisible = false
            binding.emptyStateLabel.isVisible = true
        }
    }

    private fun initReviewRecycler() {
        reviewsAdapter = ReviewsAdapter(emptyList())

        if(reviewsAdapter?.getItemCount() == 0){
            setRecyclerViewVisibility(false)
        } else {
            setRecyclerViewVisibility(true)
        }

        binding.reviewsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.reviewsRecyclerView.adapter = reviewsAdapter

        binding.reviewsRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

}