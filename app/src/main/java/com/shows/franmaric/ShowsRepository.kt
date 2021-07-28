package com.shows.franmaric

import com.shows.franmaric.database.ShowsDatabase
import com.shows.franmaric.models.Review
import com.shows.franmaric.models.ShowResponse
import retrofit2.Retrofit

class ShowsRepository(
    val showsDatabase: ShowsDatabase,
    val retrofit: Retrofit
) {
    fun hasInternetConnection(): Boolean {
        //TODO: implement
        return false
    }

    fun login() {
        //TODO: implement
    }

    fun register() {
        //TODO: implement
    }

    fun getShows(): List<ShowResponse>{
        //TODO: implement
        return emptyList()
    }

    fun getShow(showId: String): ShowResponse{
        //TODO: implement
        return ShowResponse(showId,0.0,"","",0,"")
    }

    fun getReviews(): List<Review>{
        //TODO: implement
        return emptyList()
    }

    fun getReview(): List<ShowResponse>{
        //TODO: implement
        return emptyList()
    }

    fun postReview(review: Review) {
        //TODO: implement
    }

    fun uploadProfilePhoto() {
        //TODO: implement
    }
}