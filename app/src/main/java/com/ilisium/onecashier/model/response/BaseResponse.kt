package com.ilisium.onecashier.model.response

data class BaseResponse(
//    val response: Any,
    val metadata: Metadata
) {
    data class Metadata(
        val message: String,
        val http_code: Int,
        val error_code: Int,
//        val error_data: List<Any>
    )
}