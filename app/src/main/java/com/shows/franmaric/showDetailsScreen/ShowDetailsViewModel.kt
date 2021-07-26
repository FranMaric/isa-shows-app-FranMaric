package com.shows.franmaric.showDetailsScreen

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shows.franmaric.PREFS_EMAIL_KEY
import com.shows.franmaric.models.*
import com.shows.franmaric.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowDetailsViewModel : ViewModel() {
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

    fun setShow(show: ShowResponse) {
        showLiveData.value = show
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
                            reviewsAverageRatingLiveData.value = calculateAverageReviewsRating()
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

    private fun calculateAverageReviewsRating(): Double {
        return reviews.map{it->it.rating}.average()
    }

   fun initShow(showId: String){
       ApiModule.retrofit.getShow(showId)
           .enqueue(object : Callback<GetShowResponse> {
               override fun onResponse(
                   call: Call<GetShowResponse>,
                   response: Response<GetShowResponse>
               ) {
                   if(response.isSuccessful) {
                        showLiveData.value = response.body()?.show
                   }
               }
               override fun onFailure(call: Call<GetShowResponse>, t: Throwable) {
                    //TODO: update viewModel onFailure
               }
           })

       ApiModule.retrofit.getReviews(showId)
           .enqueue(object : Callback<GetReviewsResponse> {
               override fun onResponse(
                   call: Call<GetReviewsResponse>,
                   response: Response<GetReviewsResponse>
               ) {
                   if(response.isSuccessful) {
                       reviews = response.body()?.reviews?.toMutableList() ?: mutableListOf<Review>()
                       reviewsAverageRatingLiveData.value = calculateAverageReviewsRating()
                       reviewsLiveData.value = reviews
                   }
               }
               override fun onFailure(call: Call<GetReviewsResponse>, t: Throwable) {
                   //TODO: update viewModel onFailure
               }
           })
   }
}