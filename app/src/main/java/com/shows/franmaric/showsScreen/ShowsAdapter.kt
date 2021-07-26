package com.shows.franmaric.showsScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.shows.franmaric.databinding.ViewShowItemBinding
import com.shows.franmaric.models.Show
import com.shows.franmaric.models.ShowResponse

class ShowsAdapter(
    private var items: List<ShowResponse>,
    private val context : Context,
    private val onItemClickCallback: (ShowResponse) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val binding =
            ViewShowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ShowViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(shows: List<ShowResponse>) {
        items = shows
        notifyDataSetChanged()
    }

    fun addItem(show: ShowResponse) {
        items = items + show
        notifyItemInserted(items.size)
    }

    inner class ShowViewHolder(private val binding: ViewShowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShowResponse) {
            Glide.with(context)
                .load(item.imageUrl)
                .into(binding.showImage)

            binding.titleTextView.text = item.title

            binding.descriptionTextView.text = item.description

            binding.card.setOnClickListener {
                onItemClickCallback(item)
            }
        }
    }
}