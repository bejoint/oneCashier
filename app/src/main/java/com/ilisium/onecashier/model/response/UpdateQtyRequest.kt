package com.ilisium.onecashier.model.response

data class UpdateQtyRequest(
    val quantity: Int,
    val _method: String
)