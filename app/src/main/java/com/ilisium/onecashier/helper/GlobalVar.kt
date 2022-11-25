package com.ilisium.onecashier.helper

import com.ilisium.onecashier.model.DataDiskon
import com.ilisium.onecashier.model.DataTax
import com.ilisium.onecashier.model.response.ProdukResponse

class GlobalVar {
    companion object {
        // development
        const val BASE_URL = "http://cashierone.ilisium.web.id/api/v1/"

        // production
//        const val BASE_URL = "http://onecashier.my.id/api/v1/"

        var searchProduk = ""

        var selectProduk: MutableList<String>?= mutableListOf()

        var dataProduk: List<ProdukResponse.Response>?= mutableListOf()

        var listTaxSelect : MutableList<DataTax>?= mutableListOf()
        var listDiskonSelect : MutableList<DataDiskon>?= mutableListOf()

        var TYPE_OPEN = "open"
        var TYPE_CLOSE = "close"

        var posTabKategori = 0
    }
}