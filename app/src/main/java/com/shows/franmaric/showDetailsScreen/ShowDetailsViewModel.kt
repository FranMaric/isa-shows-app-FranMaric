package com.shows.franmaric.showDetailsScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shows.franmaric.models.Review
import com.shows.franmaric.models.Show

class ShowDetailsViewModel : ViewModel() {
    private val reviews = mutableListOf<Review>()

    private val showsLiveData: MutableLiveData<Show> by lazy {
        MutableLiveData<Show>()
    }
    private val reviewsLiveData: MutableLiveData<List<Review>> by lazy {
        MutableLiveData<List<Review>>()
    }
    private val reviewsAverageRatingLiveData: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>()
    }

    fun getShowLiveData(): LiveData<Show> {
        return showsLiveData
    }

    fun getAverageReviewRatingLiveData(): LiveData<Double> {
        return reviewsAverageRatingLiveData
    }

    fun setShow(show: Show) {
        showsLiveData.value = show
    }

    fun reviewsCount(): Int {
        return reviews.size
    }

    fun getReviewsLiveData(): LiveData<List<Review>> {
        return reviewsLiveData
    }

    fun addReview(review: Review) {
        reviews.add(review)
        reviewsLiveData.value = reviews
        reviewsAverageRatingLiveData.value = calculateAverageReviewsRating()
    }

    fun removeReview(review: Review) {
        reviews.remove(review)
        reviewsLiveData.value = reviews
    }

    private fun calculateAverageReviewsRating(): Double {
        return reviews.map{it->it.rating}.average()
    }
}