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

    fun addReview(comment: String, rating: Int) {
        showLiveData.value?.id?.let {
            ApiModule.retrofit.postReview(ReviewRequest(comment, rating, it.toInt()))
                .enqueue(object : Callback<PostReviewResponse> {
                    override fun onResponse(
                        call: Call<PostReviewResponse>,
                        response: Response<PostReviewResponse>
                    ) {
                        if (response.isSuccessful && response.body()?.review != null) {
                            reviews.add(response.body()?.review!!)

                            reviewsLiveData.value = reviews
                        }
                    }

                    override fun onFailure(call: Call<PostReviewResponse>, t: Throwable) {
                        //TODO: update viewModel onFailure
                    }
                })
        }
    }

    fun removeReview(review: Review) {
        reviews.remove(review)
        reviewsLiveData.value = reviews
    }

    fun calculateAverageReviewsRating() : Double {
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