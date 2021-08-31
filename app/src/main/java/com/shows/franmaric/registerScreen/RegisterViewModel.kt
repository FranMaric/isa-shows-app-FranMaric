package com.shows.franmaric.registerScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shows.franmaric.models.RegisterRequest
import com.shows.franmaric.models.RegisterResponse
import com.shows.franmaric.networking.ApiModule
import com.shows.franmaric.repository.ShowsRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(
    val repository: ShowsRepository
) : ViewModel() {

    private val registrationResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getRegistrationResultLiveData(): LiveData<Boolean> {
        return registrationResultLiveData
    }

    fun register(
        email: String,
        password: String,
        passwordConfirmation: String,
        hasInternetConnection: Boolean
    ) {
        repository.register(email, password, passwordConfirmation, hasInternetConnection, {
            registrationResultLiveData.value = false
        }) { response ->
            registrationResultLiveData.value = response.isSuccessful
        }
    }
}