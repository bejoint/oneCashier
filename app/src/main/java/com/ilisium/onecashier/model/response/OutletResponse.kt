package com.ilisium.onecashier.model.response

data class OutletResponse(
    val response: Response,
    val metadata: Metadata
) {
    data class Response(
        val id: Int,
        val name: String,
        val address: String,
        val description: String
    )

    data class Metadata(
        val message: String,
        val http_code: Int,
        val error_code: Int
    )
}