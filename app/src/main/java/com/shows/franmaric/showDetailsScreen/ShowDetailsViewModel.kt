package com.shows.franmaric.showDetailsScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shows.franmaric.models.*
import com.shows.franmaric.networking.ApiModule
import com.shows.franmaric.repository.ShowsRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowDetailsViewModel(
    val repository: ShowsRepository
) : ViewModel() {
    private var reviews = mutableListOf<Review>()

    private val showLiveData: MutableLiveData<ShowResponse> by lazy {
        MutableLiveData<ShowResponse>()
    }
    private val reviewsLiveData: MutableLiveData<List<Review>> by lazy {
        MutableLiveData<List<Review>>()
    }
    private val reviewsAverageRatingLiveData: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>()
    }

    fun getShowLiveData(): LiveData<ShowResponse> {
        return showLiveData
    }

    fun getAverageReviewRatingLiveData(): LiveData<Double> {
        return reviewsAverageRatingLiveData
    }

    fun getReviewsLiveData(): LiveData<List<Review>> {
        return reviewsLiveData
    }

    fun reviewsCount(): Int {
        return reviews.size
    }

    fun addReview(comment: String, rating: Int, hasInternetConnection: Boolean, email: String) {
        showLiveData.value?.id?.let {
            repository.addReview(ReviewRequest(comment, rating, it.toInt()), hasInternetConnection, email) { review ->
                review?.let { review ->
                    reviews.add(0, review)
                    reviewsLiveData.postValue(reviews)
                    reviewsAverageRatingLiveData.postValue(calculateAverageReviewsRating())
                }
            }
        }
    }

    fun removeReview(review: Review) {
        reviews.remove(review)
        reviewsLiveData.value = reviews
    }

    private fun calculateAverageReviewsRating() : Double {
        return reviews.map{it->it.rating}.average()
    }

   fun initShow(showId: String, hasInternetConnection: Boolean) {
       getShow(showId, hasInternetConnection)
       getReview(showId, hasInternetConnection)
   }

    private fun getShow(showId: String, hasInternetConnection: Boolean) {
        repository.getShow(showId, hasInternetConnection) {
            showLiveData.postValue(it)
        }
    }

    private fun getReview(showId: String, hasInternetConnection: Boolean) {
        repository.getReviews(showId, hasInternetConnection) {
            reviews = it.toMutableList()
            reviewsLiveData.postValue(it)
            reviewsAverageRatingLiveData.postValue(calculateAverageReviewsRating())
        }
    }
}