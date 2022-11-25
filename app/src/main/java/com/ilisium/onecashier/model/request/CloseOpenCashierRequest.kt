package com.ilisium.onecashier.model.request

data class CloseOpenCashierRequest(
    val type: String,
    val money: Int
)