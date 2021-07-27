package com.shows.franmaric.loginScreen

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shows.franmaric.PREFS_EMAIL_KEY
import com.shows.franmaric.PREFS_PROFILE_PHOTO_URL
import com.shows.franmaric.R
import com.shows.franmaric.models.LoginRequest
import com.shows.franmaric.models.LoginResponse
import com.shows.franmaric.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val loginResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getLoginResultLiveData(): LiveData<Boolean> {
        return loginResultLiveData
    }

    fun login(email: String, password: String, preferences: SharedPreferences) {
        ApiModule.retrofit.login(LoginRequest(email, password))
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    loginResultLiveData.value = response.isSuccessful
                    with(preferences.edit()){
                        putString("access-token", response.headers()["access-token"])
                        putString("client", response.headers()["client"])
                        putString("uid", response.headers()["uid"])
                        putString(PREFS_EMAIL_KEY, email)
                        putString(PREFS_PROFILE_PHOTO_URL, response.body()?.user?.imageUrl)
                        apply()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    loginResultLiveData.value = false
                }
            })
    }
}