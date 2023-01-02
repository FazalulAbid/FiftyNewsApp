package com.fifty.fiftynews.ui.viewmodels

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fifty.fiftynews.repository.UserRepository

class UserViewModelProviderFactory(
    val app: Application,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(app) as T
    }
}