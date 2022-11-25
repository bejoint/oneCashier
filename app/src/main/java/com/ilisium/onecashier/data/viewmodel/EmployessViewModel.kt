package com.ilisium.onecashier.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ilisium.onecashier.data.api.ApiRepository
import com.ilisium.onecashier.model.Resource
import kotlinx.coroutines.Dispatchers

class EmployessViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    fun getEmployees(outlet_id: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiRepository.getEmployees(outlet_id)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}