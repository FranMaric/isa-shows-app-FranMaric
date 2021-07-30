package com.shows.franmaric.repository

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.PrimaryKey
import com.shows.franmaric.database.ShowsDatabase
import com.shows.franmaric.database.entities.ReviewEntity
import com.shows.franmaric.database.entities.ShowEntity
import com.shows.franmaric.database.entities.UserEntity
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

    fun getShow(showId: String, hasInternetConnection: Boolean, callback: (ShowResponse) -> Unit) {
        if (hasInternetConnection) {
            retrofit.getShow(showId)
                .enqueue(object : Callback<GetShowResponse> {
                    override fun onResponse(
                        call: Call<GetShowResponse>,
                        response: Response<GetShowResponse>
                    ) {
                        if (response.body()?.show != null) {
                            callback(response.body()?.show!!)
                            writeShowToDB(response.body()?.show!!)
                        } else {
                            getShowFromDB(showId, callback)
                        }
                    }

                    override fun onFailure(call: Call<GetShowResponse>, t: Throwable) {
                        getShowFromDB(showId, callback)
                    }
                })
        } else {
            getShowFromDB(showId, callback)
        }
    }

    private fun writeShowToDB(show : ShowResponse) {
        Executors.newSingleThreadExecutor().execute {
            showsDatabase.showDao().insertShow(
                ShowEntity(
                    id = show.id,
                    averageRating = show.averageRating,
                    description = show.description,
                    imageUrl = show.imageUrl,
                    noOfReviews = show.noOfReviews,
                    title = show.title
                )
            )
        }
    }

    private fun getShowFromDB(showId: String, callback: (ShowResponse) -> Unit) {
        Executors.newSingleThreadExecutor().execute {
            val showEntity = showsDatabase.showDao().getShow(showId)
            callback(
                ShowResponse(
                    id = showEntity.id,
                    averageRating = showEntity.averageRating,
                    description = showEntity.description,
                    imageUrl = showEntity.imageUrl,
                    noOfReviews = showEntity.noOfReviews,
                    title = showEntity.title
                )
            )
        }
    }

    fun getReviews(showId: String, hasInternetConnection: Boolean, callback: (List<Review>) -> Unit) {
        if (hasInternetConnection) {
            retrofit.getReviews(showId)
                .enqueue(object : Callback<GetReviewsResponse> {
                    override fun onResponse(
                        call: Call<GetReviewsResponse>,
                        response: Response<GetReviewsResponse>
                    ) {
                        if (response.body()?.reviews != null) {
                            callback(response.body()?.reviews!!)
                            writeReviewsToDB(response.body()?.reviews!!)
                        } else {
                            getReviewsFromDB(showId.toInt(), callback)
                        }
                    }

                    override fun onFailure(call: Call<GetReviewsResponse>, t: Throwable) {
                        getReviewsFromDB(showId.toInt(), callback)
                    }
                })
        } else {
            getReviewsFromDB(showId.toInt(), callback)
        }
    }

    private fun getReviewsFromDB(showId: Int, callback: (List<Review>) -> Unit) {
        Executors.newSingleThreadExecutor().execute {
            callback(
                showsDatabase.reviewDao().getReviews(showId).map {
                    Review(
                        id = it.id,
                        comment = it.comment,
                        rating = it.rating,
                        showId = it.showId,
                        user = User(
                            id = it.user.id,
                            email = it.user.email,
                            imageUrl = it.user.imageUrl
                        )
                    )
                }
            )
        }
    }

    private fun writeReviewsToDB(reviews: List<Review>) {
        Executors.newSingleThreadExecutor().execute {
            showsDatabase.reviewDao().insertReviews(reviews.map {
                ReviewEntity(
                    id = it.id,
                    comment = it.comment,
                    rating = it.rating,
                    showId = it.showId,
                    user = UserEntity(
                        id = it.user.id,
                        email = it.user.email,
                        imageUrl = it.user.imageUrl
                    )
                )
            })
        }
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