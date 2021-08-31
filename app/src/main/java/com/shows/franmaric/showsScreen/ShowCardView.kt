package com.shows.franmaric.showsScreen

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.shows.franmaric.R
import com.shows.franmaric.databinding.ViewShowItemBinding

class ShowCardView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var binding: ViewShowItemBinding =
        ViewShowItemBinding.inflate(LayoutInflater.from(context), this)

    init {
        val pixelPadding = context.resources.getDimensionPixelSize(R.dimen.show_card_padding)
        setPadding(pixelPadding, pixelPadding, pixelPadding, pixelPadding)

        clipToPadding = false
    }

    fun setTitle(title: String) {
        binding.titleTextView.text = title
    }

    fun setDescription(description: String) {
        binding.descriptionTextView.text = description
    }

    fun setImage(@DrawableRes imageResource: Int) {
        binding.showImage.setImageResource(imageResource)
    }

    fun setImage(imageUrl: String) {
        Glide.with(context)
            .load(imageUrl)
            .into(binding.showImage)
    }

    fun setOnClickListener(callback: () -> Unit) {
        binding.card.setOnClickListener {
            callback()
        }
    }
}