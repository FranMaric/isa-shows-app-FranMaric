package com.shows.franmaric.repository

import android.util.Log
import com.shows.franmaric.database.ShowsDatabase
import com.shows.franmaric.database.entities.ShowEntity
import com.shows.franmaric.models.*
import com.shows.franmaric.networking.ShowsApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class ShowsRepository(
    private val showsDatabase: ShowsDatabase,
    private val retrofit: ShowsApiService
) {


    fun login(
        email: String,
        password: String,
        hasInternetConnection: Boolean,
        onFailureCallback: () -> Unit,
        onResponseCallback: (response: Response<LoginResponse>) -> Unit
    ) {
        if (!hasInternetConnection) {
            onFailureCallback()
            return
        }

        retrofit.login(LoginRequest(email, password))
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    onResponseCallback(response)
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    onFailureCallback()
                }
            })
    }

    fun register(
        email: String,
        password: String,
        passwordConfirmation: String,
        hasInternetConnection: Boolean,
        onFailureCallback: () -> Unit,
        onResponseCallback: (response: Response<RegisterResponse>) -> Unit
    ) {
        if (!hasInternetConnection) {
            onFailureCallback()
            return
        }

        retrofit.register(RegisterRequest(email, password, passwordConfirmation))
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) = onResponseCallback(response)

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable)
                   = onFailureCallback()
            })
    }

    fun getShows(
        hasInternetConnection: Boolean,
        callback: (List<ShowResponse>) -> Unit
    ) {
        if (hasInternetConnection) {
            retrofit.getShows()
                .enqueue(object : Callback<GetShowsResponse> {
                    override fun onResponse(
                        call: Call<GetShowsResponse>,
                        response: Response<GetShowsResponse>
                    ) {
                        if (response.body()?.shows != null) {
                            callback(response.body()?.shows!!)
                            writeShowsToDB(response.body()?.shows!!)
                        } else {
                            getShowsFromDB(callback)
                        }
                    }

                    override fun onFailure(call: Call<GetShowsResponse>, t: Throwable) {
                        getShowsFromDB(callback)
                    }
                })
        } else {
            getShowsFromDB(callback)
        }
    }

    private fun getShowsFromDB(callback: (List<ShowResponse>) -> Unit) {
        Executors.newSingleThreadExecutor().execute {
            callback(showsDatabase.showDao().getShows().map {
                ShowResponse(
                    id = it.id,
                    averageRating = it.averageRating,
                    description = it.description,
                    imageUrl = it.imageUrl,
                    noOfReviews = it.noOfReviews,
                    title = it.title
                )
            })
        }
    }

    private fun writeShowsToDB(shows: List<ShowResponse>) {
        Executors.newSingleThreadExecutor().execute {
            showsDatabase.showDao().insertShows(shows.map {
                ShowEntity(
                    id = it.id,
                    averageRating = it.averageRating,
                    description = it.description,
                    imageUrl = it.imageUrl,
                    noOfReviews = it.noOfReviews,
                    title = it.title
                )
            })
        }
    }

    fun getShow(showId: String): ShowResponse {
        //TODO: implement
        return ShowResponse(showId, 0.0, "", "", 0, "")
    }

    fun getReviews(): List<Review> {
        //TODO: implement
        return emptyList()
    }

    fun getReview(): List<ShowResponse> {
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