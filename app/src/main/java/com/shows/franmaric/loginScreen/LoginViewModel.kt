package com.shows.franmaric.loginScreen

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shows.franmaric.PREFS_EMAIL_KEY
import com.shows.franmaric.PREFS_PROFILE_PHOTO_URL
import com.shows.franmaric.repository.ShowsRepository

class LoginViewModel(
    val repository: ShowsRepository
) : ViewModel() {
    private val loginResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getLoginResultLiveData(): LiveData<Boolean> {
        return loginResultLiveData
    }

    fun login(
        email: String,
        password: String,
        preferences: SharedPreferences,
        hasInternetConnection: Boolean
    ) {
        repository.login(email, password, hasInternetConnection,{
            loginResultLiveData.value = false
        }) { response ->
            loginResultLiveData.value = response.isSuccessful
            with(preferences.edit()) {
                putString("access-token", response.headers()["access-token"])
                putString("client", response.headers()["client"])
                putString("uid", response.headers()["uid"])
                putString(PREFS_EMAIL_KEY, email)
                putString(PREFS_PROFILE_PHOTO_URL, response.body()?.user?.imageUrl)
                apply()
            }
        }
    }
}