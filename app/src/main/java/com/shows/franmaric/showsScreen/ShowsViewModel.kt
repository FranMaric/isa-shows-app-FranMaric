package com.shows.franmaric.showsScreen

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shows.franmaric.PREFS_PROFILE_PHOTO_URL
import com.shows.franmaric.models.*
import com.shows.franmaric.networking.ApiModule
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ShowsViewModel : ViewModel() {
    private val showsLiveData: MutableLiveData<List<ShowResponse>> by lazy {
        MutableLiveData<List<ShowResponse>>()
    }

    fun getShowsLiveData(): LiveData<List<ShowResponse>> {
        return showsLiveData
    }

    fun initShows(){
        ApiModule.retrofit.getShows()
            .enqueue(object : Callback<GetShowsResponse> {
                override fun onResponse(
                    call: Call<GetShowsResponse>,
                    response: Response<GetShowsResponse>
                ) {
                    if(response.isSuccessful){
                        showsLiveData.value = response.body()?.shows
                    }
                }

                override fun onFailure(call: Call<GetShowsResponse>, t: Throwable) {
                    showsLiveData.value = emptyList()
                }
            })
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
}