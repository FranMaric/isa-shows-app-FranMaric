package com.shows.franmaric.showsScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shows.franmaric.models.*
import com.shows.franmaric.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
}