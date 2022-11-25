package com.ilisium.onecashier.model.response

data class LoginResponse(
    val response: Response,
    val metadata: Metadata
) {
    data class Response(
        val token: String,
        val user: User
    ) {
        data class User(
            val id: Int,
            val username: String,
            val name: String,
            val image: String,
            val outlet_id: Int
        )
    }

    data class Metadata(
        val message: String,
        val http_code: Int,
        val error_code: Int,
//        val error_data: List<Any>
    )
}