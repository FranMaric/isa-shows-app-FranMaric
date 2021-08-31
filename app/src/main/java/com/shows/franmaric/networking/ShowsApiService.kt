package com.shows.franmaric.networking

import com.shows.franmaric.models.*
import com.shows.franmaric.models.GetShowResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ShowsApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/users/sign_in")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("/shows")
    fun getShows(): Call<GetShowsResponse>

    @GET("/shows/top_rated")
    fun getTopRatedShows(): Call<GetTopRatedShowsResponse>

    @GET("/shows/{showId}")
    fun getShow(@Path("showId") showId: String): Call<GetShowResponse>

    @GET("/shows/{showId}/reviews")
    fun getReviews(@Path("showId") showId: String): Call<GetReviewsResponse>

    @POST("/reviews")
    fun postReview(@Body request: ReviewRequest): Call<PostReviewResponse>

    @Multipart
    @PUT("/users")
    fun uploadProfilePhoto(@Part image: MultipartBody.Part): Call<ProfilePhotoResponse>
}