package com.shows.franmaric.repository

import com.shows.franmaric.database.ShowsDatabase
import com.shows.franmaric.models.*
import com.shows.franmaric.networking.ShowsApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

const val GOOGLE_DNS = "8.8.8.8"
const val DNS_PORT = 53
const val TIMEOUT = 1500
const val TAG = "NetworkChecker"

class ShowsRepository(
    private val showsDatabase: ShowsDatabase,
    private val retrofit: ShowsApiService
) {
    private fun hasInternetConnection(): Boolean {
        return try {
            val sock = Socket()
            val socketAddress: SocketAddress = InetSocketAddress(GOOGLE_DNS, DNS_PORT)
            sock.connect(socketAddress, TIMEOUT)
            sock.close()
            true
        } catch (e: IOException) {
            false
        }
    }

    fun login(email: String, password: String, onFailureCallback: () -> Unit, onResponseCallback: (response: Response<LoginResponse>)->Unit) {
        if(!hasInternetConnection()) {
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

    fun register(email: String, password: String, passwordConfirmation: String, onFailureCallback: () -> Unit, onResponseCallback: (response: Response<RegisterResponse>)->Unit) {
        if(!hasInternetConnection()) {
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