package com.ilisium.onecashier.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ilisium.onecashier.data.api.ApiRepository
import com.ilisium.onecashier.model.Resource
import kotlinx.coroutines.Dispatchers

class ProfileViewModel (private val apiRepository: ApiRepository) : ViewModel() {

    fun getUserMe() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiRepository.getUserMe()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun historyKasir(
        start_date: String,
        end_date: String
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiRepository.historyKasir(start_date, end_date)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getOutlet(idOutlet: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiRepository.getOutlet(idOutlet)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}