package com.shows.franmaric.showsScreen

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shows.franmaric.PREFS_EMAIL_KEY
import com.shows.franmaric.PREFS_PROFILE_PHOTO_URL
import com.shows.franmaric.PREFS_REMEMBER_ME_KEY
import com.shows.franmaric.models.*
import com.shows.franmaric.networking.ApiModule
import com.shows.franmaric.repository.ShowsRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ShowsViewModel (
    val repository: ShowsRepository
): ViewModel() {
    private val showsLiveData: MutableLiveData<List<ShowResponse>> by lazy {
        MutableLiveData<List<ShowResponse>>()
    }

    fun getShowsLiveData(): LiveData<List<ShowResponse>> {
        return showsLiveData
    }

    fun initShows(){
        getShows(false)
    }

    fun getShows(isTopRated: Boolean = false){
        if(isTopRated){
            ApiModule.retrofit.getTopRatedShows()
                .enqueue(object : Callback<GetTopRatedShowsResponse> {
                    override fun onResponse(
                        call: Call<GetTopRatedShowsResponse>,
                        response: Response<GetTopRatedShowsResponse>
                    ) {
                        if(response.isSuccessful){
                            showsLiveData.value = response.body()?.shows
                        }
                    }

                    override fun onFailure(call: Call<GetTopRatedShowsResponse>, t: Throwable) {
                        showsLiveData.value = emptyList()
                    }
                })
        } else {
            ApiModule.retrofit.getShows()
                .enqueue(object : Callback<GetShowsResponse> {
                    override fun onResponse(
                        call: Call<GetShowsResponse>,
                        response: Response<GetShowsResponse>
                    ) {
                        if (response.isSuccessful) {
                            showsLiveData.value = response.body()?.shows
                        }
                    }

                    override fun onFailure(call: Call<GetShowsResponse>, t: Throwable) {
                        showsLiveData.value = emptyList()
                    }
                })
        }
    }

    fun uploadProfilePhoto(imageUri: String, prefs: SharedPreferences, updateCallback: (String) -> Unit) {
        val file = File(imageUri)
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val profilePic = MultipartBody.Part.createFormData("image", file.name, requestFile)

        ApiModule.retrofit.uploadProfilePhoto(profilePic)
            .enqueue(object : Callback<ProfilePhotoResponse> {
                override fun onResponse(
                    call: Call<ProfilePhotoResponse>,
                    response: Response<ProfilePhotoResponse>
                ) {
                    if(response.isSuccessful){
                        val photoUrl = response.body()?.user?.imageUrl.toString()
                        with(prefs.edit()){
                            putString(PREFS_PROFILE_PHOTO_URL, photoUrl)
                            apply()
                        }
                        updateCallback(photoUrl)
                    }
                }

                override fun onFailure(call: Call<ProfilePhotoResponse>, t: Throwable) {

                }
            })
    }

    fun logout(prefs: SharedPreferences) {
        with(prefs.edit()) {
            remove(PREFS_EMAIL_KEY)
            remove(PREFS_PROFILE_PHOTO_URL)
            putBoolean(PREFS_REMEMBER_ME_KEY, false)
            apply()
        }
    }
}