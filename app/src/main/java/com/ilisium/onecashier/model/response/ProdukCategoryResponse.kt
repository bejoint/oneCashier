package com.ilisium.onecashier.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class ProdukCategoryResponse(
    val response: List<Response>,
    val metadata: Metadata
) {
    @Parcelize
    data class Response(
        val id: Int,
        val name: String
    ): Parcelable

    data class Metadata(
        val message: String,
        val http_code: Int,
        val error_code: Int,
//        val error_data: List<Any>
    )
}