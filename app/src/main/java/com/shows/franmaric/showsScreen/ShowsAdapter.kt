package com.shows.franmaric.showsScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shows.franmaric.databinding.ViewShowItemBinding
import com.shows.franmaric.models.ShowResponse

class ShowsAdapter(
    private var items: List<ShowResponse>,
    private val context : Context,
    private val onItemClickCallback: (ShowResponse) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val showCardView = ShowCardView(parent.context)

        return ShowViewHolder(showCardView)
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

    inner class ShowViewHolder(private val showCardView: ShowCardView) :
        RecyclerView.ViewHolder(showCardView) {

        fun bind(item: ShowResponse) {
            showCardView.setImage(item.imageUrl)

            showCardView.setTitle(item.title)

            showCardView.setDescription(item.description ?: "")

            showCardView.setOnClickListener {
                onItemClickCallback(item)
            }
        }
    }
}