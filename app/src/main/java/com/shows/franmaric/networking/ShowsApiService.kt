package com.shows.franmaric.networking

import com.shows.franmaric.models.*
import com.shows.franmaric.models.GetShowResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ShowsApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/users/sign_in")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("/shows")
    fun getShows(): Call<GetShowsResponse>

    @GET("/shows/{showId}")
    fun getShow(@Path("showId") showId: String): Call<GetShowResponse>

    @GET("/shows/{showId}/reviews")
    fun getReviews(@Path("showId") showId: String): Call<GetReviewsResponse>

    @POST("/reviews")
    fun postReview(@Body request: ReviewRequest): Call<PostReviewResponse>
}