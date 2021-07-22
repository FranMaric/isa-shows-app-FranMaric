package com.shows.franmaric.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shows.franmaric.data.ShowsResources
import com.shows.franmaric.models.Show

class ShowsViewModel : ViewModel() {
    private val showsLiveData: MutableLiveData<List<Show>> by lazy {
        MutableLiveData<List<Show>>()
    }

    fun getShowsLiveData(): LiveData<List<Show>> {
        return showsLiveData
    }

    fun initShows(){
        showsLiveData.value = ShowsResources.shows
    }
}