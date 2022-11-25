package com.ilisium.onecashier.model.response

data class TaxResponse(
    val response: List<Response>,
    val metadata: Metadata
) {
    data class Response(
        val id: Int,
        val name: String,
        val percentage: String
    )

    data class Metadata(
        val message: String,
        val http_code: Int,
        val error_code: Int,
        val error_data: List<Any>
    )
}