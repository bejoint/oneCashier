package com.ilisium.onecashier.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ilisium.onecashier.helper.GlobalVar

class SearchViewModel: ViewModel() {
    private var _title_1 = MutableLiveData<String>()

    init {
        _title_1.value = GlobalVar.searchProduk
    }

    val title_1: MutableLiveData<String>
        get() = _title_1

    fun changeSearch () {
        _title_1.value = GlobalVar.searchProduk
    }
}