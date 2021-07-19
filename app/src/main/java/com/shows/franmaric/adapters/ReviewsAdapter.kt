package com.shows.franmaric.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shows.franmaric.databinding.ViewReviewItemBinding
import com.shows.franmaric.models.Review

class ReviewsAdapter(
    private var items: List<Review>
) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding =
            ViewReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ReviewViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getItems(): List<Review> {
        return items
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(reviews: List<Review>) {
        items = reviews
        notifyDataSetChanged()
    }

    fun addItem(review: Review) {
        items = items + review
        notifyItemInserted(items.size)
    }

    inner class ReviewViewHolder(private val binding: ViewReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review) {
            binding.nameTextView.text = review.author
            binding.commentTextView.text = review.comment
            binding.ratingTextView.text = review.rating.toString()
            println("BOK izradujem viewholder. Nema na cemu.")
        }
    }
}