package com.ilisium.onecashier.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class OpenCloseKasirResponse(
    val response: List<Response>,
    val metadata: Metadata
) {
    @Parcelize
    data class Response(
        val open_datetime: String,
        val close_datetime: String,
        val open_balance: String,
        val close_balance: String,
        val total_expected: String,
        val total_actual: String,
        val created_by_user_cashier: Int,
        val outlet_id: Int,
        val created_at: String
    ): Parcelable

    data class Metadata(
        val message: String,
        val detailed_message: Any,
        val http_code: Int,
        val error_code: Int,
        val error_data: List<Any>
    )
}