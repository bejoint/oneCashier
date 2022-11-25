package com.ilisium.onecashier.model.response

data class DiscountResponse(
    val response: List<Response>,
    val metadata: Metadata
) {
    data class Response(
        val id: Int,
        val name: String,
        val code: String,
        val type: String,
        val quota: Int,
        val used: Int,
        val value: Int,
        val fg_active: String
    )

    data class Metadata(
        val message: String,
        val http_code: Int,
        val error_code: Int,
//        val error_data: List<Any>
    )
}