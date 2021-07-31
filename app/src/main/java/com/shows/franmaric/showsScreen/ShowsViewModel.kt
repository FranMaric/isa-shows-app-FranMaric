package com.shows.franmaric.showsScreen

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import com.shows.franmaric.PREFS_EMAIL_KEY
import com.shows.franmaric.PREFS_PROFILE_PHOTO_URL
import com.shows.franmaric.PREFS_REMEMBER_ME_KEY
import com.shows.franmaric.models.*
import com.shows.franmaric.networking.ApiModule
import com.shows.franmaric.repository.ShowsRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ShowsViewModel(
    val repository: ShowsRepository
) : ViewModel() {
    private val showsLiveData: MutableLiveData<List<ShowResponse>> by lazy {
        MutableLiveData<List<ShowResponse>>()
    }

    fun getShowsLiveData(): LiveData<List<ShowResponse>> {
        return showsLiveData
    }

    fun getShows(hasInternetConnection: Boolean, isTopRated: Boolean = false) {
        if (isTopRated) {
            ApiModule.retrofit.getTopRatedShows()
                .enqueue(object : Callback<GetTopRatedShowsResponse> {
                    override fun onResponse(
                        call: Call<GetTopRatedShowsResponse>,
                        response: Response<GetTopRatedShowsResponse>
                    ) {
                        if (response.isSuccessful) {
                            showsLiveData.value = response.body()?.shows
                        }
                    }

                    override fun onFailure(call: Call<GetTopRatedShowsResponse>, t: Throwable) {
                        showsLiveData.value = emptyList()
                    }
                })
        } else {
            repository.getShows(hasInternetConnection) {
                showsLiveData.postValue(it)
            }
        }
    }

    fun uploadProfilePhoto(imageUri: String, avatarUri: String, prefs: SharedPreferences, hasInternetConnection: Boolean,updateCallback: () -> Unit)
        = repository.uploadProfilePhoto(imageUri, avatarUri, prefs,hasInternetConnection, updateCallback)

    fun checkForOfflinePhotoToUpload(imageUri: String, avatarUri: String, prefs: SharedPreferences, hasInternetConnection: Boolean) {
        val currentImageUri = prefs.getString(PREFS_PROFILE_PHOTO_URL,"") ?: return

       if(!currentImageUri.contains("http")) {
           uploadProfilePhoto(imageUri, avatarUri, prefs, hasInternetConnection, {})
       }
    }

    fun logout(prefs: SharedPreferences) {
        with(prefs.edit()) {
            remove(PREFS_EMAIL_KEY)
            remove(PREFS_PROFILE_PHOTO_URL)
            putBoolean(PREFS_REMEMBER_ME_KEY, false)
            remove("access-token")
            remove("client")
            remove("uid")
            apply()
        }
    }
}