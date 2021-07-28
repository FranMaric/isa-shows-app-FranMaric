package com.shows.franmaric.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shows.franmaric.database.ShowsDatabase
import com.shows.franmaric.loginScreen.LoginViewModel
import com.shows.franmaric.registerScreen.RegisterViewModel
import com.shows.franmaric.showDetailsScreen.ShowDetailsViewModel
import com.shows.franmaric.showsScreen.ShowsViewModel

class RepositoryViewModelFactory(val repository: ShowsRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowsViewModel::class.java)) {
            return ShowsViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(ShowDetailsViewModel::class.java)) {
            return ShowDetailsViewModel(repository) as T
        } else if(modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        } else if(modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Sorry, ne mozemo radit s ne ShowsViewModel i ShowDetailsViewModel klasama")
    }
}