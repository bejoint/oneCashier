package com.ilisium.onecashier.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ilisium.onecashier.data.api.ApiRepository
import com.ilisium.onecashier.model.Resource
import kotlinx.coroutines.Dispatchers

class AuthViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    fun login(username: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiRepository.login(username, password)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}