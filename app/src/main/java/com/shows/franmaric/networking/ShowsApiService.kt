package com.shows.franmaric.networking

import com.shows.franmaric.models.RegisterRequest
import com.shows.franmaric.models.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ShowsApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>
}