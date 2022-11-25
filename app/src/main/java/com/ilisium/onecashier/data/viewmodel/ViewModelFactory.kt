package com.ilisium.onecashier.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ilisium.onecashier.data.api.ApiInterface
import com.ilisium.onecashier.data.api.ApiRepository

class ViewModelFactory (private val apiInterface: ApiInterface) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmployessViewModel::class.java)) {
            return EmployessViewModel(ApiRepository(apiInterface)) as T
        }
        else if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(ApiRepository(apiInterface)) as T
        }
        else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(ApiRepository(apiInterface)) as T
        }
        else if (modelClass.isAssignableFrom(ProdukViewModel::class.java)) {
            return ProdukViewModel(ApiRepository(apiInterface)) as T
        }
        else if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return ProdukViewModel(ApiRepository(apiInterface)) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }

}
