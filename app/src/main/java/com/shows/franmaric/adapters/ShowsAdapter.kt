package com.shows.franmaric.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shows.franmaric.databinding.ViewShowItemBinding
import com.shows.franmaric.models.Show

class ShowsAdapter(
    private var items: List<Show>,
    private val onItemClickCallback: (Show) -> Unit
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

    fun setItems(shows: List<Show>) {
        items = shows
        notifyDataSetChanged()
    }

    fun addItem(show: Show) {
        items = items + show
        notifyItemInserted(items.size)
    }

    inner class ShowViewHolder(private val binding: ViewShowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Show) {
            binding.showImage.setImageResource(item.imageResourceId)

            binding.titleTextView.text = item.name

            binding.descriptionTextView.text = item.description

            binding.card.setOnClickListener {
                onItemClickCallback(item)
            }
        }
    }
}