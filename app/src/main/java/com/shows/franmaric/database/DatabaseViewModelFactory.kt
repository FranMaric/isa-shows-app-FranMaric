package com.shows.franmaric.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shows.franmaric.showDetailsScreen.ShowDetailsViewModel
import com.shows.franmaric.showsScreen.ShowsViewModel

class DatabaseViewModelFactory(val database: ShowsDatabase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowsViewModel::class.java)) {
            return ShowsViewModel(database) as T
        } else if (modelClass.isAssignableFrom(ShowDetailsViewModel::class.java)) {
            return ShowDetailsViewModel(database) as T
        }
        throw IllegalArgumentException("Sorry, ne mozemo radit s ne ShowsViewModel i ShowDetailsViewModel klasama")
    }
}